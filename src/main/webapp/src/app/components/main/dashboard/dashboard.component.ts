import { Component, OnInit } from '@angular/core';
import { AddressBalance, Allocation, UserCollectionSummary } from "../../../../generated/client";
import { AddressBalanceService } from "../../../services/address-balance.service";
import { Select, Store } from "@ngxs/store";
import {
  AddAddressToCollection,
  GetUserCollections,
  UserCollectionsState
} from "../../../state/user-collections.state";
import { Observable } from "rxjs";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  @Select(UserCollectionsState.getUserCollections)
  userCollections$!: Observable<UserCollectionSummary[]>;

  selectedUserCollectionId: number | undefined;

  accountToSearch = '';
  searchRunning = false;
  addressBalance: AddressBalance | null = null;
  totalBalance: number | null = null;
  liquidBalance: number | null = null;
  stackedBalance: number | null = null;
  unclaimedRewardsBalance: number | null = null;

  constructor(private addressService: AddressBalanceService,
              private store: Store,
              private modalService: NgbModal) {
  }

  ngOnInit(): void {
    this.store.dispatch(new GetUserCollections());
  }

  getAccountBalance(account: string) {
    this.addressBalance = null;
    this.searchRunning = true;
    this.addressService.getAddressBalance(account)
      .then(data => {
        this.searchRunning = false;
        this.addressBalance = data;
        this.totalBalance = data.tokenBalances.reduce((sum, current) => sum + current.usdValue, 0);
        this.liquidBalance = this.getBalanceByAllocation(data, Allocation.LIQUID);
        this.stackedBalance = this.getBalanceByAllocation(data, Allocation.STACKED);
        this.unclaimedRewardsBalance = this.getBalanceByAllocation(data, Allocation.UNCLAIMED_REWARDS);
      })
  }

  private getBalanceByAllocation(data: AddressBalance, allocation: Allocation) {
    return data.tokenBalances
      .filter(x => x.allocation === allocation)
      .reduce((sum, current) => sum + current.usdValue, 0);
  }

  open(content: any) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then((result) => {
      //this.closeResult = `Closed with: ${result}`;
      if (this.selectedUserCollectionId) {
        this.store.dispatch(new AddAddressToCollection(this.selectedUserCollectionId, this.accountToSearch));
      }
    }, (reason) => {
      //this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }
}
