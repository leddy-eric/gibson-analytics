import { Injectable } from '@angular/core';
import { Http }       from '@angular/http';
import { Observable } from 'rxjs/Observable';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

@Injectable()
export class TeamService {

  constructor(private http: Http) { }

  getTeams(): Observable<String[]> {
  	return this.http
  			   .get('/team')
  			   .map(response => response.json() as String[])
  			   .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }

}
