import {Component, OnInit} from '@angular/core';
import {BlogService} from './blog.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {NewBlogModalComponent} from './new-blog-modal/new-blog-modal.component';
import {CreateBlog} from './dto/create-blog';
import {Blog} from './dto/blog';
import {AuthService} from '../security/auth.service';

@Component({
  selector: 'app-blog',
  templateUrl: './blog.component.html',
  styleUrls: ['./blog.component.scss']
})
export class BlogComponent implements OnInit {
  blogs: Blog[];

  constructor(private blogService: BlogService, private modalService: NgbModal, private authService: AuthService) {
  }

  ngOnInit() {
    this.getBlogs();
  }

  getBlogs(): void {
    this.blogService.getBlogs()
      .subscribe(blogs => this.blogs = blogs);
  }

  openNewBlogModal() {
    const modalRef = this.modalService.open(NewBlogModalComponent);

    modalRef.result.then((result) => {
      this.createBlog(new CreateBlog(result.name, result.description));
    });
  }

  createBlog(createBlog: CreateBlog): void {
    this.blogService.createBlog(createBlog)
      .subscribe(result => this.getBlogs());
  }

  isUserLoggedIn() {
    return this.authService.isLoggedIn();
  }
}
