import { TestBed } from '@angular/core/testing';

import { DefaultRouteGuardService } from './default-route-guard.service';

describe('DefaultRouteGuardService', () => {
  let service: DefaultRouteGuardService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DefaultRouteGuardService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
