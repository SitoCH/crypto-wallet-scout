import { Pipe, PipeTransform } from '@angular/core';
import { Network } from "../../../../generated/client";

@Pipe({
  name: 'networkImage'
})
export class NetworkImagePipe implements PipeTransform {

  transform(network: Network, ...args: unknown[]): unknown {
    switch (network) {
      case Network.POLYGON:
        return 'assets/images/network/Polygon.png';
      case Network.AVALANCHE:
        return 'assets/images/network/Avalanche.png';
      case Network.TERRA:
        return 'assets/images/network/Luna.png';
      case Network.CRONOS:
        return 'assets/images/network/Crypto-com.png';
      case Network.SOLANA:
        return 'assets/images/network/Solana.png';
      case Network.COSMOS:
        return 'assets/images/network/Cosmos.png';
      case Network.BITCOIN:
        return 'assets/images/network/Bitcoin.png';
      case Network.POLKADOT:
        return 'assets/images/network/Polkadot.png';
    }

    return null;
  }

}
