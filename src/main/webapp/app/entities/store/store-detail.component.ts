import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ActivatedRouteSnapshot } from '@angular/router';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IStore } from 'app/shared/model/store.model';
import { StoreEquipmentService } from '../store-equipment/store-equipment.service';
import { IStoreEquipment, StoreEquipment } from 'app/shared/model/store-equipment.model';
import { Observable } from 'rxjs';
import { flatMap } from 'rxjs/operators';
import { StoreEquipmentDeleteDialogComponent } from '../store-equipment/store-equipment-delete-dialog.component';

@Component({
  selector: 'jhi-store-detail',
  templateUrl: './store-detail.component.html',
})
export class StoreDetailComponent implements OnInit {

  store: IStore | null = null;
  storeEquipament: IStoreEquipment | null = null;
  equipaments: IStoreEquipment[] = [];  

  constructor(
    protected activatedRoute: ActivatedRoute,
    private storeEquipmentService: StoreEquipmentService,
    protected modalService: NgbModal,
  ) { }

  ngOnInit(): void { 
    this.activatedRoute.data.subscribe(({ store }) => {
      this.store = store;
      this.storeEquipmentService.findEquipmentByStore(this.store?.id).subscribe((res: HttpResponse<IStoreEquipment[]>) => (
        this.equipaments = res.body || []
        ));
    });   
   
  }


  delete(storeEquipment: IStoreEquipment): void {
    const modalRef = this.modalService.open(StoreEquipmentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.storeEquipment = storeEquipment;
  }


  previousState(): void {
    window.history.back();
  }




}
