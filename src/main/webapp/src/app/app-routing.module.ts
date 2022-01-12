import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from "./components/main/home/home.component";
import { DashboardComponent } from "./components/main/dashboard/dashboard.component";
import { UserCollectionsComponent } from "./components/main/user-collections/user-collections.component";
import { SearchAddressComponent } from "./components/main/search-address/search-address.component";
import {
  UserCollectionAddressDetailComponent
} from "./components/main/user-collections/user-collection-address-detail/user-collection-address-detail.component";
import { AutoLoginPartialRoutesGuard } from "angular-auth-oidc-client";


const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'dashboard', component: DashboardComponent, canActivate: [AutoLoginPartialRoutesGuard]},
  {path: 'search', component: SearchAddressComponent, canActivate: [AutoLoginPartialRoutesGuard]},
  {path: 'collections', component: UserCollectionsComponent, canActivate: [AutoLoginPartialRoutesGuard]},
  {
    path: 'collection/:id/address/:address',
    component: UserCollectionAddressDetailComponent,
    canActivate: [AutoLoginPartialRoutesGuard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
