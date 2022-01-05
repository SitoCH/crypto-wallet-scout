import { Injectable } from '@angular/core';
import { InjectableRestApplicationClient } from "./utils/injectable-rest-application-client.service";
import { UserCollectionResourceClient } from "../../generated/client";

@Injectable({
  providedIn: 'root'
})
export class UserCollectionService extends UserCollectionResourceClient {

  constructor(httpClient: InjectableRestApplicationClient) {
    super(httpClient)
  }
}
