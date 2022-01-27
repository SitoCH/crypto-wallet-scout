import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TokenBalanceComponent } from './token-balance.component';

describe('TokenBalanceComponent', () => {
  let component: TokenBalanceComponent;
  let fixture: ComponentFixture<TokenBalanceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TokenBalanceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TokenBalanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
