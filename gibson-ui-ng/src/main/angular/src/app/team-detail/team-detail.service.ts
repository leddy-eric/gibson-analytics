import { Injectable } from '@angular/core';
import { Http }       from '@angular/http';
import { Observable } from 'rxjs/Observable';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

@Injectable()
export class TeamDetailService {

  constructor(private http: Http) { }

  getTeamByName(name: string): Observable<Team[]> {
  	return this.http
  			   .get('/team/' + name)
  			   .map(response => response.json() as Team[])
  			   .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }

}

export class Team {
	constructor(public id: number, public name: string, public team: string)  {}
}
