import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HealthConcernComponent } from '../list/health-concern.component';
import { HealthConcernDetailComponent } from '../detail/health-concern-detail.component';
import { HealthConcernUpdateComponent } from '../update/health-concern-update.component';
import { HealthConcernRoutingResolveService } from './health-concern-routing-resolve.service';

const healthConcernRoute: Routes = [
  {
    path: '',
    component: HealthConcernComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HealthConcernDetailComponent,
    resolve: {
      healthConcern: HealthConcernRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HealthConcernUpdateComponent,
    resolve: {
      healthConcern: HealthConcernRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HealthConcernUpdateComponent,
    resolve: {
      healthConcern: HealthConcernRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(healthConcernRoute)],
  exports: [RouterModule],
})
export class HealthConcernRoutingModule {}
