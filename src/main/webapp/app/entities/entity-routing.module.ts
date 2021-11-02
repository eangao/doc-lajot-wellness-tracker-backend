import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'app-user',
        data: { pageTitle: 'doclajotwellnesstrackerbackendApp.appUser.home.title' },
        loadChildren: () => import('./app-user/app-user.module').then(m => m.AppUserModule),
      },
      {
        path: 'health-concern',
        data: { pageTitle: 'doclajotwellnesstrackerbackendApp.healthConcern.home.title' },
        loadChildren: () => import('./health-concern/health-concern.module').then(m => m.HealthConcernModule),
      },
      {
        path: 'vital-sign',
        data: { pageTitle: 'doclajotwellnesstrackerbackendApp.vitalSign.home.title' },
        loadChildren: () => import('./vital-sign/vital-sign.module').then(m => m.VitalSignModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
