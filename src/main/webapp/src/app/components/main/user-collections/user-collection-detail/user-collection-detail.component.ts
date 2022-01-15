import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { UserCollectionService } from "../../../../services/user-collection.service";
import { AddressBalance } from "../../../../../generated/client";

@Component({
  selector: 'app-user-collection-detail',
  templateUrl: './user-collection-detail.component.html',
  styleUrls: ['./user-collection-detail.component.scss']
})
export class UserCollectionDetailComponent implements OnInit {

  id!: number;
  addressBalances: AddressBalance[] | null = null;
  collectionUsdValue: number | null = null;

  constructor(private userCollectionService: UserCollectionService,
              private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route.params
      .subscribe((params: any) => {
          this.id = params.id;
          this.loadBalance();
        }
      );
  }

  private loadBalance() {
    this.addressBalances = null;
    this.collectionUsdValue = null;
    this.userCollectionService.getAddressBalance(this.id)
      .then(data => {
        this.addressBalances = data;
        let collectionUsdValue = 0;
        data.forEach(addressBalance => {
          addressBalance.tokenBalances.forEach(tokenBalance => {
            collectionUsdValue += tokenBalance.usdValue;
          });
        });
        this.collectionUsdValue = collectionUsdValue;
      })
  }

}
