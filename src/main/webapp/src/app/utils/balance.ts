import { AddressBalance, TokenBalance } from "../../generated/client";

export interface LocalTokenList {
  tokenBalances: TokenBalance[];
}

export type TokenList = LocalTokenList | AddressBalance;
