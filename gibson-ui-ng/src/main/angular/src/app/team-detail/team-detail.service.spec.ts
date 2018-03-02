import { TestBed, inject } from '@angular/core/testing';

import { TeamDetailService } from './team-detail.service';

describe('TeamDetailService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TeamDetailService]
    });
  });

  it('should ...', inject([TeamDetailService], (service: TeamDetailService) => {
    expect(service).toBeTruthy();
  }));
});
