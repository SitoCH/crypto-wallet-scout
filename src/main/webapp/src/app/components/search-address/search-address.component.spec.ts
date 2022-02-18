import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SearchAddressComponent } from './search-address.component';
import { AddressBalanceService } from "../../../services/address-balance.service";
import { Store } from "@ngxs/store";

describe('SearchAddressComponent', () => {
  let component: SearchAddressComponent;
  let fixture: ComponentFixture<SearchAddressComponent>;

  const mockedAddressBalanceService = jasmine.createSpyObj('AddressBalanceService', ['getAddressBalance']);
  const mockedStore = jasmine.createSpyObj('Store', ['dispatch']);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SearchAddressComponent],
      providers: [{
        provide: AddressBalanceService,
        useValue: mockedAddressBalanceService
      }, {
        provide: Store,
        useValue: mockedStore
      }]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchAddressComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
