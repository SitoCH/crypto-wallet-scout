import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Allocation, Network, TokenBalance } from "../../../../generated/client";
import { Select } from "@ngxs/store";
import { ApplicationState } from "../../../state/application.state";
import { Observable } from "rxjs";

@Component({
  selector: 'app-token-balance',
  templateUrl: './token-balance.component.html',
  styleUrls: ['./token-balance.component.scss']
})
export class TokenBalanceComponent implements OnChanges {

  @Input()
  tokens!: TokenBalance[];

  @Select(ApplicationState.isGroupTokenTable)
  isGroupTokenTable$!: Observable<boolean>;

  liquidTokens?: TokenBalance[];
  stackedTokens?: TokenBalance[];
  rewardsTokens?: TokenBalance[];
  groupedTokens?: TokenBalance[];

  ngOnChanges(changes: SimpleChanges) {
    if (changes['tokens'].currentValue) {
      this.liquidTokens = this.getTokensByAllocation(this.tokens, Allocation.LIQUID);
      this.stackedTokens = this.getTokensByAllocation(this.tokens, Allocation.STACKED);
      this.rewardsTokens = this.getTokensByAllocation(this.tokens, Allocation.UNCLAIMED_REWARDS);
      this.groupedTokens = this.getGroupedTokens(this.tokens);
    }
  }

  private getTokensByAllocation(tokenBalances: TokenBalance[], allocation: Allocation) {
    return tokenBalances.filter(x => x.allocation === allocation);
  }

  private getGroupedTokens(tokenBalances: TokenBalance[]) {
    let tokens = new Map<string, TokenBalance>();
    tokenBalances.forEach(tokenBalance => {
      let key = tokenBalance.parentTokenId || tokenBalance.tokenId;
      if (!tokens.has(key)) {
        tokens.set(key, {
          tokenId: key,
          usdValue: tokenBalance.usdValue,
          nativeValue: tokenBalance.nativeValue,
          network: Network.TERRA,
          parentTokenId: tokenBalance.parentTokenId,
          allocation: Allocation.LIQUID
        });
      } else {
        tokens.get(key)!.nativeValue += tokenBalance.nativeValue;
        tokens.get(key)!.usdValue += tokenBalance.usdValue;
      }
    });

    return Array.from(tokens, ([_, value]) => (value));
  }

}
