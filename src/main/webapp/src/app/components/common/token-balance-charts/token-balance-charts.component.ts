import { Component, Inject, Input, LOCALE_ID, OnChanges, SimpleChanges } from '@angular/core';
import { ChartConfiguration } from "chart.js";
import { Store } from "@ngxs/store";
import { TokenState } from "../../../state/token.state";
import { from, mergeMap, toArray } from "rxjs";
import { formatNumber } from "@angular/common";
import { Allocation, Network, TokenBalance } from "../../../../generated/client";

@Component({
  selector: 'app-token-balance-charts',
  templateUrl: './token-balance-charts.component.html',
  styleUrls: ['./token-balance-charts.component.scss']
})
export class TokenBalanceChartsComponent implements OnChanges {

  @Input()
  tokens!: TokenBalance[];

  chartByTokenData?: ChartConfiguration['data'];

  chartByAllocationData?: ChartConfiguration['data'];

  chartByNetworkData?: ChartConfiguration['data'];

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

  colors = ['rgb(110, 64, 170)', 'rgb(96, 84, 200)', 'rgb(76, 110, 219)', 'rgb(54, 140, 225)', 'rgb(35, 171, 216)', 'rgb(26, 199, 194)', 'rgb(29, 223, 163)', 'rgb(48, 239, 130)', 'rgb(82, 246, 103)', 'rgb(127, 246, 88)', 'rgb(175, 240, 91)', 'rgb(198, 214, 60)', 'rgb(226, 183, 47)', 'rgb(251, 150, 51)', 'rgb(255, 120, 71)', 'rgb(255, 94, 99)', 'rgb(254, 75, 131)', 'rgb(228, 65, 157)', 'rgb(191, 60, 175)', 'rgb(150, 61, 179)'];

  constructor(@Inject(LOCALE_ID) private locale: string,
              private store: Store) {
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['tokens'].currentValue) {
      let tokensByTokenId = new Map<string, number>();
      let tokensByAllocation = new Map<Allocation, number>();
      let tokensByNetwork = new Map<Network, number>();

      this.tokens.forEach(tokenBalance => {
        let currentBalance = tokensByTokenId.get(tokenBalance.parentTokenId || tokenBalance.tokenId) || 0;
        tokensByTokenId.set(tokenBalance.parentTokenId || tokenBalance.tokenId, currentBalance + tokenBalance.usdValue);

        let currentAllocationBalance = tokensByAllocation.get(tokenBalance.allocation) || 0;
        tokensByAllocation.set(tokenBalance.allocation, currentAllocationBalance + tokenBalance.usdValue);

        let currentNetworkBalance = tokensByNetwork.get(tokenBalance.network) || 0;
        tokensByNetwork.set(tokenBalance.network, currentNetworkBalance + tokenBalance.usdValue);
      });

      this.generateChartByTokens(tokensByTokenId);

      this.generateChartByAllocation(tokensByAllocation);

      this.generateChartByNetwork(tokensByNetwork);

    }
  }

  private generateChartByNetwork(tokensByNetwork: Map<Network, number>) {

    let sortedKeys = Array.from(tokensByNetwork.keys());

    this.chartByNetworkData = {
      datasets: [
        {
          data: sortedKeys.map(x => tokensByNetwork.get(x) || 0),
          borderColor: '#343a40',
          backgroundColor: this.colors,
          borderWidth: 1
        }
      ],
      labels: sortedKeys
    };
  }

  private generateChartByAllocation(tokensByAllocation: Map<Allocation, number>) {

    let sortedKeys = Array.from(tokensByAllocation.keys());

    this.chartByAllocationData = {
      datasets: [
        {
          data: sortedKeys.map(x => tokensByAllocation.get(x) || 0),
          borderColor: '#343a40',
          backgroundColor: this.colors,
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
            backgroundColor: this.colors,
            borderWidth: 1
          }
        ],
        labels: sortedKeys.map((x, index) => tokens[index]!.name)
      };
    });
  }
}
