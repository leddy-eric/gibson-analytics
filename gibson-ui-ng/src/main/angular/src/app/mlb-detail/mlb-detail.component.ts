import { Observable } from 'rxjs';

import { ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { MlbDetailService, MlbDetail } from './mlb-detail.service';



@Component({
  selector: 'app-mlb-detail',
  templateUrl: './mlb-detail.component.html',
  styleUrls: ['./mlb-detail.component.css'],
  providers: [MlbDetailService]
})
export class MlbDetailComponent implements OnInit {
  detail: MlbDetail;
  id: string;

  constructor(private route:ActivatedRoute, private detailService: MlbDetailService) { }

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.detailService.getDetail(this.id).subscribe(detail => this.detail = detail);
  }

}
