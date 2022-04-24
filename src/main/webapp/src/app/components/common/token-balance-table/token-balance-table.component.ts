import { Component, Input } from '@angular/core';
import { TokenBalance, TokenResult } from "../../../../generated/client";
import { Store } from "@ngxs/store";
import { mergeMap, Observable } from "rxjs";
import { GetTokenById, TokenState } from "../../../state/token.state";

@Component({
  selector: 'app-token-balance-table',
  templateUrl: './token-balance-table.component.html',
  styleUrls: ['./token-balance-table.component.scss']
})
export class TokenBalanceTableComponent {

  @Input()
  tokens!: TokenBalance[];
  @Input()
  hideNetwork = false;
  @Input()
  showGroupingToggle = false;

  @Input()
  title!: string;

  constructor(private store: Store) {
  }

  getToken(tokenId: string): Observable<TokenResult | undefined> {
    return this.store
      .dispatch(new GetTokenById(tokenId))
      .pipe(mergeMap(() => this.store.select(TokenState.getTokenById(tokenId))))
  }
}
