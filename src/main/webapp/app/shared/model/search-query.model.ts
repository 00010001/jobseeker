import { IJob } from 'app/shared/model//job.model';

export interface ISearchQuery {
    id?: number;
    position?: string;
    location?: string;
    jobs?: IJob[];
}

export class SearchQuery implements ISearchQuery {
    constructor(public id?: number, public position?: string, public location?: string, public jobs?: IJob[]) {}
}
