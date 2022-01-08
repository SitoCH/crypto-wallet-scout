import { Action, Selector, State, StateContext } from '@ngxs/store';
import { UserCollectionSummary } from "../../generated/client";
import { UserCollectionService } from "../services/user-collection.service";
import { AddUserCollection, GetUserCollections } from "./user-collections.actions";
import { Injectable } from "@angular/core";

export class UserCollectionSummaryModel {
  userCollections: UserCollectionSummary[] = [];
}

@State<UserCollectionSummaryModel>({
  name: 'userCollectionSummaryModel'
})
@Injectable()
export class UserCollectionsState {

  constructor(private userCollectionService: UserCollectionService) {
  }

  @Selector()
  static getUserCollections(state: UserCollectionSummaryModel) {
    return state.userCollections;
  }

  @Action(GetUserCollections)
  getUserCollections({getState, setState}: StateContext<UserCollectionSummaryModel>) {
    return this.userCollectionService.getUserCollections().then(((result) => {
      const state = getState();
      setState({
        ...state,
        userCollections: result,
      });
    }));
  }

  @Action(AddUserCollection)
  addUserCollection(ctx: StateContext<UserCollectionSummaryModel>, action: AddUserCollection) {
    return this.userCollectionService.newUserCollection({name: action.payload}).then(
      newCollection => {
        const state = ctx.getState();
        ctx.setState({
          ...state,
          userCollections: [...state.userCollections, newCollection]
        });
      });
  }
}
