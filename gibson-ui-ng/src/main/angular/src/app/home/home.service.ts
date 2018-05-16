import { Injectable } from '@angular/core';
import { Http }       from '@angular/http';
import { Observable } from 'rxjs/Observable';

import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

@Injectable()
export class HomeService {

  constructor(private http: Http) { }
  
  getMatchupByDate(date: Date): Observable<Scoreboard[]> {
        return this.http
           .get('/scoreboard/'+ date.toISOString())
           .map(response => response.json() as Scoreboard[])
           .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }

  getMatchups(): Observable<Scoreboard[]> {
  	  	return this.http
  		     .get('/scoreboard/today')
  			   .map(response => response.json() as Scoreboard[])
  			   .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }

}

export class Scoreboard {
  constructor(public league:string, public status:string, public games: Game[]) {}
}

export class Game {
	constructor(public id: string, public league: string, public home: GameTeam, public away: GameTeam,
    public time: string, public gameStatistics: GameStatistic[] )  {}
}

export class GameTeam {
  constructor(public name: string, public code: string, public record: string, public city: string)  {}
}

export class GameStatistic {
   constructor(public name: string, public value: string)  {}
}

