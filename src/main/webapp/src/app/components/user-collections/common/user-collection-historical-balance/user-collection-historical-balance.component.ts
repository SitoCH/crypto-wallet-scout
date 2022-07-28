import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Store } from "@ngxs/store";
import { GetHistoricalBalance, UserCollectionsState } from "../../../../state/user-collections.state";
import {
  AbstractHistoricalBalanceComponent
} from "../address-historical-balance/abstract-historical-balance.component";

@Component({
  selector: 'app-user-collection-historical-balance',
  templateUrl: './user-collection-historical-balance.component.html',
  styleUrls: ['./user-collection-historical-balance.component.scss']
})
export class UserCollectionHistoricalBalanceComponent extends AbstractHistoricalBalanceComponent implements OnChanges {

  @Input()
  collectionId!: number;
  @Input()
  currentUsdValue?: number;

  constructor(private store: Store) {
    super();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['collectionId'] && this.collectionId) {
      this.store.select(UserCollectionsState.getHistoricalAddressBalance(this.collectionId))
        .subscribe(result => {
          this.originalSnapshots = result?.snapshots;
          this.refreshHistoricalBalance(this.currentUsdValue);
        });
    }

    if (changes['collectionId'] && this.collectionId) {
      this.store.dispatch(new GetHistoricalBalance(this.collectionId));
    }
    if (changes['currentUsdValue']) {
      this.refreshHistoricalBalance(this.currentUsdValue);
    }
  }

}


