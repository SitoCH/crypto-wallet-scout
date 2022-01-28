import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCollectionDetailComponent } from './user-collection-detail.component';
import { Store } from "@ngxs/store";
import { ActivatedRoute } from "@angular/router";
import { of } from "rxjs";
import { UserCollectionService } from "../../../../services/user-collection.service";

describe('UserCollectionDetailComponent', () => {
  let component: UserCollectionDetailComponent;
  let fixture: ComponentFixture<UserCollectionDetailComponent>;

  const mockedUserCollectionService = jasmine.createSpyObj('UserCollectionService', ['getAddressBalance']);
  const mockedStore = jasmine.createSpyObj('Store', ['dispatch']);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserCollectionDetailComponent],
      providers: [{
        provide: UserCollectionService,
        useValue: mockedUserCollectionService
      }, {
        provide: Store,
        useValue: mockedStore
      }, {
        provide: ActivatedRoute, useValue: {
          params: of({id: 'test'})
        }
      }]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserCollectionDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
