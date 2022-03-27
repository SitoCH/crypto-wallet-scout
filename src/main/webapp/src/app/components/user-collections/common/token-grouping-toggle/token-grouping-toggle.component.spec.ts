import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TokenGroupingToggleComponent } from './token-grouping-toggle.component';

describe('TokenGroupingToggleComponent', () => {
  let component: TokenGroupingToggleComponent;
  let fixture: ComponentFixture<TokenGroupingToggleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TokenGroupingToggleComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TokenGroupingToggleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
