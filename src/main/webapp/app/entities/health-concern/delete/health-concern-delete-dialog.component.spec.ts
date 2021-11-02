jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { HealthConcernService } from '../service/health-concern.service';

import { HealthConcernDeleteDialogComponent } from './health-concern-delete-dialog.component';

describe('HealthConcern Management Delete Component', () => {
  let comp: HealthConcernDeleteDialogComponent;
  let fixture: ComponentFixture<HealthConcernDeleteDialogComponent>;
  let service: HealthConcernService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [HealthConcernDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(HealthConcernDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HealthConcernDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(HealthConcernService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
