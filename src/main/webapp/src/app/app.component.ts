import { Component, OnInit } from '@angular/core';
import { OidcSecurityService } from "angular-auth-oidc-client";
import { Select, Store } from "@ngxs/store";
import { ApplicationState, ToggleSidebar } from "./state/application.state";
import { Observable } from "rxjs";
import { SetAuthentication } from "./state/authentication.state";
import { GetUserCollections } from "./state/user-collections.state";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  @Select(ApplicationState.isSidebarClosed) isSidebarClosed$!: Observable<boolean>;

  isSidebarClosed = false;

  constructor(private oidcSecurityService: OidcSecurityService,
              private store: Store) {
  }

  ngOnInit() {
    this.oidcSecurityService.checkAuth().subscribe(data => {
      this.store.dispatch(new SetAuthentication(data.isAuthenticated));
      this.store.dispatch(new GetUserCollections());
    });

    this.isSidebarClosed$.subscribe(isSidebarClosed => {
      this.isSidebarClosed = isSidebarClosed;
      const bodyTag = document.body;
      if (isSidebarClosed) {
        bodyTag.classList.remove('collapsed');
      } else {
        bodyTag.classList.add('collapsed');
      }
    });
  }

  closeSidebarIfNeeded($event: MouseEvent) {
    if (!this.isSidebarClosed) {
      this.store.dispatch(new ToggleSidebar());
      $event.stopPropagation();
    }

  }
}
