import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from "@angular/common/http";
import { FormsModule } from "@angular/forms";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fas } from "@fortawesome/free-solid-svg-icons";
import { far } from "@fortawesome/free-regular-svg-icons";
import { InjectableRestApplicationClient } from "./services/utils/injectable-rest-application-client.service";
import { AddressBalanceService } from "./services/address-balance.service";
import { environment } from "../environments/environment";
import { NgxsModule } from "@ngxs/store";
import { UserCollectionsState } from "./state/user-collections.state";
import { HeaderComponent } from "./layout/header/header.component";
import { SidenavComponent } from "./layout/sidenav/sidenav.component";
import { NetworkLogoComponent } from "./components/common/network-logo/network-logo.component";
import { UserCollectionsComponent } from "./components/main/user-collections/user-collections.component";
import { HomeComponent } from "./components/main/home/home.component";
import { DashboardComponent } from "./components/main/dashboard/dashboard.component";
import { OAuthInterceptor } from "./auth/oauth-interceptor.service";
import { AuthConfigModule } from "./auth/auth-config.module";
import { ApplicationState } from "./state/application.state";
import { NgxsStoragePluginModule } from "@ngxs/storage-plugin";
import { AuthenticationState } from "./state/authentication.state";
import { SearchAddressComponent } from './components/main/search-address/search-address.component';
import {
  AddressBalanceTableComponent
} from './components/common/address-balance-table/address-balance-table.component';
import {
  AddressBalanceOverviewComponent
} from './components/common/address-balance-overview/address-balance-overview.component';
import {
  UserCollectionAddressDetailComponent
} from "./components/main/user-collections/user-collection-address-detail/user-collection-address-detail.component";

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    HomeComponent,
    HeaderComponent,
    SidenavComponent,
    UserCollectionsComponent,
    NetworkLogoComponent,
    SearchAddressComponent,
    AddressBalanceTableComponent,
    AddressBalanceOverviewComponent,
    UserCollectionAddressDetailComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    NgbModule,
    FontAwesomeModule,
    AuthConfigModule,
    NgxsStoragePluginModule.forRoot({
      key: [ApplicationState]
    }),
    NgxsModule.forRoot([
        UserCollectionsState,
        ApplicationState,
        AuthenticationState],
      {
        developmentMode: !environment.production
      }),
  ],
  providers: [
    InjectableRestApplicationClient,
    AddressBalanceService,
    {provide: HTTP_INTERCEPTORS, useClass: OAuthInterceptor, multi: true}
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
  constructor(library: FaIconLibrary) {
    library.addIconPacks(fas, far);
  }
}
