import { Action, Selector, State, StateContext } from '@ngxs/store';
import { Injectable } from "@angular/core";

export enum HistoricalChartRange {
  THREE_DAYS = 'THREE_DAYS',
  SEVEN_DAYS = 'SEVEN_DAYS',
  FOURTEEN_DAYS = 'FOURTEEN_DAYS',
  THIRTY_DAYS = 'THIRTY_DAYS',
  NINETY_DAYS = 'NINETY_DAYS',
}

export class ToggleSidebar {
  static readonly type = '[Application] ToggleSidebar';
}

export class ToggleGroupTokenTable {
  static readonly type = '[Application] ToggleGroupTokenTable';
}

export class SetHistoricalBalancesRange {
  static readonly type = '[Application] SetHistoricalBalancesRange';

  constructor(public payload: HistoricalChartRange) {
  }
}

export class ToggleIncludeLotsInHistoricalBalances {
  static readonly type = '[Application] ToggleIncludeLotsInHistoricalBalances';
}

export class ApplicationStateModel {
  isSidebarClosed!: boolean;
  groupTokenTable!: boolean;
  includeLotsInHistoricalBalances!: boolean;
  historicalBalancesRange!: HistoricalChartRange;
}

@State<ApplicationStateModel>({
  name: 'applicationStateModel',
  defaults: {
    isSidebarClosed: true,
    groupTokenTable: false,
    includeLotsInHistoricalBalances: false,
    historicalBalancesRange: HistoricalChartRange.SEVEN_DAYS
  }
})
@Injectable()
export class ApplicationState {

  @Selector()
  static getHistoricalBalancesRange(state: ApplicationStateModel) {
    return state.historicalBalancesRange;
  }

  @Action(SetHistoricalBalancesRange)
  setHistoricalBalancesRange(ctx: StateContext<ApplicationStateModel>, action: SetHistoricalBalancesRange) {
    const state = ctx.getState();
    ctx.setState({
      ...state,
      historicalBalancesRange: action.payload
    });

    return state;
  }

  @Selector()
  static isSidebarClosed(state: ApplicationStateModel) {
    return state.isSidebarClosed;
  }

  @Action(ToggleSidebar)
  toggleSidebar(ctx: StateContext<ApplicationStateModel>) {
    const state = ctx.getState();
    ctx.setState({
      ...state,
      isSidebarClosed: !state.isSidebarClosed
    });

    return state;
  }

  @Selector()
  static isGroupTokenTable(state: ApplicationStateModel) {
    return state.groupTokenTable;
  }

  @Action(ToggleGroupTokenTable)
  toggleGroupTokenTable(ctx: StateContext<ApplicationStateModel>) {
    const state = ctx.getState();
    ctx.setState({
      ...state,
      groupTokenTable: !state.groupTokenTable
    });

    return state;
  }

  @Selector()
  static includeLotsInHistoricalBalances(state: ApplicationStateModel) {
    return state.includeLotsInHistoricalBalances;
  }

  @Action(ToggleIncludeLotsInHistoricalBalances)
  toggle(ctx: StateContext<ApplicationStateModel>) {
    const state = ctx.getState();
    ctx.setState({
      ...state,
      includeLotsInHistoricalBalances: !state.includeLotsInHistoricalBalances
    });

    return state;
  }
}
