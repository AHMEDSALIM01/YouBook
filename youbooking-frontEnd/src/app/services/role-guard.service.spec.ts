import { TestBed } from '@angular/core/testing';

import { RoleGuard } from './role-guard.service';

describe('RoleGuardService', () => {
  let service: RoleGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RoleGuard);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
