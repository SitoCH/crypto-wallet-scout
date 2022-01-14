import { Action, createSelector, State, StateContext } from "@ngxs/store";
import { Injectable } from "@angular/core";
import { TokenService } from "../services/token.service";
import { TokenResult } from "../../generated/client";

export interface TokenStateModel {
  tokens: Map<string, TokenResult>;
}

export class GetTokenById {
  static readonly type = '[Token] GetTokenById';

  constructor(public id: string) {
  }
}

@State<TokenStateModel>({
  name: 'tokenState',
  defaults: {
    tokens: new Map<string, TokenResult>()
  }
})
@Injectable()
export class TokenState {

  static getTokenById(id: string) {
    return createSelector([TokenState], (state: TokenStateModel) => state.tokens.get(id));
  }

  constructor(private tokenService: TokenService) {
  }

  @Action(GetTokenById)
  getTokenById(ctx: StateContext<TokenStateModel>, action: GetTokenById) {
    const id = action.id;

    if (ctx.getState().tokens.has(id)) {
      return;
    }

    return this.tokenService.getToken(id)
      .then(token => {
          const state = ctx.getState();
          state.tokens.set(id, token);
          ctx.setState(state);
        }
      );
  }
}
