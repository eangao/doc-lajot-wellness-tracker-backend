import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IHealthConcern, getHealthConcernIdentifier } from '../health-concern.model';

export type EntityResponseType = HttpResponse<IHealthConcern>;
export type EntityArrayResponseType = HttpResponse<IHealthConcern[]>;

@Injectable({ providedIn: 'root' })
export class HealthConcernService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/health-concerns');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(healthConcern: IHealthConcern): Observable<EntityResponseType> {
    return this.http.post<IHealthConcern>(this.resourceUrl, healthConcern, { observe: 'response' });
  }

  update(healthConcern: IHealthConcern): Observable<EntityResponseType> {
    return this.http.put<IHealthConcern>(`${this.resourceUrl}/${getHealthConcernIdentifier(healthConcern) as number}`, healthConcern, {
      observe: 'response',
    });
  }

  partialUpdate(healthConcern: IHealthConcern): Observable<EntityResponseType> {
    return this.http.patch<IHealthConcern>(`${this.resourceUrl}/${getHealthConcernIdentifier(healthConcern) as number}`, healthConcern, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHealthConcern>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHealthConcern[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addHealthConcernToCollectionIfMissing(
    healthConcernCollection: IHealthConcern[],
    ...healthConcernsToCheck: (IHealthConcern | null | undefined)[]
  ): IHealthConcern[] {
    const healthConcerns: IHealthConcern[] = healthConcernsToCheck.filter(isPresent);
    if (healthConcerns.length > 0) {
      const healthConcernCollectionIdentifiers = healthConcernCollection.map(
        healthConcernItem => getHealthConcernIdentifier(healthConcernItem)!
      );
      const healthConcernsToAdd = healthConcerns.filter(healthConcernItem => {
        const healthConcernIdentifier = getHealthConcernIdentifier(healthConcernItem);
        if (healthConcernIdentifier == null || healthConcernCollectionIdentifiers.includes(healthConcernIdentifier)) {
          return false;
        }
        healthConcernCollectionIdentifiers.push(healthConcernIdentifier);
        return true;
      });
      return [...healthConcernsToAdd, ...healthConcernCollection];
    }
    return healthConcernCollection;
  }
}
