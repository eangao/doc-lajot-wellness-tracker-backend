import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHealthConcern } from '../health-concern.model';

@Component({
  selector: 'jhi-health-concern-detail',
  templateUrl: './health-concern-detail.component.html',
})
export class HealthConcernDetailComponent implements OnInit {
  healthConcern: IHealthConcern | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ healthConcern }) => {
      this.healthConcern = healthConcern;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
