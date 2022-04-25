import { Action, Selector, State, StateContext } from '@ngxs/store';
import { Injectable } from "@angular/core";

export class ToggleSidebar {
  static readonly type = '[Application] ToggleSidebar';
}

export class ToggleGroupTokenTable {
  static readonly type = '[Application] ToggleGroupTokenTable';
}

export class ToggleIncludeLotsInHistoricalBalances {
  static readonly type = '[Application] ToggleIncludeLotsInHistoricalBalances';
}

export class ApplicationStateModel {
  isSidebarClosed = false;
  groupTokenTable = false;
  includeLotsInHistoricalBalances = false;
}

@State<ApplicationStateModel>({
  name: 'applicationStateModel'
})
@Injectable()
export class ApplicationState {

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
