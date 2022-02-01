import { Component, Inject, Input, LOCALE_ID, OnChanges, SimpleChanges } from '@angular/core';
import { ChartConfiguration } from "chart.js";
import { formatDate } from "@angular/common";
import { map, Observable } from "rxjs";

@Component({
  selector: 'app-historical-balance',
  templateUrl: './historical-balance.component.html',
  styleUrls: ['./historical-balance.component.scss']
})
export class HistoricalBalanceComponent implements OnChanges {

  @Input()
  balances$?: Observable<{ [index: string]: number }>;

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
        tension: 0.2
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
      this.lineChartData$ = this.balances$.pipe(
        map(result => this.getChartData(result))
      );
    }
  }

  getChartData(lineChartData: { [index: string]: number } | undefined): ChartConfiguration['data'] {

    let sortedKeys = Object.keys(lineChartData || []).sort();

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
          pointBorderColor: '#fff',
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

}
