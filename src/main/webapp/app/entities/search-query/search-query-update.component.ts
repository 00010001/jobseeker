import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { ISearchQuery } from 'app/shared/model/search-query.model';
import { SearchQueryService } from './search-query.service';
import { IJob } from 'app/shared/model/job.model';
import { JobService } from 'app/entities/job';

@Component({
    selector: 'jhi-search-query-update',
    templateUrl: './search-query-update.component.html'
})
export class SearchQueryUpdateComponent implements OnInit {
    private _searchQuery: ISearchQuery;
    isSaving: boolean;

    jobs: IJob[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private searchQueryService: SearchQueryService,
        private jobService: JobService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ searchQuery }) => {
            this.searchQuery = searchQuery;
        });
        this.jobService.query().subscribe(
            (res: HttpResponse<IJob[]>) => {
                this.jobs = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.searchQuery.id !== undefined) {
            this.subscribeToSaveResponse(this.searchQueryService.update(this.searchQuery));
        } else {
            this.subscribeToSaveResponse(this.searchQueryService.create(this.searchQuery));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ISearchQuery>>) {
        result.subscribe((res: HttpResponse<ISearchQuery>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackJobById(index: number, item: IJob) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
    get searchQuery() {
        return this._searchQuery;
    }

    set searchQuery(searchQuery: ISearchQuery) {
        this._searchQuery = searchQuery;
    }
}
