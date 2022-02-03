import { Component, Inject, Input, LOCALE_ID, OnChanges, SimpleChanges } from '@angular/core';
import { TokenList } from "../../../utils/balance";
import { ChartConfiguration } from "chart.js";
import { Store } from "@ngxs/store";
import { TokenState } from "../../../state/token.state";
import { from, mergeMap, toArray } from "rxjs";
import { formatNumber } from "@angular/common";

@Component({
  selector: 'app-token-balance-charts',
  templateUrl: './token-balance-charts.component.html',
  styleUrls: ['./token-balance-charts.component.scss']
})
export class TokenBalanceChartsComponent implements OnChanges {

  @Input()
  tokens!: TokenList;

  pieChartData?: ChartConfiguration['data'];

  pieChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    elements: {},
    plugins: {
      legend: {display: false},
      tooltip: {
        callbacks: {
          label: (tooltipItem) => {
            return tooltipItem.label + ': $' + formatNumber(<number>tooltipItem.raw, this.locale, '1.2-2');
          }
        }
      }
    }
  };

  constructor(@Inject(LOCALE_ID) private locale: string,
              private store: Store) {

  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['tokens'].currentValue) {
      let tokensByTokenId = new Map<string, number>();
      this.tokens.tokenBalances.forEach(tokenBalance => {
        let currentBalance = tokensByTokenId.get(tokenBalance.tokenId) || 0;
        tokensByTokenId.set(tokenBalance.tokenId, currentBalance + tokenBalance.usdValue);
      });

      let sortedKeys = Array.from(tokensByTokenId.keys());

      from(sortedKeys)
        .pipe(
          mergeMap((tokenId) => this.store.selectOnce(TokenState.getTokenById(tokenId))),
          toArray()
        ).subscribe(tokens => {
        this.pieChartData = {
          datasets: [
            {
              data: sortedKeys.map(x => tokensByTokenId.get(x) || 0),
              borderColor: '#343a40',
              borderWidth: 1
            }
          ],
          labels: sortedKeys.map((x, index) => tokens[index]!.name)
        };
      });

    }
  }

}
