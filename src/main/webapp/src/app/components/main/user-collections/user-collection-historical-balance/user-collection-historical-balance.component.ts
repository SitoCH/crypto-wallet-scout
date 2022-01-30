import { Component, Inject, Input, LOCALE_ID, OnChanges, SimpleChanges } from '@angular/core';
import { Store } from "@ngxs/store";
import { GetHistoricalBalance, UserCollectionsState } from "../../../../state/user-collections.state";
import { ChartConfiguration } from "chart.js";
import { HistoricalAddressBalance } from "../../../../../generated/client";
import { map, Observable } from "rxjs";
import { formatDate } from "@angular/common";

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

  historicalBalance$?: Observable<ChartConfiguration['data']>;

  lineChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: true,
    scales: {
      x: {},
      'y-axis-0': {
        ticks: {
          callback: function (value) {
            return '$ ' + value.toLocaleString();
          }
        }
      }
    },
    elements: {
      line: {
        tension: 0.2
      }
    },
    plugins: {
      legend: {display: false}
    }
  };

  constructor(private store: Store,
              @Inject(LOCALE_ID) private locale: string) {
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['collectionId'] && this.collectionId) {
      this.historicalBalance$ = this.store.select(UserCollectionsState.getHistoricalAddressBalance(this.collectionId)).pipe(
        map(result => {
          return this.getChartData(result);
        }));
    }

    if (this.currentUsdValue && this.collectionId) {
      this.store.dispatch(new GetHistoricalBalance(this.collectionId));
    }
  }

  getChartData(result: HistoricalAddressBalance | undefined): ChartConfiguration['data'] {
    if (!result) {
      return {
        datasets: []
      };
    }

    let snapshots = result.snapshots;
    if (this.currentUsdValue) {
      snapshots = Object.assign({}, snapshots);
      snapshots[new Date().toISOString()] = this.currentUsdValue;
    }

    let sortedKeys = Object.keys(snapshots).sort();

    return {
      datasets: [
        {
          data: sortedKeys.map(x => snapshots[x]),
          backgroundColor: 'rgba(0,0,0,0)',
          borderColor: '#1b68ff',
          pointBackgroundColor: '#1b68ff',
          pointBorderColor: '#fff',
          fill: 'origin'
        }
      ],
      labels: sortedKeys.map(x => this.formatLabel(x))
    };
  }

  private formatLabel(label: string) {
    let date = new Date(label);
    return formatDate(date, 'MM.dd HH:mm', this.locale);
  }
}


