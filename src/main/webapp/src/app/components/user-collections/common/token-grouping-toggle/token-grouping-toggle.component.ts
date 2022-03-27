import { Component, OnInit } from '@angular/core';
import { ApplicationState, ToggleGroupTokenTable } from "../../../../state/application.state";
import { map, Observable } from "rxjs";
import { IconProp } from "@fortawesome/fontawesome-svg-core";
import { Store } from "@ngxs/store";

@Component({
  selector: 'app-token-grouping-toggle',
  templateUrl: './token-grouping-toggle.component.html',
  styleUrls: ['./token-grouping-toggle.component.scss']
})
export class TokenGroupingToggleComponent implements OnInit {

  tokenTableGroupingIcon$!: Observable<IconProp>;

  constructor(private store: Store) {
  }

  ngOnInit(): void {
    this.tokenTableGroupingIcon$ = this.store.select(ApplicationState.isGroupTokenTable).pipe(
      map(isGrouped => {
        return isGrouped ? 'compress-arrows-alt' : 'expand-arrows-alt';
      })
    )
  }

  onGroupingChange() {
    this.store.dispatch(new ToggleGroupTokenTable())
  }
}
