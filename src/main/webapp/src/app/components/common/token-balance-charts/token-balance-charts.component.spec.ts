import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TokenBalanceChartsComponent } from './token-balance-charts.component';

describe('TokenBalanceChartsComponent', () => {
  let component: TokenBalanceChartsComponent;
  let fixture: ComponentFixture<TokenBalanceChartsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TokenBalanceChartsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TokenBalanceChartsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
