import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCollectionAddressDetailComponent } from './user-collection-address-detail.component';
import { ActivatedRoute } from "@angular/router";
import { of } from 'rxjs';
import { AddressBalanceService } from "../../../services/address-balance.service";

describe('UserCollectionAddressDetailComponent', () => {
  let component: UserCollectionAddressDetailComponent;
  let fixture: ComponentFixture<UserCollectionAddressDetailComponent>;

  const mockedAddressBalanceService = jasmine.createSpyObj('AddressBalanceService', ['getAddressBalance']);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserCollectionAddressDetailComponent],
      providers: [{
        provide: AddressBalanceService,
        useValue: mockedAddressBalanceService
      }, {
        provide: ActivatedRoute, useValue: {
          params: of({id: 'test'})
        }
      }]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserCollectionAddressDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
