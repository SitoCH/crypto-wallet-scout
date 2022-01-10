import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCollectionAddressDetailComponent } from './user-collection-address-detail.component';

describe('UserCollectionAddressDetailComponent', () => {
  let component: UserCollectionAddressDetailComponent;
  let fixture: ComponentFixture<UserCollectionAddressDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserCollectionAddressDetailComponent ]
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
