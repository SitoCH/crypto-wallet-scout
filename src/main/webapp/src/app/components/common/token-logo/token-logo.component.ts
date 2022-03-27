import { Component, Input } from '@angular/core';
import { Network } from "../../../../generated/client";

@Component({
  selector: 'app-token-logo',
  templateUrl: './token-logo.component.html',
  styleUrls: ['./token-logo.component.scss']
})
export class TokenLogoComponent {

  @Input()
  network = Network.POLYGON;
  @Input()
  tokenImage?: string;
  @Input()
  tokenName?: string;
  @Input()
  hideNetwork = false;
}
