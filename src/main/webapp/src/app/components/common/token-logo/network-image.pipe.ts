import { Pipe, PipeTransform } from '@angular/core';
import { Network } from "../../../../generated/client";

@Pipe({
  name: 'networkImage'
})
export class NetworkImagePipe implements PipeTransform {

  transform(network: Network): unknown {
    switch (network) {
      case Network.POLYGON:
        return 'assets/images/network/Polygon.png';
      case Network.AVALANCHE:
        return 'assets/images/network/Avalanche.png';
      case Network.TERRA:
        return 'assets/images/network/Luna.png';
      case Network.TERRA_CLASSIC:
        return 'assets/images/network/LunaClassic.png';
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
      case Network.OPTIMISM:
        return 'assets/images/network/Optimism.png';
      case Network.ETHEREUM:
        return 'assets/images/network/Ethereum.png';
      case Network.DOGECOIN:
        return 'assets/images/network/Dogecoin.png';
      case Network.BNB:
        return 'assets/images/network/Bnb.png';
    }

    return null;
  }

}
