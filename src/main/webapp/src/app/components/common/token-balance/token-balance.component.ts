import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Allocation, TokenBalance } from "../../../../generated/client";

@Component({
  selector: 'app-token-balance',
  templateUrl: './token-balance.component.html',
  styleUrls: ['./token-balance.component.scss']
})
export class TokenBalanceComponent implements OnChanges {

  @Input()
  tokens!: TokenBalance[];

  liquidTokens?: TokenBalance[];
  stackedTokens?: TokenBalance[];
  rewardsTokens?: TokenBalance[];

  ngOnChanges(changes: SimpleChanges) {
    if (changes['tokens'].currentValue) {
      this.liquidTokens = this.getTokensByAllocation(this.tokens, Allocation.LIQUID);
      this.stackedTokens = this.getTokensByAllocation(this.tokens, Allocation.STACKED);
      this.rewardsTokens = this.getTokensByAllocation(this.tokens, Allocation.UNCLAIMED_REWARDS);
    }
  }

  private getTokensByAllocation(tokenBalances: TokenBalance[], allocation: Allocation) {
    return tokenBalances.filter(x => x.allocation === allocation);
  }

}
