import { Component, Inject, Input, LOCALE_ID, OnChanges, SimpleChanges } from '@angular/core';
import { ChartConfiguration } from "chart.js";
import { formatDate } from "@angular/common";
import { combineLatest, map, Observable } from "rxjs";
import {
  ApplicationState,
  HistoricalChartRange,
  SetHistoricalBalancesRange,
  ToggleIncludeLotsInHistoricalBalances
} from "../../../state/application.state";
import { Select, Store } from "@ngxs/store";

@Component({
  selector: 'app-historical-balance',
  templateUrl: './historical-balance.component.html',
  styleUrls: ['./historical-balance.component.scss']
})
export class HistoricalBalanceComponent implements OnChanges {

  HistoricalChartRange = HistoricalChartRange;

  @Input()
  balances$?: Observable<{ [index: string]: number }>;

  @Select(ApplicationState.getHistoricalBalancesRange)
  historicalChartRange$!: Observable<HistoricalChartRange>;

  lineChartData$?: Observable<ChartConfiguration['data']>;

  lineChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false,
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
        tension: 0.1
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
    if (changes['balances$'] && this.balances$) {
      this.lineChartData$ =
        combineLatest([this.balances$, this.historicalChartRange$]).pipe(
          map(([balances, range]) => this.getChartData(balances, range))
        );
    }
  }

  getChartData(lineChartData: { [index: string]: number } | undefined, range: HistoricalChartRange): ChartConfiguration['data'] {
    let daysToConsider = this.getDaysToConsider(range);
    let fromDate = new Date(new Date().getTime() - (daysToConsider * 24 * 60 * 60 * 1000));

    let sortedKeys = Object.keys(lineChartData || [])
      .filter(dateAsString => {
        return new Date(dateAsString) > fromDate;
      })
      .sort();

    if (!lineChartData || sortedKeys.length < 2) {
      return {
        datasets: []
      };
    }

    return {
      datasets: [
        {
          data: sortedKeys.map(x => lineChartData[x]),
          backgroundColor: 'rgba(0,0,0,0)',
          borderColor: '#1b68ff',
          pointBackgroundColor: '#1b68ff',
          pointRadius: 0,
          fill: 'origin'
        }
      ],
      labels: sortedKeys.map(x => this.formatLabel(daysToConsider, x))
    };
  }

  private getDaysToConsider(range: string) {
    if (range === HistoricalChartRange.THREE_DAYS) {
      return 3;
    }
    if (range === HistoricalChartRange.SEVEN_DAYS) {
      return 7;
    }
    if (range === HistoricalChartRange.FOURTEEN_DAYS) {
      return 14;
    }
    if (range === HistoricalChartRange.THIRTY_DAYS) {
      return 30;
    }
    if (range === HistoricalChartRange.NINETY_DAYS) {
      return 90;
    }
    return 3;
  }

  private formatLabel(daysToConsider: number, label: string) {
    let date = new Date(label);
    return formatDate(date, daysToConsider < 8 ? 'HH:mm' : 'MM.dd', this.locale);
  }

  onRangeChange(value: HistoricalChartRange) {
    this.store.dispatch(new SetHistoricalBalancesRange(value));
  }

  onIncludeLotsChange() {
    this.store.dispatch(new ToggleIncludeLotsInHistoricalBalances());
  }
}
