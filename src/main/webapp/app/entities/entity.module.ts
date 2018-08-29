import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { JobseekerJobModule } from './job/job.module';
import { JobseekerSearchQueryModule } from './search-query/search-query.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        JobseekerJobModule,
        JobseekerSearchQueryModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JobseekerEntityModule {}
