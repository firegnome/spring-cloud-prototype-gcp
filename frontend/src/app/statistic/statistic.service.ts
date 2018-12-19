import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StatisticService {

  private statisticUrl = '/api/statistic';

  constructor(private http: HttpClient) {
  }

  getBlogPostVisitCount(blogId, postId): Observable<number> {
    return this.http.get<number>(this.statisticUrl + '/blog/' + blogId + '/post/' + postId + '/visitcount');
  }
}
