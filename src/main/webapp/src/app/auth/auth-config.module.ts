import { NgModule } from '@angular/core';
import {
  AuthModule,
  LogLevel,
  OpenIdConfiguration,
  StsConfigHttpLoader,
  StsConfigLoader
} from 'angular-auth-oidc-client';
import { ConfigurationService } from "../services/configuration.service";
import { from } from "rxjs";
import { LocalStorage } from "./local-storage";

export const stsConfigLoader = (configurationService: ConfigurationService) => {
  return new StsConfigHttpLoader(from(configurationService.getConfiguration().then((config): OpenIdConfiguration => {
    return {
      authority: config.authServerUrl,
      redirectUrl: window.location.origin,
      postLogoutRedirectUri: window.location.origin,
      clientId: config.clientId,
      scope: 'openid profile',
      responseType: 'id_token token',
      silentRenew: true,
      useRefreshToken: true,
      ignoreNonceAfterRefresh: true,
      triggerAuthorizationResultEvent: true,
      logLevel: LogLevel.Debug,
      historyCleanupOff: false,
      storage: new LocalStorage()
    };
  })));
};

@NgModule({
  imports: [
    AuthModule.forRoot({
      loader: {
        provide: StsConfigLoader,
        useFactory: stsConfigLoader,
        deps: [ConfigurationService],
      }
    }),
  ],
  exports: [AuthModule],
})
export class AuthConfigModule {
}
