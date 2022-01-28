import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TokenBalanceTableComponent } from './token-balance-table.component';
import { Store } from "@ngxs/store";

describe('AddressBalanceTableComponent', () => {
  let component: TokenBalanceTableComponent;
  let fixture: ComponentFixture<TokenBalanceTableComponent>;

  const mockedStore = jasmine.createSpyObj('Store', ['dispatch']);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TokenBalanceTableComponent],
      providers: [{
        provide: Store,
        useValue: mockedStore
      }]
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
