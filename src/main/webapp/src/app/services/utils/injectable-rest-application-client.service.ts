import { Injectable } from "@angular/core";
import { HttpClient as TypeScriptHttpClient, RestResponse } from "../../../generated/client";
import { HttpClient } from "@angular/common/http";
import { firstValueFrom } from "rxjs";

@Injectable()
export class InjectableRestApplicationClient implements TypeScriptHttpClient {
  constructor(private httpClient: HttpClient) {

  }

  request<R>(requestConfig: { method: string; url: string; queryParams?: any; data?: any; copyFn?: (data: R) => R }): RestResponse<R> {
    return firstValueFrom(this.httpClient.request<R>(requestConfig.method, requestConfig.url, {
      body: requestConfig.data,
      params: requestConfig.queryParams
    }));
  }
}
