jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IHealthConcern, HealthConcern } from '../health-concern.model';
import { HealthConcernService } from '../service/health-concern.service';

import { HealthConcernRoutingResolveService } from './health-concern-routing-resolve.service';

describe('HealthConcern routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: HealthConcernRoutingResolveService;
  let service: HealthConcernService;
  let resultHealthConcern: IHealthConcern | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(HealthConcernRoutingResolveService);
    service = TestBed.inject(HealthConcernService);
    resultHealthConcern = undefined;
  });

  describe('resolve', () => {
    it('should return IHealthConcern returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultHealthConcern = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultHealthConcern).toEqual({ id: 123 });
    });

    it('should return new IHealthConcern if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultHealthConcern = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultHealthConcern).toEqual(new HealthConcern());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as HealthConcern })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultHealthConcern = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultHealthConcern).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
