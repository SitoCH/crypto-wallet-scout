import { Pipe, PipeTransform } from '@angular/core';
import { DecimalPipe } from "@angular/common";

@Pipe({
  name: 'tokenUsdValue'
})
export class TokenUsdValuePipe implements PipeTransform {

  constructor(private decimalPipe: DecimalPipe) {
  }

  transform(value: number, ...args: unknown[]): unknown {
    if (value > 0 && value < 0.99) {
      return this.decimalPipe.transform(value, '1.4-4');
    }

    return this.decimalPipe.transform(value, '1.2-2');
  }

}
