import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHealthConcern, HealthConcern } from '../health-concern.model';

import { HealthConcernService } from './health-concern.service';

describe('HealthConcern Service', () => {
  let service: HealthConcernService;
  let httpMock: HttpTestingController;
  let elemDefault: IHealthConcern;
  let expectedResult: IHealthConcern | IHealthConcern[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HealthConcernService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a HealthConcern', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new HealthConcern()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a HealthConcern', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a HealthConcern', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
        },
        new HealthConcern()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of HealthConcern', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a HealthConcern', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addHealthConcernToCollectionIfMissing', () => {
      it('should add a HealthConcern to an empty array', () => {
        const healthConcern: IHealthConcern = { id: 123 };
        expectedResult = service.addHealthConcernToCollectionIfMissing([], healthConcern);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(healthConcern);
      });

      it('should not add a HealthConcern to an array that contains it', () => {
        const healthConcern: IHealthConcern = { id: 123 };
        const healthConcernCollection: IHealthConcern[] = [
          {
            ...healthConcern,
          },
          { id: 456 },
        ];
        expectedResult = service.addHealthConcernToCollectionIfMissing(healthConcernCollection, healthConcern);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a HealthConcern to an array that doesn't contain it", () => {
        const healthConcern: IHealthConcern = { id: 123 };
        const healthConcernCollection: IHealthConcern[] = [{ id: 456 }];
        expectedResult = service.addHealthConcernToCollectionIfMissing(healthConcernCollection, healthConcern);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(healthConcern);
      });

      it('should add only unique HealthConcern to an array', () => {
        const healthConcernArray: IHealthConcern[] = [{ id: 123 }, { id: 456 }, { id: 42499 }];
        const healthConcernCollection: IHealthConcern[] = [{ id: 123 }];
        expectedResult = service.addHealthConcernToCollectionIfMissing(healthConcernCollection, ...healthConcernArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const healthConcern: IHealthConcern = { id: 123 };
        const healthConcern2: IHealthConcern = { id: 456 };
        expectedResult = service.addHealthConcernToCollectionIfMissing([], healthConcern, healthConcern2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(healthConcern);
        expect(expectedResult).toContain(healthConcern2);
      });

      it('should accept null and undefined values', () => {
        const healthConcern: IHealthConcern = { id: 123 };
        expectedResult = service.addHealthConcernToCollectionIfMissing([], null, healthConcern, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(healthConcern);
      });

      it('should return initial array if no HealthConcern is added', () => {
        const healthConcernCollection: IHealthConcern[] = [{ id: 123 }];
        expectedResult = service.addHealthConcernToCollectionIfMissing(healthConcernCollection, undefined, null);
        expect(expectedResult).toEqual(healthConcernCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
