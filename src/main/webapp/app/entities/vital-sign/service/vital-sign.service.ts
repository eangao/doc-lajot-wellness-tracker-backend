import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVitalSign, getVitalSignIdentifier } from '../vital-sign.model';

export type EntityResponseType = HttpResponse<IVitalSign>;
export type EntityArrayResponseType = HttpResponse<IVitalSign[]>;

@Injectable({ providedIn: 'root' })
export class VitalSignService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vital-signs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vitalSign: IVitalSign): Observable<EntityResponseType> {
    return this.http.post<IVitalSign>(this.resourceUrl, vitalSign, { observe: 'response' });
  }

  update(vitalSign: IVitalSign): Observable<EntityResponseType> {
    return this.http.put<IVitalSign>(`${this.resourceUrl}/${getVitalSignIdentifier(vitalSign) as number}`, vitalSign, {
      observe: 'response',
    });
  }

  partialUpdate(vitalSign: IVitalSign): Observable<EntityResponseType> {
    return this.http.patch<IVitalSign>(`${this.resourceUrl}/${getVitalSignIdentifier(vitalSign) as number}`, vitalSign, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVitalSign>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVitalSign[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVitalSignToCollectionIfMissing(
    vitalSignCollection: IVitalSign[],
    ...vitalSignsToCheck: (IVitalSign | null | undefined)[]
  ): IVitalSign[] {
    const vitalSigns: IVitalSign[] = vitalSignsToCheck.filter(isPresent);
    if (vitalSigns.length > 0) {
      const vitalSignCollectionIdentifiers = vitalSignCollection.map(vitalSignItem => getVitalSignIdentifier(vitalSignItem)!);
      const vitalSignsToAdd = vitalSigns.filter(vitalSignItem => {
        const vitalSignIdentifier = getVitalSignIdentifier(vitalSignItem);
        if (vitalSignIdentifier == null || vitalSignCollectionIdentifiers.includes(vitalSignIdentifier)) {
          return false;
        }
        vitalSignCollectionIdentifiers.push(vitalSignIdentifier);
        return true;
      });
      return [...vitalSignsToAdd, ...vitalSignCollection];
    }
    return vitalSignCollection;
  }
}
