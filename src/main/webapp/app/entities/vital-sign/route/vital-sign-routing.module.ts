import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VitalSignComponent } from '../list/vital-sign.component';
import { VitalSignDetailComponent } from '../detail/vital-sign-detail.component';
import { VitalSignUpdateComponent } from '../update/vital-sign-update.component';
import { VitalSignRoutingResolveService } from './vital-sign-routing-resolve.service';

const vitalSignRoute: Routes = [
  {
    path: '',
    component: VitalSignComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VitalSignDetailComponent,
    resolve: {
      vitalSign: VitalSignRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VitalSignUpdateComponent,
    resolve: {
      vitalSign: VitalSignRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VitalSignUpdateComponent,
    resolve: {
      vitalSign: VitalSignRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vitalSignRoute)],
  exports: [RouterModule],
})
export class VitalSignRoutingModule {}
