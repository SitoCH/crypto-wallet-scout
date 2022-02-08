import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Allocation, TokenBalance } from "../../../../generated/client";

@Component({
  selector: 'app-token-balance-overview',
  templateUrl: './token-balance-overview.component.html',
  styleUrls: ['./token-balance-overview.component.scss']
})
export class TokenBalanceOverviewComponent implements OnChanges {

  @Input()
  tokens!: TokenBalance[];

  totalBalance: number | null = null;
  liquidBalance: number | null = null;
  stackedBalance: number | null = null;
  unclaimedRewardsBalance: number | null = null;

  ngOnChanges(changes: SimpleChanges) {
    if (changes['tokens'].currentValue) {
      this.totalBalance = this.tokens.reduce((sum, current) => sum + current.usdValue, 0);
      this.liquidBalance = this.getBalanceByAllocation(this.tokens, Allocation.LIQUID);
      this.stackedBalance = this.getBalanceByAllocation(this.tokens, Allocation.STACKED);
      this.unclaimedRewardsBalance = this.getBalanceByAllocation(this.tokens, Allocation.UNCLAIMED_REWARDS);
    }
  }

  private getBalanceByAllocation(tokenBalances: TokenBalance[], allocation: Allocation) {
    return tokenBalances
      .filter(x => x.allocation === allocation)
      .reduce((sum, current) => sum + current.usdValue, 0);
  }

}
