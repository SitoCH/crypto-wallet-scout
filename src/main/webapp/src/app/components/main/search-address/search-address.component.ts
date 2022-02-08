import { Component } from '@angular/core';
import { TokenBalance, UserCollectionSummary } from "../../../../generated/client";
import { AddressBalanceService } from "../../../services/address-balance.service";
import { Select, Store } from "@ngxs/store";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { AddAddressToCollection, UserCollectionsState } from "../../../state/user-collections.state";
import { Observable } from "rxjs";

@Component({
  selector: 'app-search-address',
  templateUrl: './search-address.component.html',
  styleUrls: ['./search-address.component.scss']
})
export class SearchAddressComponent {

  addressToSearch = '';
  activeAddress = '';
  firstSearchDone = false;
  searchRunning = false;
  addressBalance: TokenBalance[] | null = null;

  @Select(UserCollectionsState.getUserCollections)
  userCollections$!: Observable<UserCollectionSummary[]>;

  selectedUserCollectionId: number | undefined;

  constructor(private addressService: AddressBalanceService,
              private store: Store,
              private modalService: NgbModal) {
  }

  getAccountBalance(activeAddress: string) {
    this.firstSearchDone = true;
    this.addressBalance = null;
    this.searchRunning = true;
    this.addressService.getAddressBalance(activeAddress)
      .then(data => {
        this.activeAddress = activeAddress;
        this.searchRunning = false;
        this.addressBalance = data;
      })
  }

  addToCollection(content: any) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then(_ => {
      if (this.selectedUserCollectionId) {
        this.store.dispatch(new AddAddressToCollection(this.selectedUserCollectionId, this.activeAddress));
      }
    });
  }
}
