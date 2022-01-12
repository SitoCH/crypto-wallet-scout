import { Component } from '@angular/core';
import { AddressBalance } from "../../../../generated/client";
import { AddressBalanceService } from "../../../services/address-balance.service";

@Component({
  selector: 'app-search-address',
  templateUrl: './search-address.component.html',
  styleUrls: ['./search-address.component.scss']
})
export class SearchAddressComponent {

  accountToSearch = '';
  firstSearchDone = false;
  searchRunning = false;
  addressBalance: AddressBalance | null = null;

  constructor(private addressService: AddressBalanceService) {
  }

  getAccountBalance(account: string) {
    this.firstSearchDone = true;
    this.addressBalance = null;
    this.searchRunning = true;
    this.addressService.getAddressBalance(account)
      .then(data => {
        this.searchRunning = false;
        this.addressBalance = data;
      })
  }

}
