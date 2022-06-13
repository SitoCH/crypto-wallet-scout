import { Pipe, PipeTransform } from '@angular/core';
import { DecimalPipe } from "@angular/common";

@Pipe({
  name: 'tokenAmountDecimals'
})
export class TokenAmountDecimalsPipe implements PipeTransform {

  constructor(private decimalPipe: DecimalPipe) {
  }

  transform(value: number, tokenUsdValue: number): unknown {
    if (tokenUsdValue > 10000) {
      return this.decimalPipe.transform(value, '1.8-8');
    }

    if (tokenUsdValue > 1000) {
      return this.decimalPipe.transform(value, '1.6-6');
    }

    return this.decimalPipe.transform(value, '1.4-4');
  }

}
