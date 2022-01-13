import { Component, Input, OnInit } from '@angular/core';
import { Network } from "../../../../generated/client";

@Component({
  selector: 'app-token-logo',
  templateUrl: './token-logo.component.html',
  styleUrls: ['./token-logo.component.scss']
})
export class TokenLogoComponent implements OnInit {

  @Input()
  network = Network.POLYGON;
  @Input()
  tokenImage!: string;
  @Input()
  tokenName!: string;

  networkImage!: string;

  ngOnInit(): void {
    switch (this.network) {
      case Network.POLYGON:
        this.networkImage = 'assets/images/network/Polygon.png';
        break;
      case Network.AVALANCHE:
        this.networkImage = 'assets/images/network/Avalanche.png';
        break;
      case Network.TERRA:
        this.networkImage = 'assets/images/network/Luna.png';
        break;
      case Network.CRO:
        this.networkImage = 'assets/images/network/Crypto-com.png';
        break;
      case Network.SOLANA:
        this.networkImage = 'assets/images/network/Solana.png';
        break;
    }
  }

}
