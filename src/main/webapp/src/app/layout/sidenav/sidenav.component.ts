import { Component } from '@angular/core';
import { OidcSecurityService } from "angular-auth-oidc-client";


@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.scss']
})
export class SidenavComponent {

  constructor(public oidcSecurityService: OidcSecurityService) {
  }

  doLogin() {
    this.oidcSecurityService.authorize();
  }
}
