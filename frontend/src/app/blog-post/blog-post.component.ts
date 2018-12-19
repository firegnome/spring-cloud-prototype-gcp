import {Component, OnInit} from '@angular/core';
import {BlogService} from '../blog/blog.service';
import {BlogPost} from './dto/blog-post';
import {ActivatedRoute} from '@angular/router';
import {FormBuilder, FormGroup} from '@angular/forms';
import {CreateBlogPostComment} from '../blog-post-comment/dto/create-blog-post-comment';
import {BlogPostComment} from '../blog-post-comment/dto/blog-post-comment';
import {AuthService} from '../security/auth.service';
import {CommentService} from '../blog-post-comment/comment.service';
import {StatisticService} from '../statistic/statistic.service';

@Component({
  selector: 'app-blog-post',
  templateUrl: './blog-post.component.html',
  styleUrls: ['./blog-post.component.scss']
})
export class BlogPostComponent implements OnInit {

  blogId;
  blogPost: BlogPost;
  username = 'unknown';
  headline: string;
  sections: string[];
  myForm: FormGroup;
  blogPostComments: BlogPostComment[] = [];
  blogPostVisitCount;

  constructor(private route: ActivatedRoute,
              private blogService: BlogService,
              private formBuilder: FormBuilder,
              private authService: AuthService,
              private commentService: CommentService,
              private statisticService: StatisticService) {
    this.myForm = this.formBuilder.group({
      comment: ''
    });
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.blogId = params['blogid'];
      const postId = params['postid'];
      if (this.blogId && postId) {
        this.loadBlogPost(this.blogId, postId);
        this.loadBlogPostComments(this.blogId, postId);
        this.loadBlogPostStatistics(this.blogId, postId);
      }
    });
  }

  loadBlogPost(blogId, postId): void {
    this.blogService.getBlogPost(blogId, postId)
      .subscribe(post => {
        this.blogPost = post;
        this.setContent();
        this.authService.getUsernameFromBackend(post.userId)
          .subscribe(userInfo => this.username = userInfo.username);
      });
  }

  loadBlogPostComments(blogId, postId): void {
    this.commentService.getBlogPostComments(blogId, postId)
      .subscribe(blogPostComments => {
        this.blogPostComments = blogPostComments.sort((a, b) => b.created - a.created);
        this.blogPostComments.forEach(comment => {
          this.authService.getUsernameFromBackend(comment.userId)
            .subscribe(userInfo => comment.userName = userInfo.username);
        });
      });
  }

  loadBlogPostStatistics(blogId, postId): void {
    this.statisticService.getBlogPostVisitCount(blogId, postId)
      .subscribe(visitCount => this.blogPostVisitCount = visitCount);
  }

  setContent(): void {
    const splitted = this.blogPost.content.split('\n');
    this.headline = splitted[0];
    if (splitted.length > 1) {
      this.sections = splitted.slice(1);
    }
  }

  isUserLoggedIn() {
    return this.authService.isLoggedIn();
  }

  submitForm(): void {
    this.commentService.createBlogPostComment(this.blogId, this.blogPost.id, new CreateBlogPostComment(this.myForm.value.comment))
      .subscribe(() => {
        this.myForm.reset();
        this.loadBlogPostComments(this.blogId, this.blogPost.id);
      });
  }
}
