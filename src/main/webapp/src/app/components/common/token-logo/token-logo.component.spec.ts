import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TokenLogoComponent } from './token-logo.component';

describe('TokenLogoComponent', () => {
  let component: TokenLogoComponent;
  let fixture: ComponentFixture<TokenLogoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TokenLogoComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TokenLogoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
