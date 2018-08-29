import { NgModule } from '@angular/core';

import { JobseekerSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [JobseekerSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [JobseekerSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class JobseekerSharedCommonModule {}
