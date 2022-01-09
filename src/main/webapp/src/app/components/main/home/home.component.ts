import { Component, OnInit } from '@angular/core';
import { Select } from "@ngxs/store";
import { AuthenticationState } from "../../../state/authentication.state";
import { Observable } from "rxjs";
import { Router } from "@angular/router";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  @Select(AuthenticationState.isAuthenticated) isAuthenticated$: Observable<boolean> | undefined;

  constructor(private router: Router) {
  }

  ngOnInit(): void {
    this.isAuthenticated$?.subscribe(isAuthenticated => {
      if (isAuthenticated) {
        this.router.navigate(['/dashboard']);
      }
    });
  }

}
