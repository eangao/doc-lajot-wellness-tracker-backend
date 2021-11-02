import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HealthConcernComponent } from './list/health-concern.component';
import { HealthConcernDetailComponent } from './detail/health-concern-detail.component';
import { HealthConcernUpdateComponent } from './update/health-concern-update.component';
import { HealthConcernDeleteDialogComponent } from './delete/health-concern-delete-dialog.component';
import { HealthConcernRoutingModule } from './route/health-concern-routing.module';

@NgModule({
  imports: [SharedModule, HealthConcernRoutingModule],
  declarations: [HealthConcernComponent, HealthConcernDetailComponent, HealthConcernUpdateComponent, HealthConcernDeleteDialogComponent],
  entryComponents: [HealthConcernDeleteDialogComponent],
})
export class HealthConcernModule {}
