import { Observable, of } from "rxjs";


export class AbstractHistoricalBalanceComponent {


  originalSnapshots?: { [index: string]: number };
  historicalBalance$?: Observable<{ [index: string]: number }>;

  refreshHistoricalBalance(currentUsdValue: number | undefined) {
    if (this.originalSnapshots && currentUsdValue) {
      let snapshots = Object.assign({}, this.originalSnapshots || {});
      snapshots[new Date().toISOString()] = currentUsdValue;
      this.historicalBalance$ = of(snapshots);
    } else {
      this.historicalBalance$ = of({});
    }
  }
}
