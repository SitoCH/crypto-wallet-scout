import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SidenavComponent } from './sidenav.component';
import { OidcSecurityService } from "angular-auth-oidc-client";
import { Router } from "@angular/router";
import { NgxsModule } from "@ngxs/store";

describe('SidenavComponent', () => {
  let component: SidenavComponent;
  let fixture: ComponentFixture<SidenavComponent>;

  const mockedOidcSecurityService = jasmine.createSpyObj('OidcSecurityService', ['authorize']);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NgxsModule.forRoot([])],
      declarations: [SidenavComponent],
      providers: [{
        provide: OidcSecurityService,
        useValue: mockedOidcSecurityService
      }, {
        provide: Router,
        useValue: {}
      }]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SidenavComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
