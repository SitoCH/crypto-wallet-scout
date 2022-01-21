import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { UserCollectionService } from "../../../../services/user-collection.service";
import { AddressBalance, TokenBalance, TokenResult } from "../../../../../generated/client";
import { mergeMap, Observable } from "rxjs";
import { GetTokenById, TokenState } from "../../../../state/token.state";
import { Store } from "@ngxs/store";

@Component({
  selector: 'app-user-collection-detail',
  templateUrl: './user-collection-detail.component.html',
  styleUrls: ['./user-collection-detail.component.scss']
})
export class UserCollectionDetailComponent implements OnInit {

  id!: number;
  addressBalances: AddressBalance[] | null = null;
  aggregatedTokenBalances: TokenBalance[] | null = null;
  collectionUsdValue: number | null = null;

  constructor(private userCollectionService: UserCollectionService,
              private route: ActivatedRoute,
              private store: Store) {
  }

  ngOnInit(): void {
    this.route.params
      .subscribe((params: any) => {
          this.id = params.id;
          this.loadBalance();
        }
      );
  }

  private loadBalance() {
    this.addressBalances = null;
    this.collectionUsdValue = null;
    this.aggregatedTokenBalances = null;
    this.userCollectionService.getAddressBalance(this.id)
      .then(data => {
        this.addressBalances = data;
        let collectionUsdValue = 0;
        let tokens = new Map<string, TokenBalance>();
        data.forEach(addressBalance => {
          addressBalance.tokenBalances.forEach(tokenBalance => {
            collectionUsdValue += tokenBalance.usdValue;
            if (!tokens.has(tokenBalance.tokenId)) {
              tokens.set(tokenBalance.tokenId, tokenBalance);
            } else {
              tokens.get(tokenBalance.tokenId)!.nativeValue += tokenBalance.nativeValue;
              tokens.get(tokenBalance.tokenId)!.usdValue += tokenBalance.usdValue;
            }
          });
        });
        this.aggregatedTokenBalances = Array.from(tokens, ([name, value]) => (value));
        this.collectionUsdValue = collectionUsdValue;
      })
  }

  getToken(tokenId: string): Observable<TokenResult | undefined> {
    return this.store
      .dispatch(new GetTokenById(tokenId))
      .pipe(mergeMap(() => this.store.select(TokenState.getTokenById(tokenId))))
  }
}
