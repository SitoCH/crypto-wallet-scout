import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddressBalanceTableComponent } from './address-balance-table.component';

describe('AddressBalanceTableComponent', () => {
  let component: AddressBalanceTableComponent;
  let fixture: ComponentFixture<AddressBalanceTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddressBalanceTableComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddressBalanceTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
