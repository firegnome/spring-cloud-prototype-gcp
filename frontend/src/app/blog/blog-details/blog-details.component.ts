import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {BlogService} from '../blog.service';
import {Blog} from '../dto/blog';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {NewPostModalComponent} from './new-post-modal/new-post-modal.component';
import {CreateBlogPost} from '../dto/create-blog-post';
import {BlogPost} from '../../blog-post/dto/blog-post';
import {AuthService} from '../../security/auth.service';

@Component({
  selector: 'app-blog-details',
  templateUrl: './blog-details.component.html',
  styleUrls: ['./blog-details.component.scss']
})
export class BlogDetailsComponent implements OnInit {

  blog: Blog;
  blogPosts: BlogPost[];

  constructor(private route: ActivatedRoute,
              private blogService: BlogService,
              private modalService: NgbModal,
              private authService: AuthService) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      const blogId = params['id'];
      if (blogId) {
        this.reload(blogId);
      }
    });
  }

  reload(blogId): void {
    this.loadBlog(blogId);
    this.loadBlogPosts(blogId);
  }

  loadBlog(blogId): void {
    this.blogService.getBlog(blogId)
      .subscribe(blog => this.blog = blog);
  }

  loadBlogPosts(blogId): void {
    this.blogService.getBlogPosts(blogId)
      .subscribe(blogPosts => this.blogPosts = blogPosts);
  }

  openNewPostModal() {
    const modalRef = this.modalService.open(NewPostModalComponent, {
      size: 'lg',
      backdrop: 'static',
      keyboard: false
    });

    modalRef.result.then((result) => {
      this.blogService.createBlogPost(this.blog.id, new CreateBlogPost(result.title, result.content))
        .subscribe(() => this.reload(this.blog.id));
    });
  }

  isBlogOwner(): boolean {
    return this.authService.isLoggedIn() && this.authService.getUserId() === this.blog.userId;
  }
}
