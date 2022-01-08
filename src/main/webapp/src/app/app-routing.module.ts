import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from "./components/main/home/home.component";
import { DashboardComponent } from "./components/main/dashboard/dashboard.component";
import { UserCollectionsComponent } from "./components/main/user-collections/user-collections.component";
import { IsLoggedInGuard } from "./auth/is-logged-in.guard";


const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'dashboard', component: DashboardComponent, canActivate: [IsLoggedInGuard]},
  {path: 'collections', component: UserCollectionsComponent, canActivate: [IsLoggedInGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
