import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVitalSign } from '../vital-sign.model';
import { VitalSignService } from '../service/vital-sign.service';

@Component({
  templateUrl: './vital-sign-delete-dialog.component.html',
})
export class VitalSignDeleteDialogComponent {
  vitalSign?: IVitalSign;

  constructor(protected vitalSignService: VitalSignService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vitalSignService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
