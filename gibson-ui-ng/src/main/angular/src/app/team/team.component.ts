import { Component, OnInit } from '@angular/core';

import { TeamService }		 from './team.service';

@Component({
  selector: 'app-team',
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.css'],
  providers: [TeamService]
})
export class TeamComponent implements OnInit {
  teams: String[] = [];
  title: String = 'test';

  constructor(private teamService: TeamService) { }

  ngOnInit() {
  	this.teamService.getTeams().subscribe(teams => this.teams = teams);
  }

}
