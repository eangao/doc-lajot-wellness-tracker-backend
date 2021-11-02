import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IHealthConcern, HealthConcern } from '../health-concern.model';
import { HealthConcernService } from '../service/health-concern.service';

@Component({
  selector: 'jhi-health-concern-update',
  templateUrl: './health-concern-update.component.html',
})
export class HealthConcernUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
  });

  constructor(protected healthConcernService: HealthConcernService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ healthConcern }) => {
      this.updateForm(healthConcern);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const healthConcern = this.createFromForm();
    if (healthConcern.id !== undefined) {
      this.subscribeToSaveResponse(this.healthConcernService.update(healthConcern));
    } else {
      this.subscribeToSaveResponse(this.healthConcernService.create(healthConcern));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHealthConcern>>): void {
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

  protected updateForm(healthConcern: IHealthConcern): void {
    this.editForm.patchValue({
      id: healthConcern.id,
      name: healthConcern.name,
    });
  }

  protected createFromForm(): IHealthConcern {
    return {
      ...new HealthConcern(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
