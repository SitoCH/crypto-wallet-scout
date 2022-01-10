import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from "./components/main/home/home.component";
import { DashboardComponent } from "./components/main/dashboard/dashboard.component";
import { UserCollectionsComponent } from "./components/main/user-collections/user-collections.component";
import { IsLoggedInGuard } from "./auth/is-logged-in.guard";
import { SearchAddressComponent } from "./components/main/search-address/search-address.component";
import {
  UserCollectionAddressDetailComponent
} from "./components/common/user-collection-address-detail/user-collection-address-detail.component";


const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'dashboard', component: DashboardComponent, canActivate: [IsLoggedInGuard]},
  {path: 'search', component: SearchAddressComponent, canActivate: [IsLoggedInGuard]},
  {path: 'collections', component: UserCollectionsComponent, canActivate: [IsLoggedInGuard]},
  {path: 'collection/:id/address/:address', component: UserCollectionAddressDetailComponent, canActivate: [IsLoggedInGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
