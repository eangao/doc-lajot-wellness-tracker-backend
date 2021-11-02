import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VitalSignComponent } from './list/vital-sign.component';
import { VitalSignDetailComponent } from './detail/vital-sign-detail.component';
import { VitalSignUpdateComponent } from './update/vital-sign-update.component';
import { VitalSignDeleteDialogComponent } from './delete/vital-sign-delete-dialog.component';
import { VitalSignRoutingModule } from './route/vital-sign-routing.module';

@NgModule({
  imports: [SharedModule, VitalSignRoutingModule],
  declarations: [VitalSignComponent, VitalSignDetailComponent, VitalSignUpdateComponent, VitalSignDeleteDialogComponent],
  entryComponents: [VitalSignDeleteDialogComponent],
})
export class VitalSignModule {}
