import { Component, OnInit } from '@angular/core';
import { OidcSecurityService } from "angular-auth-oidc-client";
import { map, Observable } from "rxjs";


@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.scss']
})
export class SidenavComponent implements OnInit {

  isAuthenticated$: Observable<boolean> ;

  constructor(public oidcSecurityService: OidcSecurityService) {

    this.oidcSecurityService.checkAuth()

    this.isAuthenticated$ = this.oidcSecurityService.isAuthenticated$.pipe(map(result => result.isAuthenticated));
  }

  doLogin() {
    this.oidcSecurityService.authorize();
  }

  ngOnInit(): void {
    //this.isAuthenticated$ = this.oidcSecurityService.isAuthenticated$.pipe(map(result => result.isAuthenticated));
  }
}
