import { Component } from '@angular/core';
import { OidcSecurityService } from "angular-auth-oidc-client";
import { Select } from "@ngxs/store";
import { ApplicationState } from "./state/application.state";
import { Observable } from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  @Select(ApplicationState.isSidebarClosed) isSidebarClosed$: Observable<boolean> | undefined;

  constructor(public oidcSecurityService: OidcSecurityService) {
  }

  ngOnInit() {
    this.oidcSecurityService.checkAuth().subscribe(data => console.log(data));
  }
}
