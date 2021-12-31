import { Component } from '@angular/core';
import { AddressService } from "../services/address.service";

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent {

  constructor(private addressService: AddressService) {
  }

  test() {
    this.addressService.getTestAccount()
      .subscribe(data => console.log(data))
  }
}
