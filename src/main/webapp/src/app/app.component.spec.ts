import { TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { AppComponent } from './app.component';
import { OidcSecurityService } from "angular-auth-oidc-client";
import { Store } from "@ngxs/store";

describe('AppComponent', () => {


  const mockedOidcSecurityService = jasmine.createSpyObj('OidcSecurityService', ['checkAuth']);
  const mockedStore = jasmine.createSpyObj('Store', ['dispatch']);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule
      ],
      declarations: [
        AppComponent
      ],
      providers: [{
        provide: OidcSecurityService,
        useValue: mockedOidcSecurityService
      }, {
        provide: Store,
        useValue: mockedStore
      }]
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });
});
