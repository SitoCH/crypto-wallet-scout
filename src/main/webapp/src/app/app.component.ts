import { Component } from '@angular/core';
import { OidcSecurityService } from "angular-auth-oidc-client";
import { Select, Store } from "@ngxs/store";
import { ApplicationState } from "./state/application.state";
import { Observable } from "rxjs";
import { SetAuthentication } from "./state/authentication.state";
import { Router } from "@angular/router";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  @Select(ApplicationState.isSidebarClosed) isSidebarClosed$: Observable<boolean> | undefined;

  constructor(public oidcSecurityService: OidcSecurityService,
              private router: Router,
              private store: Store) {
  }

  ngOnInit() {
    this.oidcSecurityService.checkAuth().subscribe(data => {
      this.store.dispatch(new SetAuthentication(data.isAuthenticated));
      this.router.navigate(['/dashboard']);
    });
  }
}
