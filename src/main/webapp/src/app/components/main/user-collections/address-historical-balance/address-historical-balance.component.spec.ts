import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddressHistoricalBalanceComponent } from './address-historical-balance.component';

describe('AddressHistoricalBalanceComponent', () => {
  let component: AddressHistoricalBalanceComponent;
  let fixture: ComponentFixture<AddressHistoricalBalanceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddressHistoricalBalanceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddressHistoricalBalanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
