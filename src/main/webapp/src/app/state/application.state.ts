import { Action, Selector, State, StateContext } from '@ngxs/store';
import { Injectable } from "@angular/core";

export class ToggleSidebar {
  static readonly type = '[Application] ToggleSidebar';
}

export class ApplicationStateModel {
  isSidebarClosed = false;
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
}
