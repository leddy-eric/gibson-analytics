import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { TeamComponent }        from './team/team.component';
import { TeamDetailComponent }  from './team-detail/team-detail.component';
import { PlayerComponent }	    from './player/player.component';
import { FootballComponent } from './football/football.component';
import { HomeComponent }		from './home/home.component';
import { MlbDetailComponent } from "./mlb-detail/mlb-detail.component";


const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  { path: 'teams',  component: TeamComponent },
  { path: 'football',  component: FootballComponent },
  { path: 'team/:id', component: TeamDetailComponent},
  { path: 'mlb/:id', component: MlbDetailComponent},
  { path: 'players', component: PlayerComponent }
];
@NgModule({
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule { }
