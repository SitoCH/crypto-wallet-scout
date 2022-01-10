import { Component, Input } from '@angular/core';
import { AddressBalance, UserCollectionSummary } from "../../../../generated/client";
import { Select, Store } from "@ngxs/store";
import { AddAddressToCollection, UserCollectionsState } from "../../../state/user-collections.state";
import { Observable } from "rxjs";
import { AddressBalanceService } from "../../../services/address-balance.service";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-address-balance-table',
  templateUrl: './address-balance-table.component.html',
  styleUrls: ['./address-balance-table.component.scss']
})
export class AddressBalanceTableComponent {

  @Input()
  address!: string;

  @Input()
  addressBalance!: AddressBalance;

  @Select(UserCollectionsState.getUserCollections)
  userCollections$!: Observable<UserCollectionSummary[]>;

  selectedUserCollectionId: number | undefined;

  constructor(private addressService: AddressBalanceService,
              private store: Store,
              private modalService: NgbModal) {
  }

  open(content: any) {
    this.modalService.open(content, {ariaLabelledBy: 'modal-basic-title'}).result.then(_ => {
      if (this.selectedUserCollectionId) {
        this.store.dispatch(new AddAddressToCollection(this.selectedUserCollectionId, this.address));
      }
    });
  }
}
