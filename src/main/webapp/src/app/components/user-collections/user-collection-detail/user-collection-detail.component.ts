import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { UserCollectionService } from "../../../services/user-collection.service";
import { TokenBalance, UserCollectionSummary } from "../../../../generated/client";
import { Store } from "@ngxs/store";
import { UserCollectionsState } from "../../../state/user-collections.state";
import { map, mergeMap, Observable, of } from "rxjs";
import { ApplicationState, ToggleGroupTokenTable } from "../../../state/application.state";
import { IconProp } from "@fortawesome/fontawesome-svg-core";

@Component({
  selector: 'app-user-collection-detail',
  templateUrl: './user-collection-detail.component.html',
  styleUrls: ['./user-collection-detail.component.scss']
})
export class UserCollectionDetailComponent implements OnInit {

  id!: number;
  aggregatedTokenBalances: TokenBalance[] | null = null;
  collectionUsdValue?: number;
  userCollection$?: Observable<UserCollectionSummary | undefined>;
  tokenTableGroupingIcon$!: Observable<IconProp>;

  constructor(private userCollectionService: UserCollectionService,
              private route: ActivatedRoute,
              private store: Store) {
  }

  ngOnInit(): void {
    this.tokenTableGroupingIcon$ = this.store.select(ApplicationState.isGroupTokenTable).pipe(
      map(isGrouped => {
        return isGrouped ? 'object-group' : 'object-ungroup';
      })
    )

    this.route.params
      .subscribe((params: any) => {
          this.id = params.id;
          this.userCollection$ = this.store.select(UserCollectionsState.getUserCollections).pipe(
            mergeMap(collections => collections.filter(x => x.id == this.id))
          );
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
      .then(tokenBalances => {
        this.collectionUsdValue = 0;
        let tokens = new Map<string, TokenBalance>();
        tokenBalances.forEach(tokenBalance => {
          let key = UserCollectionDetailComponent.makeKey(tokenBalance);
          this.collectionUsdValue! += tokenBalance.usdValue;
          if (!tokens.has(key)) {
            tokens.set(key, tokenBalance);
          } else {
            tokens.get(key)!.nativeValue += tokenBalance.nativeValue;
            tokens.get(key)!.usdValue += tokenBalance.usdValue;
          }
        });

        this.aggregatedTokenBalances = Array.from(tokens, ([_, value]) => (value));
      })
  }

  onGroupingChange() {
    this.store.dispatch(new ToggleGroupTokenTable())
  }
}
