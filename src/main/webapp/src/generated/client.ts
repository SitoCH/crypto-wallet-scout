/* tslint:disable */
/* eslint-disable */
// Generated using typescript-generator version 2.34.976 on 2022-01-13 00:18:18.

export interface AddressBalance {
    tokenBalances: TokenBalance[];
}

export interface Configuration {
    authServerUrl: string;
    clientId: string;
}

export interface NewUserCollection {
    name: string;
}

export interface UserCollectionSummary {
    id: number;
    name: string;
    addresses: string[];
}

export interface TokenBalance {
    network: Network;
    allocation: Allocation;
    nativeValue: number;
    usdValue: number;
    symbol: string;
    name: string;
    image: string;
}

export interface HttpClient {

    request<R>(requestConfig: { method: string; url: string; queryParams?: any; data?: any; copyFn?: (data: R) => R; }): RestResponse<R>;
}

export class AddressBalanceResourceClient {

    constructor(protected httpClient: HttpClient) {
    }

    /**
     * HTTP GET /api/address/balance/{address}
     * Java method: ch.grignola.web.AddressBalanceResource.getAddressBalance
     */
    getAddressBalance(address: string): RestResponse<AddressBalance> {
        return this.httpClient.request({ method: "GET", url: uriEncoding`api/address/balance/${address}` });
    }
}

export class UserCollectionResourceClient {

    constructor(protected httpClient: HttpClient) {
    }

    /**
     * HTTP GET /api/collection
     * Java method: ch.grignola.web.UserCollectionResource.getUserCollections
     */
    getUserCollections(): RestResponse<UserCollectionSummary[]> {
        return this.httpClient.request({ method: "GET", url: uriEncoding`api/collection` });
    }

    /**
     * HTTP POST /api/collection
     * Java method: ch.grignola.web.UserCollectionResource.newUserCollection
     */
    newUserCollection(newUserCollection: NewUserCollection): RestResponse<UserCollectionSummary> {
        return this.httpClient.request({ method: "POST", url: uriEncoding`api/collection`, data: newUserCollection });
    }

    /**
     * HTTP POST /api/collection/{collectionId}/add/{address}
     * Java method: ch.grignola.web.UserCollectionResource.addAddress
     */
    addAddress(collectionId: number, address: string): RestResponse<void> {
        return this.httpClient.request({ method: "POST", url: uriEncoding`api/collection/${collectionId}/add/${address}` });
    }
}

export class AsynchronousDispatcherClient {

    constructor(protected httpClient: HttpClient) {
    }

    /**
     * HTTP GET /{job-id}
     * Java method: org.jboss.resteasy.core.AsynchronousDispatcher.get
     */
    get(jobId: string, queryParams?: { wait?: number; }): RestResponse<any> {
        return this.httpClient.request({ method: "GET", url: uriEncoding`${jobId}`, queryParams: queryParams });
    }

    /**
     * HTTP POST /{job-id}
     * Java method: org.jboss.resteasy.core.AsynchronousDispatcher.readAndRemove
     */
    readAndRemove(jobId: string, queryParams?: { wait?: number; }): RestResponse<any> {
        return this.httpClient.request({ method: "POST", url: uriEncoding`${jobId}`, queryParams: queryParams });
    }

    /**
     * HTTP DELETE /{job-id}
     * Java method: org.jboss.resteasy.core.AsynchronousDispatcher.remove
     */
    remove(jobId: string): RestResponse<void> {
        return this.httpClient.request({ method: "DELETE", url: uriEncoding`${jobId}` });
    }
}

export class ConfigurationResourceClient {

    constructor(protected httpClient: HttpClient) {
    }

    /**
     * HTTP GET /public/api/configuration
     * Java method: ch.grignola.web.ConfigurationResource.getConfiguration
     */
    getConfiguration(): RestResponse<Configuration> {
        return this.httpClient.request({ method: "GET", url: uriEncoding`public/api/configuration` });
    }
}

export type RestResponse<R> = Promise<R>;

export const enum Network {
    POLYGON = "POLYGON",
    AVALANCHE = "AVALANCHE",
    TERRA = "TERRA",
    CRO = "CRO",
    SOLANA = "SOLANA",
}

export const enum Allocation {
    LIQUID = "LIQUID",
    STACKED = "STACKED",
    UNCLAIMED_REWARDS = "UNCLAIMED_REWARDS",
}

function uriEncoding(template: TemplateStringsArray, ...substitutions: any[]): string {
    let result = "";
    for (let i = 0; i < substitutions.length; i++) {
        result += template[i];
        result += encodeURIComponent(substitutions[i]);
    }
    result += template[template.length - 1];
    return result;
}
