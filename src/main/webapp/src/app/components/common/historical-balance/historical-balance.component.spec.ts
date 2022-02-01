import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoricalBalanceComponent } from './historical-balance.component';

describe('HistoricalBalanceComponent', () => {
  let component: HistoricalBalanceComponent;
  let fixture: ComponentFixture<HistoricalBalanceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HistoricalBalanceComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HistoricalBalanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
