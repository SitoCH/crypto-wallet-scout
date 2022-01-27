import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TokenBalanceTableComponent } from './token-balance-table.component';

describe('AddressBalanceTableComponent', () => {
  let component: TokenBalanceTableComponent;
  let fixture: ComponentFixture<TokenBalanceTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TokenBalanceTableComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TokenBalanceTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
