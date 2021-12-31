import { Component } from '@angular/core';
import { AddressService } from "../services/address.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {

  public accountToSearch: string = '';
  result: string = '';

  constructor(private addressService: AddressService) {
  }

  getAccountBalance(account: string) {
    this.addressService.getAccountBalance(account)
      .subscribe(data => this.result = JSON.stringify(data))
  }
}
