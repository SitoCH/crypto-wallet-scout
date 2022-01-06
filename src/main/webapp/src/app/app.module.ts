import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HttpClientModule } from "@angular/common/http";
import { DashboardComponent } from './dashboard/dashboard.component';
import { HomeComponent } from './home/home.component';
import { FormsModule } from "@angular/forms";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { fas } from "@fortawesome/free-solid-svg-icons";
import { far } from "@fortawesome/free-regular-svg-icons";
import { InjectableRestApplicationClient } from "./services/utils/injectable-rest-application-client.service";
import { AddressBalanceService } from "./services/address-balance.service";
import { UserCollectionsComponent } from './user-collections/user-collections.component';
import { environment } from "../environments/environment";
import { NgxsModule } from "@ngxs/store";
import { UserCollectionsState } from "./state/user-collections.state";
import { NetworkLogoComponent } from './common/network-logo/network-logo.component';
import { HeaderComponent } from "./layout/header/header.component";
import { FooterComponent } from "./layout/footer/footer.component";
import { SidenavComponent } from "./layout/sidenav/sidenav.component";

@NgModule({
  declarations: [
    AppComponent,
    DashboardComponent,
    HomeComponent,
    HeaderComponent,
    FooterComponent,
    SidenavComponent,
    UserCollectionsComponent,
    NetworkLogoComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    NgbModule,
    FontAwesomeModule,
    NgxsModule.forRoot([UserCollectionsState], {
      developmentMode: !environment.production
    })
  ],
  providers: [
    InjectableRestApplicationClient,
    AddressBalanceService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
  constructor(library: FaIconLibrary) {
    library.addIconPacks(fas, far);
  }
}
