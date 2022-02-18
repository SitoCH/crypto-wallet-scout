import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AutoLoginPartialRoutesGuard } from "angular-auth-oidc-client";
import { HomeComponent } from "./components/home/home.component";
import { SearchAddressComponent } from "./components/search-address/search-address.component";
import { UserCollectionsComponent } from "./components/user-collections/user-collections.component";
import {
  UserCollectionAddressDetailComponent
} from "./components/user-collections/user-collection-address-detail/user-collection-address-detail.component";
import {
  UserCollectionDetailComponent
} from "./components/user-collections/user-collection-detail/user-collection-detail.component";


const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'search', component: SearchAddressComponent, canActivate: [AutoLoginPartialRoutesGuard]},
  {path: 'collections', component: UserCollectionsComponent, canActivate: [AutoLoginPartialRoutesGuard]},
  {
    path: 'collection/:id/address/:address',
    component: UserCollectionAddressDetailComponent,
    canActivate: [AutoLoginPartialRoutesGuard]
  },
  {
    path: 'collection/:id/detail',
    component: UserCollectionDetailComponent,
    canActivate: [AutoLoginPartialRoutesGuard]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
