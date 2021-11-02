import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { VitalSignDetailComponent } from './vital-sign-detail.component';

describe('VitalSign Management Detail Component', () => {
  let comp: VitalSignDetailComponent;
  let fixture: ComponentFixture<VitalSignDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [VitalSignDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ vitalSign: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(VitalSignDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(VitalSignDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load vitalSign on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.vitalSign).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
