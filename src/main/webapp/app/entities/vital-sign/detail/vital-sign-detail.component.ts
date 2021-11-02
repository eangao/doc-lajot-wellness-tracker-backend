import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVitalSign } from '../vital-sign.model';

@Component({
  selector: 'jhi-vital-sign-detail',
  templateUrl: './vital-sign-detail.component.html',
})
export class VitalSignDetailComponent implements OnInit {
  vitalSign: IVitalSign | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vitalSign }) => {
      this.vitalSign = vitalSign;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
