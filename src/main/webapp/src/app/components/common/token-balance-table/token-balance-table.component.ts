import { Component, Input } from '@angular/core';
import { TokenResult } from "../../../../generated/client";
import { Store } from "@ngxs/store";
import { mergeMap, Observable } from "rxjs";
import { AddressBalanceService } from "../../../services/address-balance.service";
import { GetTokenById, TokenState } from "../../../state/token.state";
import { TokenList } from "../../../utils/balance";

@Component({
  selector: 'app-token-balance-table',
  templateUrl: './token-balance-table.component.html',
  styleUrls: ['./token-balance-table.component.scss']
})
export class TokenBalanceTableComponent {

  @Input()
  tokens!: TokenList;

  @Input()
  title!: string;

  constructor(private addressService: AddressBalanceService,
              private store: Store) {
  }

  getToken(tokenId: string): Observable<TokenResult | undefined> {
    return this.store
      .dispatch(new GetTokenById(tokenId))
      .pipe(mergeMap(() => this.store.select(TokenState.getTokenById(tokenId))))
  }
}
