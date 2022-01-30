import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { UserCollectionService } from "../../../../services/user-collection.service";
import { TokenBalance } from "../../../../../generated/client";
import { TokenList } from "../../../../utils/balance";

@Component({
  selector: 'app-user-collection-detail',
  templateUrl: './user-collection-detail.component.html',
  styleUrls: ['./user-collection-detail.component.scss']
})
export class UserCollectionDetailComponent implements OnInit {

  id!: number;
  aggregatedTokenBalances: TokenList | null = null;
  collectionUsdValue?: number;

  constructor(private userCollectionService: UserCollectionService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route.params
      .subscribe((params: any) => {
          this.id = params.id;
          this.loadBalance();
        }
      );
  }

  private static makeKey(tokenBalance: TokenBalance) {
    return tokenBalance.tokenId + '-' + tokenBalance.allocation + '-' + tokenBalance.network;
  }

  private loadBalance() {
    this.aggregatedTokenBalances = null;
    this.userCollectionService.getAddressBalance(this.id)
      .then(data => {
        this.collectionUsdValue = 0;
        let tokens = new Map<string, TokenBalance>();
        data.forEach(addressBalance => {
          addressBalance.tokenBalances.forEach(tokenBalance => {
            let key = UserCollectionDetailComponent.makeKey(tokenBalance);
            this.collectionUsdValue! += tokenBalance.usdValue;
            if (!tokens.has(key)) {
              tokens.set(key, tokenBalance);
            } else {
              tokens.get(key)!.nativeValue += tokenBalance.nativeValue;
              tokens.get(key)!.usdValue += tokenBalance.usdValue;
            }
          });
        });

        this.aggregatedTokenBalances = {
          tokenBalances: Array.from(tokens, ([name, value]) => (value))
        };
      })
  }
}
