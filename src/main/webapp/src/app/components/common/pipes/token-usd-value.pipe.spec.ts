import { TokenUsdValuePipe } from './token-usd-value.pipe';

describe('TokenUsdValuePipe', () => {
  it('create an instance', () => {
    const pipe = new TokenUsdValuePipe();
    expect(pipe).toBeTruthy();
  });
});
