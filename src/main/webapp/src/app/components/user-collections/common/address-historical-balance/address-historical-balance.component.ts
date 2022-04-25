import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { map, Observable } from "rxjs";
import { Store } from "@ngxs/store";
import { AddressState, GetHistoricalBalance } from "../../../../state/address.state";
import { ApplicationState } from "../../../../state/application.state";

@Component({
  selector: 'app-address-historical-balance',
  templateUrl: './address-historical-balance.component.html',
  styleUrls: ['./address-historical-balance.component.scss']
})
export class AddressHistoricalBalanceComponent implements OnChanges {

  @Input()
  address!: string;
  @Input()
  currentUsdValue?: number;

  historicalBalance$?: Observable<{ [index: string]: number }>;

  constructor(private store: Store) {
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['address'] && this.address) {
      this.historicalBalance$ = this.store.select(AddressState.getHistoricalAddressBalance(this.address)).pipe(
        map(result => {
          let snapshots = Object.assign({}, result?.balance.snapshots || {});
          if (!result?.lotsIncluded) {
            snapshots[new Date().toISOString()] = this.currentUsdValue || 0;
          }
          return snapshots;
        }));
    }

    if ((this.currentUsdValue || this.currentUsdValue === 0) && this.address) {
      this.store.dispatch(new GetHistoricalBalance(this.address, this.store.selectSnapshot(ApplicationState.includeLotsInHistoricalBalances)));
    }

    this.store.select(ApplicationState.includeLotsInHistoricalBalances).subscribe(includeLots => {
      this.store.dispatch(new GetHistoricalBalance(this.address, includeLots));
    })
  }

}
