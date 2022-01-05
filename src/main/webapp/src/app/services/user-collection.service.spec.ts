import { TestBed } from '@angular/core/testing';

import { UserCollectionServiceService } from './user-collection-service.service';

describe('UserCollectionServiceService', () => {
  let service: UserCollectionServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserCollectionServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
