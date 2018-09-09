import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISearchQuery } from 'app/shared/model/search-query.model';

type EntityResponseType = HttpResponse<ISearchQuery>;
type EntityArrayResponseType = HttpResponse<ISearchQuery[]>;

@Injectable({ providedIn: 'root' })
export class SearchQueryService {
    private resourceUrl = SERVER_API_URL + 'api/search-queries';

    constructor(private http: HttpClient) {}

    create(searchQuery: ISearchQuery): Observable<EntityResponseType> {
        return this.http.post<ISearchQuery>(this.resourceUrl, searchQuery, { observe: 'response' });
    }

    update(searchQuery: ISearchQuery): Observable<EntityResponseType> {
        return this.http.put<ISearchQuery>(this.resourceUrl, searchQuery, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISearchQuery>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISearchQuery[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
