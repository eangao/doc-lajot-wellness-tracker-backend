import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HealthConcernDetailComponent } from './health-concern-detail.component';

describe('HealthConcern Management Detail Component', () => {
  let comp: HealthConcernDetailComponent;
  let fixture: ComponentFixture<HealthConcernDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HealthConcernDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ healthConcern: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(HealthConcernDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HealthConcernDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load healthConcern on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.healthConcern).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
