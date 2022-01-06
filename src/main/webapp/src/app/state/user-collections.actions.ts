export class GetUserCollections {
  static readonly type = '[UserCollections] Get';
}

export class AddUserCollection {
  static readonly type = '[UserCollections] Add';

  constructor(public payload: string) {}
}

