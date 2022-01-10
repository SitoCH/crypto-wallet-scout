import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddressBalanceOverviewComponent } from './address-balance-overview.component';

describe('AddressBalanceOverviewComponent', () => {
  let component: AddressBalanceOverviewComponent;
  let fixture: ComponentFixture<AddressBalanceOverviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddressBalanceOverviewComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddressBalanceOverviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
