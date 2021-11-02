import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVitalSign, VitalSign } from '../vital-sign.model';
import { VitalSignService } from '../service/vital-sign.service';

@Injectable({ providedIn: 'root' })
export class VitalSignRoutingResolveService implements Resolve<IVitalSign> {
  constructor(protected service: VitalSignService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVitalSign> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vitalSign: HttpResponse<VitalSign>) => {
          if (vitalSign.body) {
            return of(vitalSign.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new VitalSign());
  }
}
