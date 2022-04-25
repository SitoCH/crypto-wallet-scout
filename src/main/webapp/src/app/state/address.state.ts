import { Action, createSelector, State, StateContext } from '@ngxs/store';
import { HistoricalAddressBalance } from "../../generated/client";
import { Injectable } from "@angular/core";
import { append, patch, removeItem } from "@ngxs/store/operators";
import { AddressBalanceService } from "../services/address-balance.service";

export class GetHistoricalBalance {
  static readonly type = '[Address] GetHistoricalBalance';

  constructor(public address: string, public includeLots: boolean) {
  }
}

export interface AddressSummaryModel {
  historicalBalances: { address: string, balance: HistoricalAddressBalance, lotsIncluded: boolean }[];
}

@State<AddressSummaryModel>({
  name: 'addressSummaryModel',
  defaults: {
    historicalBalances: []
  }
})
@Injectable()
export class AddressState {

  constructor(private addressBalanceService: AddressBalanceService) {
  }

  static getHistoricalAddressBalance(address: string) {
    return createSelector([AddressState], (state: AddressSummaryModel) => {
      return state.historicalBalances.find(entry => entry.address === address);
    });
  }

  @Action(GetHistoricalBalance)
  getHistoricalBalance(ctx: StateContext<AddressSummaryModel>, action: GetHistoricalBalance) {
    return (action.includeLots ? this.addressBalanceService.getHistoricalAddressBalanceWithLots(action.address) : this.addressBalanceService.getHistoricalAddressBalance(action.address))
      .then(result => {
        ctx.setState(
          patch({
            historicalBalances: removeItem<{ address: string, balance: HistoricalAddressBalance }>(entry => entry?.address === action.address)
          })
        );
        ctx.setState(
          patch({
            historicalBalances: append([{
              address: action.address,
              balance: result,
              lotsIncluded: action.includeLots
            }])
          })
        );
      });
  }

}
