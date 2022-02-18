import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCollectionHistoricalBalanceComponent } from './user-collection-historical-balance.component';

describe('UserCollectionHistoricalBalanceComponent', () => {
  let component: UserCollectionHistoricalBalanceComponent;
  let fixture: ComponentFixture<UserCollectionHistoricalBalanceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserCollectionHistoricalBalanceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserCollectionHistoricalBalanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
