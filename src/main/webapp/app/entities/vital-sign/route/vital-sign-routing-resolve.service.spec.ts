jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IVitalSign, VitalSign } from '../vital-sign.model';
import { VitalSignService } from '../service/vital-sign.service';

import { VitalSignRoutingResolveService } from './vital-sign-routing-resolve.service';

describe('VitalSign routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: VitalSignRoutingResolveService;
  let service: VitalSignService;
  let resultVitalSign: IVitalSign | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(VitalSignRoutingResolveService);
    service = TestBed.inject(VitalSignService);
    resultVitalSign = undefined;
  });

  describe('resolve', () => {
    it('should return IVitalSign returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVitalSign = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVitalSign).toEqual({ id: 123 });
    });

    it('should return new IVitalSign if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVitalSign = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultVitalSign).toEqual(new VitalSign());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as VitalSign })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVitalSign = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVitalSign).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
