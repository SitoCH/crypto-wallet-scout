import { Component, Input } from '@angular/core';
import { AddressBalance, TokenResult } from "../../../../generated/client";
import { Store } from "@ngxs/store";
import { mergeMap, Observable } from "rxjs";
import { AddressBalanceService } from "../../../services/address-balance.service";
import { GetTokenById, TokenState } from "../../../state/token.state";

@Component({
  selector: 'app-address-balance-table',
  templateUrl: './address-balance-table.component.html',
  styleUrls: ['./address-balance-table.component.scss']
})
export class AddressBalanceTableComponent {

  @Input()
  address!: string;

  @Input()
  addressBalance!: AddressBalance;

  constructor(private addressService: AddressBalanceService,
              private store: Store) {
  }

  getToken(tokenId: string): Observable<TokenResult | undefined>  {
    return this.store
      .dispatch(new GetTokenById(tokenId))
      .pipe(mergeMap(() => this.store.select(TokenState.getTokenById(tokenId))))
  }
}
