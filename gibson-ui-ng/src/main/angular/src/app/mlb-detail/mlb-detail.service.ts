import { Injectable } from '@angular/core';
import { Http }       from '@angular/http';
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
   constructor(public id: string, public gameDate: string, public status: string)  {}
 }
