import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class MlbDetailService {

  constructor(private http: Http) { }

  getDetail(id: string): Observable<MlbDetail> {
            return this.http
           .get('/detail/?id=' + id)
           .map(response => response.json() as MlbDetail)
           .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }

}

export class MlbDetail {
   constructor(public id: string, public apiId: string, public gameDate: string,
      public status: string,  public home: MlbRoster, public away: MlbRoster)  {}
}

export class MlbRoster {
  constructor(public team: string, public source: string, public lineup: MlbActive[],
     public probable: MlbActive) {}
}

export class MlbActive {
  constructor(public battingOrder: number, public player: MlbPlayer) {}
}

export class MlbPlayer {
  constructor(public id: string, public name: string, public team: string, public position: string, public statistics: MlbStats[]) {}
}

export class MlbStats {
  constructor(public name: string, public value: string) {}
}
