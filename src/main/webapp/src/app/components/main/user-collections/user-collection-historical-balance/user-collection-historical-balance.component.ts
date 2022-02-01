import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Store } from "@ngxs/store";
import { GetHistoricalBalance, UserCollectionsState } from "../../../../state/user-collections.state";
import { map, Observable } from "rxjs";

@Component({
  selector: 'app-user-collection-historical-balance',
  templateUrl: './user-collection-historical-balance.component.html',
  styleUrls: ['./user-collection-historical-balance.component.scss']
})
export class UserCollectionHistoricalBalanceComponent implements OnChanges {

  @Input()
  collectionId!: number;
  @Input()
  currentUsdValue?: number;

  historicalBalance$?: Observable<{ [index: string]: number }>;

  constructor(private store: Store) {
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['collectionId'] && this.collectionId) {
      this.historicalBalance$ = this.store.select(UserCollectionsState.getHistoricalAddressBalance(this.collectionId)).pipe(
        map(result => {
          let snapshots = Object.assign({}, result?.snapshots || {});
          snapshots[new Date().toISOString()] = this.currentUsdValue || 0;
          return snapshots;
        }));
    }

    if ((this.currentUsdValue || this.currentUsdValue === 0) && this.collectionId) {
      this.store.dispatch(new GetHistoricalBalance(this.collectionId));
    }
  }

}


