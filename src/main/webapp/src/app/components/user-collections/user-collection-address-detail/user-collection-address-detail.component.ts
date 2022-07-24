import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { TokenBalance } from "../../../../generated/client";
import { AddressBalanceService } from "../../../services/address-balance.service";
import { HttpErrorResponse } from "@angular/common/http";

@Component({
  selector: 'app-user-collection-address-detail',
  templateUrl: './user-collection-address-detail.component.html',
  styleUrls: ['./user-collection-address-detail.component.scss']
})
export class UserCollectionAddressDetailComponent implements OnInit {

  id!: number;
  address!: string;

  addressBalance: TokenBalance[] | null = null;
  currentUsdValue: number | undefined = undefined;
  error = null;

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
    this.error = null;
    this.addressBalance = null;
    this.addressService.getAddressBalance(this.address)
      .then(tokenBalances => {
          this.addressBalance = tokenBalances;
          this.currentUsdValue = tokenBalances
            .map(x => x.usdValue)
            .reduce((a, b) => a + b);
        },
        (response: HttpErrorResponse) => {
          this.error = response.error.details;
        });
  }
}
