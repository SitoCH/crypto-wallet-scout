import { TestBed } from '@angular/core/testing';

import { AddressBalanceService } from './address-balance.service';

describe('AddressBalanceService', () => {
  let service: AddressBalanceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AddressBalanceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
