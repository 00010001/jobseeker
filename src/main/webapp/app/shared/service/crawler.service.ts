import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { SearchQuery } from 'app/shared/model/search-query.model';
import { ISearchQuery } from 'app/shared/model/search-query.model';
import { map } from 'rxjs/operators';

type EntityResponseType = HttpResponse<ISearchQuery>;
type EntityArrayResponseType = HttpResponse<ISearchQuery[]>;

@Injectable({
  providedIn: 'root'
})
export class CrawlerService {
  private resourceUrl = SERVER_API_URL + 'api/crawler/jobs';

  constructor(private http: HttpClient) { }

  find(stanowisko: string, miejscowosc: string) {
    return this.http.get(`${this.resourceUrl}/${stanowisko}/${miejscowosc}`);
  }
}
