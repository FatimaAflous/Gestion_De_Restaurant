import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionCreneauComponent } from './gestion-creneau.component';

describe('GestionCreneauComponent', () => {
  let component: GestionCreneauComponent;
  let fixture: ComponentFixture<GestionCreneauComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GestionCreneauComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GestionCreneauComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
