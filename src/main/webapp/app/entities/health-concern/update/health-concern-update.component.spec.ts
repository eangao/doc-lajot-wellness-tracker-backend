jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { HealthConcernService } from '../service/health-concern.service';
import { IHealthConcern, HealthConcern } from '../health-concern.model';

import { HealthConcernUpdateComponent } from './health-concern-update.component';

describe('HealthConcern Management Update Component', () => {
  let comp: HealthConcernUpdateComponent;
  let fixture: ComponentFixture<HealthConcernUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let healthConcernService: HealthConcernService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [HealthConcernUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(HealthConcernUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HealthConcernUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    healthConcernService = TestBed.inject(HealthConcernService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const healthConcern: IHealthConcern = { id: 456 };

      activatedRoute.data = of({ healthConcern });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(healthConcern));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<HealthConcern>>();
      const healthConcern = { id: 123 };
      jest.spyOn(healthConcernService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ healthConcern });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: healthConcern }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(healthConcernService.update).toHaveBeenCalledWith(healthConcern);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<HealthConcern>>();
      const healthConcern = new HealthConcern();
      jest.spyOn(healthConcernService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ healthConcern });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: healthConcern }));
      saveSubject.complete();

      // THEN
      expect(healthConcernService.create).toHaveBeenCalledWith(healthConcern);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<HealthConcern>>();
      const healthConcern = { id: 123 };
      jest.spyOn(healthConcernService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ healthConcern });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(healthConcernService.update).toHaveBeenCalledWith(healthConcern);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
