import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LeftBarNavigationComponent } from './left-bar-navigation.component';

describe('LeftBarNavigationComponent', () => {
  let component: LeftBarNavigationComponent;
  let fixture: ComponentFixture<LeftBarNavigationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LeftBarNavigationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(LeftBarNavigationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
