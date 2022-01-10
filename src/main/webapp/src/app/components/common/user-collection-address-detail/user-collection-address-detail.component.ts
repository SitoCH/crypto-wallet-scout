import { Component } from '@angular/core';
import { AddressBalanceService } from "../../../services/address-balance.service";
import { AddressBalance } from "../../../../generated/client";
import { ActivatedRoute } from "@angular/router";

@Component({
  selector: 'app-user-collection-address-detail',
  templateUrl: './user-collection-address-detail.component.html',
  styleUrls: ['./user-collection-address-detail.component.scss']
})
export class UserCollectionAddressDetailComponent {

  id!: number;
  address!: string;

  addressBalance: AddressBalance | null = null;

  constructor(private addressService: AddressBalanceService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route.params
      .subscribe((params: any) => {
          this.address = params.address;
          this.loadBalance();
        }
      );
  }

  private loadBalance() {
    this.addressBalance = null;
    this.addressService.getAddressBalance(this.address)
      .then(data => {
        this.addressBalance = data;
      })
  }
}
