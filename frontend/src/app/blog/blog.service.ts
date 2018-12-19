import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Blog} from './dto/blog';
import {Observable} from 'rxjs';
import {CreateBlog} from './dto/create-blog';
import {CreateBlogPost} from './dto/create-blog-post';
import {BlogPost} from '../blog-post/dto/blog-post';
import {CreateBlogPostComment} from '../blog-post-comment/dto/create-blog-post-comment';
import {BlogPostComment} from '../blog-post-comment/dto/blog-post-comment';

const httpOptions = {
  headers: new HttpHeaders(
    {'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class BlogService {

  private blogUrl = '/api/blog';

  constructor(private http: HttpClient) {
  }

  getBlogs(): Observable<Blog[]> {
    return this.http.get<Blog[]>(this.blogUrl);
  }

  createBlog(createBlog: CreateBlog): Observable<CreateBlog> {
    return this.http.post<CreateBlog>(this.blogUrl, createBlog, httpOptions);
  }

  getBlog(blogId): Observable<Blog> {
    return this.http.get<Blog>(this.blogUrl + '/' + blogId);
  }

  createBlogPost(blogId, createBlogPost: CreateBlogPost): Observable<CreateBlogPost> {
    return this.http.post<CreateBlogPost>(this.blogUrl + '/' + blogId + '/post', createBlogPost, httpOptions);
  }

  getBlogPosts(blogId): Observable<BlogPost[]> {
    return this.http.get<BlogPost[]>(this.blogUrl + '/' + blogId + '/post');
  }

  getBlogPost(blogId, postId): Observable<BlogPost> {
    return this.http.get<BlogPost>(this.blogUrl + '/' + blogId + '/post/' + postId);
  }
}
