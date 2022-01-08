import { Component } from '@angular/core';
import { AddressBalance, Allocation } from "../../../../generated/client";
import { AddressBalanceService } from "../../../services/address-balance.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {

  accountToSearch = '';
  searchRunning = false;
  addressBalance: AddressBalance | null = null;
  totalBalance: number | null = null;
  liquidBalance: number | null = null;
  stackedBalance: number | null = null;
  unclaimedRewardsBalance: number | null = null;

  constructor(private addressService: AddressBalanceService) {
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
}
