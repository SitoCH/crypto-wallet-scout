import { Injectable } from '@angular/core';
import { AddressBalanceResourceClient } from "../../generated/client";
import { InjectableRestApplicationClient } from "./utils/injectable-rest-application-client.service";

@Injectable()
export class AddressBalanceService extends AddressBalanceResourceClient {
  constructor(httpClient: InjectableRestApplicationClient) {
    super(httpClient)
  }
}
