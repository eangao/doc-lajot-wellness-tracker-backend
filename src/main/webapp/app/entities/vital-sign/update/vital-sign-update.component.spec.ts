jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VitalSignService } from '../service/vital-sign.service';
import { IVitalSign, VitalSign } from '../vital-sign.model';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { IHealthConcern } from 'app/entities/health-concern/health-concern.model';
import { HealthConcernService } from 'app/entities/health-concern/service/health-concern.service';

import { VitalSignUpdateComponent } from './vital-sign-update.component';

describe('VitalSign Management Update Component', () => {
  let comp: VitalSignUpdateComponent;
  let fixture: ComponentFixture<VitalSignUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let vitalSignService: VitalSignService;
  let appUserService: AppUserService;
  let healthConcernService: HealthConcernService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [VitalSignUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(VitalSignUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(VitalSignUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    vitalSignService = TestBed.inject(VitalSignService);
    appUserService = TestBed.inject(AppUserService);
    healthConcernService = TestBed.inject(HealthConcernService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call appUser query and add missing value', () => {
      const vitalSign: IVitalSign = { id: 456 };
      const appUser: IAppUser = { id: 83152 };
      vitalSign.appUser = appUser;

      const appUserCollection: IAppUser[] = [{ id: 88857 }];
      jest.spyOn(appUserService, 'query').mockReturnValue(of(new HttpResponse({ body: appUserCollection })));
      const expectedCollection: IAppUser[] = [appUser, ...appUserCollection];
      jest.spyOn(appUserService, 'addAppUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vitalSign });
      comp.ngOnInit();

      expect(appUserService.query).toHaveBeenCalled();
      expect(appUserService.addAppUserToCollectionIfMissing).toHaveBeenCalledWith(appUserCollection, appUser);
      expect(comp.appUsersCollection).toEqual(expectedCollection);
    });

    it('Should call HealthConcern query and add missing value', () => {
      const vitalSign: IVitalSign = { id: 456 };
      const healthConcerns: IHealthConcern[] = [{ id: 50713 }];
      vitalSign.healthConcerns = healthConcerns;

      const healthConcernCollection: IHealthConcern[] = [{ id: 9114 }];
      jest.spyOn(healthConcernService, 'query').mockReturnValue(of(new HttpResponse({ body: healthConcernCollection })));
      const additionalHealthConcerns = [...healthConcerns];
      const expectedCollection: IHealthConcern[] = [...additionalHealthConcerns, ...healthConcernCollection];
      jest.spyOn(healthConcernService, 'addHealthConcernToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ vitalSign });
      comp.ngOnInit();

      expect(healthConcernService.query).toHaveBeenCalled();
      expect(healthConcernService.addHealthConcernToCollectionIfMissing).toHaveBeenCalledWith(
        healthConcernCollection,
        ...additionalHealthConcerns
      );
      expect(comp.healthConcernsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const vitalSign: IVitalSign = { id: 456 };
      const appUser: IAppUser = { id: 13187 };
      vitalSign.appUser = appUser;
      const healthConcerns: IHealthConcern = { id: 14965 };
      vitalSign.healthConcerns = [healthConcerns];

      activatedRoute.data = of({ vitalSign });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(vitalSign));
      expect(comp.appUsersCollection).toContain(appUser);
      expect(comp.healthConcernsSharedCollection).toContain(healthConcerns);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<VitalSign>>();
      const vitalSign = { id: 123 };
      jest.spyOn(vitalSignService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vitalSign });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vitalSign }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(vitalSignService.update).toHaveBeenCalledWith(vitalSign);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<VitalSign>>();
      const vitalSign = new VitalSign();
      jest.spyOn(vitalSignService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vitalSign });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: vitalSign }));
      saveSubject.complete();

      // THEN
      expect(vitalSignService.create).toHaveBeenCalledWith(vitalSign);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<VitalSign>>();
      const vitalSign = { id: 123 };
      jest.spyOn(vitalSignService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ vitalSign });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(vitalSignService.update).toHaveBeenCalledWith(vitalSign);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackAppUserById', () => {
      it('Should return tracked AppUser primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackAppUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackHealthConcernById', () => {
      it('Should return tracked HealthConcern primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackHealthConcernById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });

  describe('Getting selected relationships', () => {
    describe('getSelectedHealthConcern', () => {
      it('Should return option if no HealthConcern is selected', () => {
        const option = { id: 123 };
        const result = comp.getSelectedHealthConcern(option);
        expect(result === option).toEqual(true);
      });

      it('Should return selected HealthConcern for according option', () => {
        const option = { id: 123 };
        const selected = { id: 123 };
        const selected2 = { id: 456 };
        const result = comp.getSelectedHealthConcern(option, [selected2, selected]);
        expect(result === selected).toEqual(true);
        expect(result === selected2).toEqual(false);
        expect(result === option).toEqual(false);
      });

      it('Should return option if this HealthConcern is not selected', () => {
        const option = { id: 123 };
        const selected = { id: 456 };
        const result = comp.getSelectedHealthConcern(option, [selected]);
        expect(result === option).toEqual(true);
        expect(result === selected).toEqual(false);
      });
    });
  });
});
