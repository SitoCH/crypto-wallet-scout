import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { AddressBalance, Allocation } from "../../../../generated/client";
import { TokenList } from "../../../utils/balance";

@Component({
  selector: 'app-address-balance-overview',
  templateUrl: './address-balance-overview.component.html',
  styleUrls: ['./address-balance-overview.component.scss']
})
export class AddressBalanceOverviewComponent implements OnChanges {

  @Input()
  addressBalance!: TokenList;

  totalBalance: number | null = null;
  liquidBalance: number | null = null;
  stackedBalance: number | null = null;
  unclaimedRewardsBalance: number | null = null;

  ngOnChanges(changes: SimpleChanges) {
    if (changes['addressBalance'].currentValue) {
      this.totalBalance = this.addressBalance.tokenBalances.reduce((sum, current) => sum + current.usdValue, 0);
      this.liquidBalance = this.getBalanceByAllocation(this.addressBalance, Allocation.LIQUID);
      this.stackedBalance = this.getBalanceByAllocation(this.addressBalance, Allocation.STACKED);
      this.unclaimedRewardsBalance = this.getBalanceByAllocation(this.addressBalance, Allocation.UNCLAIMED_REWARDS);
    }
  }

  private getBalanceByAllocation(data: AddressBalance, allocation: Allocation) {
    return data.tokenBalances
      .filter(x => x.allocation === allocation)
      .reduce((sum, current) => sum + current.usdValue, 0);
  }


}
