import { Injectable } from '@angular/core';

import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, } from '@angular/common/http';
import { Observable } from 'rxjs';
import { OidcSecurityService } from "angular-auth-oidc-client";

@Injectable()
export class OAuthInterceptor implements HttpInterceptor {

  constructor(public oidcSecurityService: OidcSecurityService) {
  }

  public intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {

    const url = req.url.toLowerCase();
    if (url.startsWith('api')) {
      let idToken = this.oidcSecurityService.getIdToken();
      if (idToken) {
        const header = 'Bearer ' + idToken;
        const headers = req.headers.set('Authorization', header);
        req = req.clone({headers});
      }
    }

    return next.handle(req);
  }
}
