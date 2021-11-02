import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHealthConcern } from '../health-concern.model';
import { HealthConcernService } from '../service/health-concern.service';

@Component({
  templateUrl: './health-concern-delete-dialog.component.html',
})
export class HealthConcernDeleteDialogComponent {
  healthConcern?: IHealthConcern;

  constructor(protected healthConcernService: HealthConcernService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.healthConcernService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
