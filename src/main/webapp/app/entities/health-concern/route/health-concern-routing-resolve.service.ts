import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHealthConcern, HealthConcern } from '../health-concern.model';
import { HealthConcernService } from '../service/health-concern.service';

@Injectable({ providedIn: 'root' })
export class HealthConcernRoutingResolveService implements Resolve<IHealthConcern> {
  constructor(protected service: HealthConcernService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHealthConcern> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((healthConcern: HttpResponse<HealthConcern>) => {
          if (healthConcern.body) {
            return of(healthConcern.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new HealthConcern());
  }
}
