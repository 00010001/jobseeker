import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JobseekerSharedModule } from 'app/shared';
import { HOME_ROUTE, HomeComponent } from './';

import { SearchboxComponent } from '../layouts/searchbox/searchbox.component';

@NgModule({
    imports: [JobseekerSharedModule, RouterModule.forChild([HOME_ROUTE])],
    declarations: [HomeComponent, SearchboxComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JobseekerHomeModule {}
