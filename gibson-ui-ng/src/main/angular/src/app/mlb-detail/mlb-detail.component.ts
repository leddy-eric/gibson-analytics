import { Observable } from 'rxjs';

import { ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { MlbDetailService, MlbDetail, MlbActive, MlbPlayer, MlbStats } from './mlb-detail.service';



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

  getSluggingValue(active: MlbActive) {
      return this.getStat(active.player, 'ParkNormalizedSLG');
  }

  getStrikeOutValue(active: MlbActive) {
      return this.getStat(active.player, 'totalBatterStrikeoutRate');
  }
  
   getPitchingStrikeOutValue(active: MlbActive) {
      return this.getStat(active.player, 'totalPitcherStrikeoutRate');
  }
  
  getWalkValue(active: MlbActive) {
      return this.getStat(active.player, 'totalBatterWalkRate');
  }  
  
  getPitchingWalkValue(active: MlbActive) {
      return this.getStat(active.player, 'totalPitcherWalkRate');
  } 
  
  getObpValue(active: MlbActive) {
      return this.getStat(active.player, 'totalBatterwOBA');
  }

  getPitchingObpValue(active: MlbActive) {
      return this.getStat(active.player, 'totalPitcherwOBA');
  }
  
  getSluggingAgainstValue(active: MlbActive) {
      return this.getStat(active.player, 'ParkNormalizedSLGAgainst');
  }

  getObpAgainstValue(active: MlbActive) {
      return this.getStat(active.player, 'totalPitcherwOBA');
  }

  getRankValue(active: MlbActive) {
      return this.getStat(active.player, 'Rank');
  }

  getFactorValue(active: MlbActive) {
      return this.getStat(active.player, 'Factor');
  }

  getGbpValue(active: MlbActive) {
      return this.getStat(active.player, 'GBPerc');
  }

  getBsrValue(active: MlbActive) {
      return this.getStat(active.player, 'BsRperPA');
  }

  getDefValue(active: MlbActive) {
      return this.getStat(active.player, 'DefperInn');
  }

  getStat(player: MlbPlayer, name: string) {
    const stat = player.statistics.find(function(s) {
      return s.name === name;
    });

    if (stat) {
      return stat.value;
    }

    return null;
  }
}
