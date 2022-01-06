import { Component, OnInit } from '@angular/core';
import { UserCollectionsState } from "../state/user-collections.state";
import { Observable } from "rxjs";
import { UserCollectionSummary } from "../../generated/client";
import { Select, Store } from "@ngxs/store";
import { GetUserCollections } from "../state/user-collections.actions";

@Component({
  selector: 'app-user-collections',
  templateUrl: './user-collections.component.html',
  styleUrls: ['./user-collections.component.scss']
})
export class UserCollectionsComponent implements OnInit {

  // @ts-ignore
  @Select(UserCollectionsState) userCollections$: Observable<UserCollectionSummary[]>;

  constructor(private store: Store) {
  }

  ngOnInit(): void {
    this.store.dispatch(new GetUserCollections());
  }

}
