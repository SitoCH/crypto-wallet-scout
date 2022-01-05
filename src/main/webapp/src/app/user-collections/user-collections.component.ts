import { Component, OnInit } from '@angular/core';
import { AddressBalanceService } from "../services/address-balance.service";
import { UserCollectionService } from "../services/user-collection.service";

@Component({
  selector: 'app-user-collections',
  templateUrl: './user-collections.component.html',
  styleUrls: ['./user-collections.component.scss']
})
export class UserCollectionsComponent implements OnInit {

  constructor(private userCollectionService: UserCollectionService) {
  }

  ngOnInit(): void {
    //this.userCollectionService.getUserCollections().then()
  }

}
