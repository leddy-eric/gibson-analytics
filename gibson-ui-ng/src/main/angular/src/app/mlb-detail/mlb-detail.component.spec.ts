import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MlbDetailComponent } from './mlb-detail.component';

describe('MlbDetailComponent', () => {
  let component: MlbDetailComponent;
  let fixture: ComponentFixture<MlbDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MlbDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MlbDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
