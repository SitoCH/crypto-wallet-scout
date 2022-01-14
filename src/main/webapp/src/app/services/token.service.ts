import { Injectable } from '@angular/core';
import { TokenResourceClient } from "../../generated/client";
import { InjectableRestApplicationClient } from "./utils/injectable-rest-application-client.service";

@Injectable({
  providedIn: 'root'
})
export class TokenService extends TokenResourceClient {
  constructor(httpClient: InjectableRestApplicationClient) {
    super(httpClient);
  }
}
