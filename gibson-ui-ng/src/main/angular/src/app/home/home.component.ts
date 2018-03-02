import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { HomeService, Scoreboard }	from './home.service';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  providers: [HomeService]
})
export class HomeComponent implements OnInit {
  today: Date = new Date();
  model: NgbDateStruct = {year: this.today.getFullYear(), month: this.today.getMonth() + 1, day: this.today.getDate() || 1 };
  show: boolean = false;
  fullScoreboard: Scoreboard[] = [];

  constructor(private homeService: HomeService) { }

  ngOnInit() {
    this.loadData();
  }

  ngOnDestroy() {
  }
  
  onSelect(newValue : NgbDateStruct) {
    console.log('changed', this.model, event);
    this.model = newValue; 
    this.show = false;
    this.homeService.getMatchupByDate(new Date(this.model.year, this.model.month - 1, this.model.day)).subscribe(
          games => this.fullScoreboard = games,
          err => console.error(err),
          () => this.show = true);
  }
  
  loadData() {
    this.show = false;
    this.homeService.getMatchups().subscribe(
          games => this.fullScoreboard = games,
          err => console.error(err),
          () => this.show = true);
  }
}
