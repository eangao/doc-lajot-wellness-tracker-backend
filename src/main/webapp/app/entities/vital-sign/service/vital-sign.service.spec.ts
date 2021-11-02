import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVitalSign, VitalSign } from '../vital-sign.model';

import { VitalSignService } from './vital-sign.service';

describe('VitalSign Service', () => {
  let service: VitalSignService;
  let httpMock: HttpTestingController;
  let elemDefault: IVitalSign;
  let expectedResult: IVitalSign | IVitalSign[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VitalSignService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      weightInPounds: 0,
      heightInInches: 0,
      bmi: 0,
      glassOfWater: 0,
      systolic: 0,
      diastolic: 0,
      currentBloodSugar: 0,
      lipidProfile: 0,
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

    it('should create a VitalSign', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new VitalSign()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VitalSign', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          weightInPounds: 1,
          heightInInches: 1,
          bmi: 1,
          glassOfWater: 1,
          systolic: 1,
          diastolic: 1,
          currentBloodSugar: 1,
          lipidProfile: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VitalSign', () => {
      const patchObject = Object.assign(
        {
          heightInInches: 1,
          bmi: 1,
          diastolic: 1,
          currentBloodSugar: 1,
          lipidProfile: 1,
        },
        new VitalSign()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VitalSign', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          weightInPounds: 1,
          heightInInches: 1,
          bmi: 1,
          glassOfWater: 1,
          systolic: 1,
          diastolic: 1,
          currentBloodSugar: 1,
          lipidProfile: 1,
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

    it('should delete a VitalSign', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addVitalSignToCollectionIfMissing', () => {
      it('should add a VitalSign to an empty array', () => {
        const vitalSign: IVitalSign = { id: 123 };
        expectedResult = service.addVitalSignToCollectionIfMissing([], vitalSign);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vitalSign);
      });

      it('should not add a VitalSign to an array that contains it', () => {
        const vitalSign: IVitalSign = { id: 123 };
        const vitalSignCollection: IVitalSign[] = [
          {
            ...vitalSign,
          },
          { id: 456 },
        ];
        expectedResult = service.addVitalSignToCollectionIfMissing(vitalSignCollection, vitalSign);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VitalSign to an array that doesn't contain it", () => {
        const vitalSign: IVitalSign = { id: 123 };
        const vitalSignCollection: IVitalSign[] = [{ id: 456 }];
        expectedResult = service.addVitalSignToCollectionIfMissing(vitalSignCollection, vitalSign);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vitalSign);
      });

      it('should add only unique VitalSign to an array', () => {
        const vitalSignArray: IVitalSign[] = [{ id: 123 }, { id: 456 }, { id: 98100 }];
        const vitalSignCollection: IVitalSign[] = [{ id: 123 }];
        expectedResult = service.addVitalSignToCollectionIfMissing(vitalSignCollection, ...vitalSignArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vitalSign: IVitalSign = { id: 123 };
        const vitalSign2: IVitalSign = { id: 456 };
        expectedResult = service.addVitalSignToCollectionIfMissing([], vitalSign, vitalSign2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vitalSign);
        expect(expectedResult).toContain(vitalSign2);
      });

      it('should accept null and undefined values', () => {
        const vitalSign: IVitalSign = { id: 123 };
        expectedResult = service.addVitalSignToCollectionIfMissing([], null, vitalSign, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vitalSign);
      });

      it('should return initial array if no VitalSign is added', () => {
        const vitalSignCollection: IVitalSign[] = [{ id: 123 }];
        expectedResult = service.addVitalSignToCollectionIfMissing(vitalSignCollection, undefined, null);
        expect(expectedResult).toEqual(vitalSignCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
