import { Component, OnInit } from '@angular/core';
import { CrawlerService } from 'app/shared/service/crawler.service';
import {Observable} from 'rxjs/Rx';

@Component({
  selector: 'jhi-searchbox',
  templateUrl: './searchbox.component.html',
  styles: []
})
export class SearchboxComponent implements OnInit {
  resp;

  constructor(private crawlerService: CrawlerService) { }

  ngOnInit() {
  }

  search(stanowisko, miejscowosc, pracodawca) {
    console.log(stanowisko, miejscowosc, pracodawca);
    this.crawlerService.find(stanowisko, miejscowosc).subscribe(
      data => {
        console.log(data);
        this.resp = data;
      },
      error => {
        console.error('Error');
        return Observable.throw(error);
      }
   );
  }

}
