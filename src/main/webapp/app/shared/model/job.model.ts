import { ISearchQuery } from 'app/shared/model//search-query.model';

export interface IJob {
    id?: number;
    url?: string;
    title?: string;
    searchQueries?: ISearchQuery[];
}

export class Job implements IJob {
    constructor(public id?: number, public url?: string, public title?: string, public searchQueries?: ISearchQuery[]) {}
}
