import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCollectionsComponent } from './user-collections.component';
import { NgxsModule, Store } from "@ngxs/store";

describe('UserCollectionsComponent', () => {
  let component: UserCollectionsComponent;
  let fixture: ComponentFixture<UserCollectionsComponent>;

  const mockedStore = jasmine.createSpyObj('Store', ['dispatch', 'select']);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NgxsModule.forRoot([])],
      declarations: [UserCollectionsComponent],
      providers: [{
        provide: Store,
        useValue: mockedStore
      }]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserCollectionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
