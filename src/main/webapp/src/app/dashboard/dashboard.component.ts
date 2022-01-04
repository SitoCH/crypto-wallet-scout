import { Component } from '@angular/core';
import { AddressBalanceResourceClient } from "../../generated/client";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {

  public accountToSearch: string = '';
  result: string = '';

  constructor(private addressService: AddressBalanceResourceClient) {
  }

  getAccountBalance(account: string) {
    this.addressService.getAddressBalance(account)
      .then(data => this.result = JSON.stringify(data))
  }
}
