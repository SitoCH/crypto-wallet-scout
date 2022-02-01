import { NgModule } from "@angular/core";
import { NgxsStoragePluginModule } from "@ngxs/storage-plugin";
import { ApplicationState } from "./application.state";
import { TokenState, TokenStateModel, TokenWithLastModified } from "./token.state";
import { NgxsModule } from "@ngxs/store";
import { UserCollectionsState } from "./user-collections.state";
import { AuthenticationState } from "./authentication.state";
import { environment } from "../../environments/environment";
import { AddressState } from "./address.state";

interface SerializableTokenStateModel {
  tokens: string | null;
}

function deserializeTokenState(obj: SerializableTokenStateModel): TokenStateModel {
  return {
    tokens: obj.tokens ? new Map(JSON.parse(obj.tokens)) : new Map<string, TokenWithLastModified>(),
    loading: new Set<string>()
  };
}

function serializeTokenState(obj: TokenStateModel): SerializableTokenStateModel {
  return {
    tokens: obj.tokens ? JSON.stringify(Array.from(obj.tokens.entries())) : null,
  };
}

@NgModule({
  imports: [
    NgxsStoragePluginModule.forRoot({
      key: [ApplicationState, TokenState],
      beforeSerialize: (obj, key) => {
        if (key === 'tokenState') {
          return serializeTokenState(obj);
        }
        return obj;
      },
      afterDeserialize: (obj, key) => {
        if (key === 'tokenState') {
          return deserializeTokenState(obj);
        }
        return obj;
      }
    }),
    NgxsModule.forRoot([
        UserCollectionsState,
        ApplicationState,
        AuthenticationState,
        TokenState,
        AddressState],
      {
        developmentMode: !environment.production
      }),
  ],
})
export class NgxsConfigModule {
}
