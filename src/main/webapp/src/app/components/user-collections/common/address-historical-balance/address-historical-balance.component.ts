import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Store } from "@ngxs/store";
import { AddressState, GetHistoricalBalance } from "../../../../state/address.state";
import { AbstractHistoricalBalanceComponent } from "./abstract-historical-balance.component";

@Component({
  selector: 'app-address-historical-balance',
  templateUrl: './address-historical-balance.component.html',
  styleUrls: ['./address-historical-balance.component.scss']
})
export class AddressHistoricalBalanceComponent extends AbstractHistoricalBalanceComponent implements OnChanges {

  @Input()
  address!: string;
  @Input()
  currentUsdValue?: number;

  constructor(private store: Store) {
    super();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['address'] && this.address) {
      this.store.select(AddressState.getHistoricalAddressBalance(this.address))
        .subscribe(result => {
          this.originalSnapshots = result?.balance.snapshots;
          this.refreshHistoricalBalance(this.currentUsdValue);
        });

      this.store.dispatch(new GetHistoricalBalance(this.address));
    }

    if (changes['currentUsdValue']) {
      this.refreshHistoricalBalance(this.currentUsdValue);
    }
  }

}
