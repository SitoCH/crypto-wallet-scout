import { Action, Selector, State, StateContext } from '@ngxs/store';
import { UserCollectionSummary } from "../../generated/client";
import { UserCollectionService } from "../services/user-collection.service";
import { Injectable } from "@angular/core";

export class GetUserCollections {
  static readonly type = '[UserCollections] Get';
}

export class AddUserCollection {
  static readonly type = '[UserCollections] Add';

  constructor(public payload: string) {
  }
}

export class AddAddressToCollection {
  static readonly type = '[UserCollections] AddAddressToCollection';

  constructor(public collectionId: number, public address: string) {
  }
}

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

  @Action(AddAddressToCollection)
  addAddressToCollection(ctx: StateContext<UserCollectionSummaryModel>, action: AddAddressToCollection) {
    return this.userCollectionService.addAddress(action.collectionId, action.address).then(
      () => {
        return this.userCollectionService.getUserCollections()
      }).then(collections => {
      const state = ctx.getState();
      ctx.setState({
        ...state,
        userCollections: collections
      });
    });
  }
}
