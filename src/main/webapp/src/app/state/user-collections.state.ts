import { Action, createSelector, Selector, State, StateContext } from '@ngxs/store';
import { HistoricalAddressBalance, UserCollectionSummary } from "../../generated/client";
import { UserCollectionService } from "../services/user-collection.service";
import { Injectable } from "@angular/core";
import { append, patch, removeItem } from "@ngxs/store/operators";

export class GetHistoricalBalance {
  static readonly type = '[UserCollections] GetHistoricalBalance';

  constructor(public collectionId: number) {
  }
}

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

export interface UserCollectionSummaryModel {
  userCollections: UserCollectionSummary[];
  historicalBalances: { collectionId: number, balance: HistoricalAddressBalance }[];
}

@State<UserCollectionSummaryModel>({
  name: 'userCollectionSummaryModel',
  defaults: {
    userCollections: [],
    historicalBalances: []
  }
})
@Injectable()
export class UserCollectionsState {

  constructor(private userCollectionService: UserCollectionService) {
  }

  static getHistoricalAddressBalance(collectionId: number) {
    return createSelector([UserCollectionsState], (state: UserCollectionSummaryModel) => {
      return state.historicalBalances.find(entry => entry.collectionId === collectionId)?.balance;
    });
  }

  @Action(GetHistoricalBalance)
  getHistoricalBalance(ctx: StateContext<UserCollectionSummaryModel>, action: GetHistoricalBalance) {
    return this.userCollectionService.getHistoricalAddressBalance(action.collectionId).then(result => {
      ctx.setState(
        patch({
          historicalBalances: removeItem<{ collectionId: number, balance: HistoricalAddressBalance }>(entry => entry?.collectionId === action.collectionId)
        })
      );
      ctx.setState(
        patch({
          historicalBalances: append([{
            collectionId: action.collectionId,
            balance: result
          }])
        })
      );
    });
  }

  @Selector()
  static getUserCollections(state: UserCollectionSummaryModel) {
    return state.userCollections;
  }

  @Action(GetUserCollections)
  getUserCollections(ctx: StateContext<UserCollectionSummaryModel>) {
    return this.userCollectionService.getUserCollections().then(((result) => {
      const state = ctx.getState();
      ctx.setState({
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
  addAddressToCollection(ctx: StateContext<UserCollectionSummaryModel>, action: AddAddressToCollection
  ) {
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
