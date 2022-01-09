import { Component } from '@angular/core';
import { Store } from "@ngxs/store";
import { ToggleSidebar } from "../../state/application.state";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  constructor(private store: Store) {
  }

  toggleSidebar() {
    this.store.dispatch(new ToggleSidebar());
  }
}
