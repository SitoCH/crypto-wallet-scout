import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class AddressService {

  constructor(private http: HttpClient) {
  }

  getAccountBalance(address: string) {
    return this.http.get('/api/address/balance/' + address);
  }
}
