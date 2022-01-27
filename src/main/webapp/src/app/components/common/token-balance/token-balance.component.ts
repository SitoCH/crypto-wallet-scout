import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { LocalTokenList, TokenList } from "../../../utils/balance";
import { AddressBalance, Allocation } from "../../../../generated/client";

@Component({
  selector: 'app-token-balance',
  templateUrl: './token-balance.component.html',
  styleUrls: ['./token-balance.component.scss']
})
export class TokenBalanceComponent implements OnChanges {

  @Input()
  tokens!: TokenList;

  liquidTokens?: LocalTokenList;
  stackedTokens?: LocalTokenList;
  rewardsTokens?: LocalTokenList;

  ngOnChanges(changes: SimpleChanges) {
    if (changes['tokens'].currentValue) {
      this.liquidTokens = {
        tokenBalances: this.getTokensByAllocation(this.tokens, Allocation.LIQUID)
      };
      this.stackedTokens = {
        tokenBalances: this.getTokensByAllocation(this.tokens, Allocation.STACKED)
      };
      this.rewardsTokens = {
        tokenBalances: this.getTokensByAllocation(this.tokens, Allocation.UNCLAIMED_REWARDS)
      };
    }
  }

  private getTokensByAllocation(data: AddressBalance, allocation: Allocation) {
    return data.tokenBalances
      .filter(x => x.allocation === allocation);
  }

}
