import { Component } from '@angular/core';
import { Observable } from "rxjs";
import { Select, Store } from "@ngxs/store";
import { UserCollectionSummary } from "../../../../generated/client";
import { AddUserCollection, UserCollectionsState } from "../../../state/user-collections.state";

@Component({
  selector: 'app-user-collections',
  templateUrl: './user-collections.component.html',
  styleUrls: ['./user-collections.component.scss']
})
export class UserCollectionsComponent {

  newCollectionName = '';
  @Select(UserCollectionsState.getUserCollections)
  userCollections$!: Observable<UserCollectionSummary[]>;

  constructor(private store: Store) {
  }

  addNewCollection(newCollectionName: string) {
    this.store.dispatch(new AddUserCollection(newCollectionName));
  }
}
