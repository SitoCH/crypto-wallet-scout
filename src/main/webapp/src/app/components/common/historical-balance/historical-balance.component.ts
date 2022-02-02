import { Component, Inject, Input, LOCALE_ID, OnChanges, SimpleChanges } from '@angular/core';
import { ChartConfiguration } from "chart.js";
import { formatDate } from "@angular/common";
import { BehaviorSubject, combineLatest, map, Observable } from "rxjs";

enum HistoricalChartRange {
  THREE_DAYS = 'THREE_DAYS', SEVEN_DAYS = 'SEVEN_DAYS'
}

@Component({
  selector: 'app-historical-balance',
  templateUrl: './historical-balance.component.html',
  styleUrls: ['./historical-balance.component.scss']
})
export class HistoricalBalanceComponent implements OnChanges {

  HistoricalChartRange = HistoricalChartRange;

  @Input()
  balances$?: Observable<{ [index: string]: number }>;

  historicalChartRange$ = new BehaviorSubject(HistoricalChartRange.THREE_DAYS);

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

  constructor(@Inject(LOCALE_ID) private locale: string) {
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['balances$'] && this.balances$) {
      this.lineChartData$ =
        combineLatest([this.balances$, this.historicalChartRange$]).pipe(
          map(([balances, range]) => this.getChartData(balances, range))
        );
    }
  }

  getChartData(lineChartData: { [index: string]: number } | undefined, range: string): ChartConfiguration['data'] {
    let daysToConsider = range === HistoricalChartRange.THREE_DAYS ? 3 : 7;
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
      labels: sortedKeys.map(x => this.formatLabel(x))
    };
  }

  private formatLabel(label: string) {
    let date = new Date(label);
    return formatDate(date, 'HH:mm', this.locale);
  }

  onRangeChange(value: HistoricalChartRange) {
    this.historicalChartRange$.next(value);
  }
}
