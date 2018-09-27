import { ISearchQuery } from 'app/shared/model//search-query.model';

export interface IJob {
    id?: number;
    url?: string;
    title?: string;
    description?: string;
    companyLogoUrl?: string;
    websiteLogoUrl?: string;
    company?: string;
    searchQueries?: ISearchQuery[];
}

export class Job implements IJob {
    constructor(
        public id?: number,
        public url?: string,
        public title?: string,
        public description?: string,
        public companyLogoUrl?: string,
        public websiteLogoUrl?: string,
        public company?: string,
        public searchQueries?: ISearchQuery[]
    ) {}
}
