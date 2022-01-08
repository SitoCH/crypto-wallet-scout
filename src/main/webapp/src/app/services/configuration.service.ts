import { Injectable } from '@angular/core';
import { ConfigurationResourceClient } from "../../generated/client";
import { InjectableRestApplicationClient } from "./utils/injectable-rest-application-client.service";

@Injectable({
  providedIn: 'root'
})
export class ConfigurationService extends ConfigurationResourceClient {

  constructor(httpClient: InjectableRestApplicationClient) {
    super(httpClient)
  }
}
