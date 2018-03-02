import { Component, OnInit } 		from '@angular/core';
import { ActivatedRoute, Params }   from '@angular/router';

import { TeamDetailService, Team }	from './team-detail.service';

import 'rxjs/add/operator/switchMap';

@Component({
  selector: 'app-team-detail',
  templateUrl: './team-detail.component.html',
  styleUrls: ['./team-detail.component.css'],
  providers: [TeamDetailService]
})
export class TeamDetailComponent implements OnInit {
  teamDetail: Team[] = [];
  title: String = 'testDetail';

  constructor(
  	private teamDetailService: TeamDetailService,
  	private route: ActivatedRoute) { }

  ngOnInit() {
  	this.route.params
  		.switchMap((params: Params) => this.teamDetailService.getTeamByName(params['id']))
  		.subscribe(response => this.onResponse(response));
  }

  onResponse(response: any) {
    const r = response;
    this.teamDetail = response;
  }

}
