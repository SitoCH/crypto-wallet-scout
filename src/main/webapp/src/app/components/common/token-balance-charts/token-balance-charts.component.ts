import { Component, Inject, Input, LOCALE_ID, OnChanges, SimpleChanges } from '@angular/core';
import { TokenList } from "../../../utils/balance";
import { ChartConfiguration } from "chart.js";
import { Store } from "@ngxs/store";
import { TokenState } from "../../../state/token.state";
import { from, mergeMap, toArray } from "rxjs";
import { formatNumber } from "@angular/common";
import { Allocation } from "../../../../generated/client";

@Component({
  selector: 'app-token-balance-charts',
  templateUrl: './token-balance-charts.component.html',
  styleUrls: ['./token-balance-charts.component.scss']
})
export class TokenBalanceChartsComponent implements OnChanges {

  @Input()
  tokens!: TokenList;

  chartByTokenData?: ChartConfiguration['data'];

  chartByAllocationData?: ChartConfiguration['data'];

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
      let tokensByAllocation = new Map<Allocation, number>();

      this.tokens.tokenBalances.forEach(tokenBalance => {
        let currentBalance = tokensByTokenId.get(tokenBalance.tokenId) || 0;
        tokensByTokenId.set(tokenBalance.tokenId, currentBalance + tokenBalance.usdValue);

        let currentAllocationBalance = tokensByAllocation.get(tokenBalance.allocation) || 0;
        tokensByAllocation.set(tokenBalance.allocation, currentAllocationBalance + tokenBalance.usdValue);
      });

      this.generateChartByTokens(tokensByTokenId);

      this.generateChartByAllocation(tokensByAllocation);

    }
  }

  private generateChartByAllocation(tokensByAllocation: Map<Allocation, number>) {

    let sortedKeys = Array.from(tokensByAllocation.keys());

    this.chartByAllocationData = {
      datasets: [
        {
          data: sortedKeys.map(x => tokensByAllocation.get(x) || 0),
          borderColor: '#343a40',
          borderWidth: 1
        }
      ],
      labels: sortedKeys
    };
  }

  private generateChartByTokens(tokensByTokenId: Map<string, number>) {

    let sortedKeys = Array.from(tokensByTokenId.keys());

    from(sortedKeys)
      .pipe(
        mergeMap((tokenId) => this.store.selectOnce(TokenState.getTokenById(tokenId))),
        toArray()
      ).subscribe(tokens => {
      this.chartByTokenData = {
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
