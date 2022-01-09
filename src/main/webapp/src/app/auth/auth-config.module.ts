import { NgModule } from '@angular/core';
import { AuthModule, LogLevel, StsConfigHttpLoader, StsConfigLoader } from 'angular-auth-oidc-client';
import { ConfigurationService } from "../services/configuration.service";
import { from } from "rxjs";

export const stsConfigLoader = (configurationService: ConfigurationService) => {
  return new StsConfigHttpLoader(from(configurationService.getConfiguration().then((config) => {
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
      logLevel: LogLevel.Warn,
      historyCleanupOff: false,
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
      },
    }),
  ],
  exports: [AuthModule],
})
export class AuthConfigModule {
}
