import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IVitalSign, VitalSign } from '../vital-sign.model';
import { VitalSignService } from '../service/vital-sign.service';
import { IAppUser } from 'app/entities/app-user/app-user.model';
import { AppUserService } from 'app/entities/app-user/service/app-user.service';
import { IHealthConcern } from 'app/entities/health-concern/health-concern.model';
import { HealthConcernService } from 'app/entities/health-concern/service/health-concern.service';

@Component({
  selector: 'jhi-vital-sign-update',
  templateUrl: './vital-sign-update.component.html',
})
export class VitalSignUpdateComponent implements OnInit {
  isSaving = false;

  appUsersCollection: IAppUser[] = [];
  healthConcernsSharedCollection: IHealthConcern[] = [];

  editForm = this.fb.group({
    id: [],
    weightInPounds: [],
    heightInInches: [],
    bmi: [],
    glassOfWater: [],
    systolic: [],
    diastolic: [],
    currentBloodSugar: [],
    lipidProfile: [],
    appUser: [null, Validators.required],
    healthConcerns: [null, Validators.required],
  });

  constructor(
    protected vitalSignService: VitalSignService,
    protected appUserService: AppUserService,
    protected healthConcernService: HealthConcernService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vitalSign }) => {
      this.updateForm(vitalSign);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vitalSign = this.createFromForm();
    if (vitalSign.id !== undefined) {
      this.subscribeToSaveResponse(this.vitalSignService.update(vitalSign));
    } else {
      this.subscribeToSaveResponse(this.vitalSignService.create(vitalSign));
    }
  }

  trackAppUserById(index: number, item: IAppUser): number {
    return item.id!;
  }

  trackHealthConcernById(index: number, item: IHealthConcern): number {
    return item.id!;
  }

  getSelectedHealthConcern(option: IHealthConcern, selectedVals?: IHealthConcern[]): IHealthConcern {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVitalSign>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(vitalSign: IVitalSign): void {
    this.editForm.patchValue({
      id: vitalSign.id,
      weightInPounds: vitalSign.weightInPounds,
      heightInInches: vitalSign.heightInInches,
      bmi: vitalSign.bmi,
      glassOfWater: vitalSign.glassOfWater,
      systolic: vitalSign.systolic,
      diastolic: vitalSign.diastolic,
      currentBloodSugar: vitalSign.currentBloodSugar,
      lipidProfile: vitalSign.lipidProfile,
      appUser: vitalSign.appUser,
      healthConcerns: vitalSign.healthConcerns,
    });

    this.appUsersCollection = this.appUserService.addAppUserToCollectionIfMissing(this.appUsersCollection, vitalSign.appUser);
    this.healthConcernsSharedCollection = this.healthConcernService.addHealthConcernToCollectionIfMissing(
      this.healthConcernsSharedCollection,
      ...(vitalSign.healthConcerns ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.appUserService
      .query({ 'vitalSignId.specified': 'false' })
      .pipe(map((res: HttpResponse<IAppUser[]>) => res.body ?? []))
      .pipe(
        map((appUsers: IAppUser[]) => this.appUserService.addAppUserToCollectionIfMissing(appUsers, this.editForm.get('appUser')!.value))
      )
      .subscribe((appUsers: IAppUser[]) => (this.appUsersCollection = appUsers));

    this.healthConcernService
      .query()
      .pipe(map((res: HttpResponse<IHealthConcern[]>) => res.body ?? []))
      .pipe(
        map((healthConcerns: IHealthConcern[]) =>
          this.healthConcernService.addHealthConcernToCollectionIfMissing(
            healthConcerns,
            ...(this.editForm.get('healthConcerns')!.value ?? [])
          )
        )
      )
      .subscribe((healthConcerns: IHealthConcern[]) => (this.healthConcernsSharedCollection = healthConcerns));
  }

  protected createFromForm(): IVitalSign {
    return {
      ...new VitalSign(),
      id: this.editForm.get(['id'])!.value,
      weightInPounds: this.editForm.get(['weightInPounds'])!.value,
      heightInInches: this.editForm.get(['heightInInches'])!.value,
      bmi: this.editForm.get(['bmi'])!.value,
      glassOfWater: this.editForm.get(['glassOfWater'])!.value,
      systolic: this.editForm.get(['systolic'])!.value,
      diastolic: this.editForm.get(['diastolic'])!.value,
      currentBloodSugar: this.editForm.get(['currentBloodSugar'])!.value,
      lipidProfile: this.editForm.get(['lipidProfile'])!.value,
      appUser: this.editForm.get(['appUser'])!.value,
      healthConcerns: this.editForm.get(['healthConcerns'])!.value,
    };
  }
}
