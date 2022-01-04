import { Component } from '@angular/core';
import { AddressBalanceService } from "../services/address-balance.service";
import { AddressBalance } from "../../generated/client";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {

  public accountToSearch: string = '';
  addressBalance: AddressBalance | null = null;
  totalBalance: number | null = null;

  constructor(private addressService: AddressBalanceService) {
  }

  getAccountBalance(account: string) {
    this.addressBalance = null;
    this.addressService.getAddressBalance(account)
      .then(data => {
        this.addressBalance = data;
        this.totalBalance = data.tokenBalances.reduce((sum, current) => sum + current.usdValue, 0);
      })
  }
}
