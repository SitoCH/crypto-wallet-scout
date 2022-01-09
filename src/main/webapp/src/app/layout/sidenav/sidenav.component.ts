import { Component } from '@angular/core';
import { OidcSecurityService } from "angular-auth-oidc-client";
import { Select, Store } from "@ngxs/store";
import { Observable } from "rxjs";
import { AuthenticationState, SetAuthentication } from "../../state/authentication.state";
import { Router } from "@angular/router";


@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.scss']
})
export class SidenavComponent {

  @Select(AuthenticationState.isAuthenticated) isAuthenticated$: Observable<boolean> | undefined;

  constructor(private oidcSecurityService: OidcSecurityService,
              private router: Router,
              private store: Store) {
  }

  doLogin() {
    this.oidcSecurityService.authorize();
  }

  doLogout() {
    this.oidcSecurityService.logoff();
    this.store.dispatch(new SetAuthentication(false));
    this.router.navigate(['/']);
  }
}
