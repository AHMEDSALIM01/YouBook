import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListreservationComponent } from './listreservation.component';

describe('ListreservationComponent', () => {
  let component: ListreservationComponent;
  let fixture: ComponentFixture<ListreservationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ListreservationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListreservationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
