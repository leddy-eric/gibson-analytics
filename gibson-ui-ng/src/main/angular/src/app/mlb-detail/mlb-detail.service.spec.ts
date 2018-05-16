import { TestBed, inject } from '@angular/core/testing';

import { MlbDetailService } from './mlb-detail.service';

describe('MlbDetailService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MlbDetailService]
    });
  });

  it('should be created', inject([MlbDetailService], (service: MlbDetailService) => {
    expect(service).toBeTruthy();
  }));
});
