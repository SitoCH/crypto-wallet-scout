import { Action, createSelector, State, StateContext } from "@ngxs/store";
import { Injectable } from "@angular/core";
import { TokenService } from "../services/token.service";
import { TokenResult } from "../../generated/client";
import { from, tap } from "rxjs";

export interface TokenStateModel {
  tokens: Map<string, TokenResult>;
  loading: Set<string>;
}

export class GetTokenById {
  static readonly type = '[Token] GetTokenById';

  constructor(public id: string) {
  }
}

export class LoadTokenFromService {
  static readonly type = '[Token] LoadTokenFromService';

  constructor(public id: string) {
  }
}

@State<TokenStateModel>({
  name: 'tokenState',
  defaults: {
    tokens: new Map<string, TokenResult>(),
    loading: new Set<string>()
  }
})
@Injectable()
export class TokenState {

  static getTokenById(id: string) {
    return createSelector([TokenState], (state: TokenStateModel) => {
      return state.tokens.get(id);
    });
  }

  constructor(private tokenService: TokenService) {
  }

  @Action(LoadTokenFromService)
  loadTokenFromService(ctx: StateContext<TokenStateModel>, action: LoadTokenFromService) {
    const id = action.id;
    return from(this.tokenService.getToken(id)).pipe(
      tap(token => {
        const state = ctx.getState();
        state.loading.delete(id);
        state.tokens.set(id, token);
        ctx.setState(state);
      }));
  }

  @Action(GetTokenById)
  getTokenById(ctx: StateContext<TokenStateModel>, action: GetTokenById) {
    const id = action.id;

    if (ctx.getState().tokens.has(id) || ctx.getState().loading.has(id)) {
      return;
    }

    const state = ctx.getState();
    state.loading.add(id);
    ctx.setState(state);

    ctx.dispatch(new LoadTokenFromService(id));
  }
}
