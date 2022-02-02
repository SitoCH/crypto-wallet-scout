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
import { UserCollectionsComponent } from "./components/main/user-collections/user-collections.component";
import { HomeComponent } from "./components/main/home/home.component";
import { DashboardComponent } from "./components/main/dashboard/dashboard.component";
import { OAuthInterceptor } from "./auth/oauth-interceptor.service";
import { AuthConfigModule } from "./auth/auth-config.module";
import { SearchAddressComponent } from './components/main/search-address/search-address.component';
import {
  TokenBalanceTableComponent
} from './components/common/token-balance-table/token-balance-table.component';
import {
  TokenBalanceOverviewComponent
} from './components/common/token-balance-overview/token-balance-overview.component';
import {
  UserCollectionAddressDetailComponent
} from "./components/main/user-collections/user-collection-address-detail/user-collection-address-detail.component";
import { TokenLogoComponent } from './components/common/token-logo/token-logo.component';
import { NetworkImagePipe } from './components/common/token-logo/network-image.pipe';
import {
  UserCollectionDetailComponent
} from './components/main/user-collections/user-collection-detail/user-collection-detail.component';
import { NgxsConfigModule } from "./state/ngxs.module";
import { TokenBalanceComponent } from './components/common/token-balance/token-balance.component';
import { UserCollectionHistoricalBalanceComponent } from './components/main/user-collections/user-collection-historical-balance/user-collection-historical-balance.component';
import { NgChartsModule } from "ng2-charts";
import { HistoricalBalanceComponent } from './components/common/historical-balance/historical-balance.component';
import { AddressHistoricalBalanceComponent } from './components/main/user-collections/address-historical-balance/address-historical-balance.component';
import { TokenBalanceChartsComponent } from './components/common/token-balance-charts/token-balance-charts.component';

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
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
