import { BrowserModule }    from '@angular/platform-browser';
import { NgModule }         from '@angular/core';
import { FormsModule }      from '@angular/forms';
import { HttpModule }       from '@angular/http';
import { RouterModule }     from '@angular/router';
import { AppRoutingModule } from './app-routing.module';

import { BsDropdownModule } from 'ngx-bootstrap';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

// Components
import { AppComponent }     from './app.component';
import { TeamComponent }    from './team/team.component';
import { PlayerComponent }  from './player/player.component';

// Services
import { TeamService }         from './team/team.service';
import { HomeComponent }       from './home/home.component';
import { TeamDetailComponent } from './team-detail/team-detail.component';
import { FootballComponent } from './football/football.component';
import { MlbDetailComponent } from './mlb-detail/mlb-detail.component';


@NgModule({
  declarations: [
    AppComponent,
    TeamComponent,
    PlayerComponent,
    HomeComponent,
    TeamDetailComponent,
    FootballComponent,
    MlbDetailComponent
  ],
  imports: [
    NgbModule.forRoot(),
    BsDropdownModule.forRoot(),
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule
  ],
  providers: [
    TeamService
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule { }
