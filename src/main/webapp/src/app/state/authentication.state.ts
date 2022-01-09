import { Action, Selector, State, StateContext } from '@ngxs/store';
import { Injectable } from "@angular/core";

export class SetAuthentication {
  static readonly type = '[Authentication] SetAuthentication';

  constructor(public payload: boolean) {
  }
}

export class AuthenticationStateModel {
  isAuthenticated = false;
}

@State<AuthenticationStateModel>({
  name: 'authenticationStateModel'
})
@Injectable()
export class AuthenticationState {

  @Selector()
  static isAuthenticated(state: AuthenticationStateModel) {
    return state.isAuthenticated;
  }

  @Action(SetAuthentication)
  setAuthentication(ctx: StateContext<AuthenticationStateModel>, action: SetAuthentication) {
    const state = ctx.getState();
    ctx.setState({
      ...state,
      isAuthenticated: action.payload
    });

    return state;
  }
}
