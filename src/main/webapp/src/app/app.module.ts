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
import { HeaderComponent } from "./layout/header/header.component";
import { SidenavComponent } from "./layout/sidenav/sidenav.component";
import { OAuthInterceptor } from "./auth/oauth-interceptor.service";
import { AuthConfigModule } from "./auth/auth-config.module";
import { TokenBalanceTableComponent } from './components/common/token-balance-table/token-balance-table.component';
import {
  TokenBalanceOverviewComponent
} from './components/common/token-balance-overview/token-balance-overview.component';
import { TokenLogoComponent } from './components/common/token-logo/token-logo.component';
import { NetworkImagePipe } from './components/common/token-logo/network-image.pipe';
import { NgxsConfigModule } from "./state/ngxs.module";
import { TokenBalanceComponent } from './components/common/token-balance/token-balance.component';
import { NgChartsModule } from "ng2-charts";
import { HistoricalBalanceComponent } from './components/common/historical-balance/historical-balance.component';
import { TokenBalanceChartsComponent } from './components/common/token-balance-charts/token-balance-charts.component';
import { HomeComponent } from "./components/home/home.component";
import { UserCollectionsComponent } from "./components/user-collections/user-collections.component";
import { SearchAddressComponent } from "./components/search-address/search-address.component";
import {
  UserCollectionAddressDetailComponent
} from "./components/user-collections/user-collection-address-detail/user-collection-address-detail.component";
import {
  UserCollectionDetailComponent
} from "./components/user-collections/user-collection-detail/user-collection-detail.component";
import {
  UserCollectionHistoricalBalanceComponent
} from "./components/user-collections/common/user-collection-historical-balance/user-collection-historical-balance.component";
import {
  AddressHistoricalBalanceComponent
} from "./components/user-collections/common/address-historical-balance/address-historical-balance.component";

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    HeaderComponent,
    SidenavComponent,
    UserCollectionsComponent,
    SearchAddressComponent,
    TokenBalanceTableComponent,
    TokenBalanceOverviewComponent,
    UserCollectionAddressDetailComponent,
    TokenLogoComponent,
    NetworkImagePipe,
    UserCollectionDetailComponent,
    TokenBalanceComponent,
    UserCollectionHistoricalBalanceComponent,
    HistoricalBalanceComponent,
    AddressHistoricalBalanceComponent,
    TokenBalanceChartsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    NgbModule,
    FontAwesomeModule,
    AuthConfigModule,
    NgxsConfigModule,
    NgChartsModule
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
