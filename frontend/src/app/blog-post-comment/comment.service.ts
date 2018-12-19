import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {CreateBlogPostComment} from './dto/create-blog-post-comment';
import {Observable} from 'rxjs';
import {BlogPostComment} from './dto/blog-post-comment';

const httpOptions = {
  headers: new HttpHeaders(
    {'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private commentUrl = '/api/comment';

  constructor(private http: HttpClient) {
  }

  createBlogPostComment(blogId, postId, createBlogPostComment: CreateBlogPostComment): Observable<CreateBlogPostComment> {
    return this.http.post<CreateBlogPostComment>(this.commentUrl + '/blog/' + blogId + '/post/' + postId,
      createBlogPostComment, httpOptions);
  }

  getBlogPostComments(blogId, postId): Observable<BlogPostComment[]> {
    return this.http.get<BlogPostComment[]>(this.commentUrl + '/blog/' + blogId + '/post/' + postId);
  }
}
