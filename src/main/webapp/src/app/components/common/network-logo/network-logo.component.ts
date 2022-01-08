import { Component, Input } from '@angular/core';
import { Network } from "../../../../generated/client";

@Component({
  selector: 'app-network-logo',
  templateUrl: './network-logo.component.html',
  styleUrls: ['./network-logo.component.scss']
})
export class NetworkLogoComponent {

  @Input()
  network = Network.POLYGON;

}
