import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TokenBalanceOverviewComponent } from './token-balance-overview.component';

describe('AddressBalanceOverviewComponent', () => {
  let component: TokenBalanceOverviewComponent;
  let fixture: ComponentFixture<TokenBalanceOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TokenBalanceOverviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TokenBalanceOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
