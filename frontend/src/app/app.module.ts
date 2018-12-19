import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BlogComponent} from './blog/blog.component';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {NewBlogModalComponent} from './blog/new-blog-modal/new-blog-modal.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {BlogDetailsComponent} from './blog/blog-details/blog-details.component';
import {BlogPostComponent} from './blog-post/blog-post.component';
import {LoginComponent} from './login/login.component';
import {AuthInterceptor} from './security/auth-interceptor.service';
import {RegisterComponent} from './register/register.component';
import {NewPostModalComponent} from './blog/blog-details/new-post-modal/new-post-modal.component';

@NgModule({
  declarations: [
    AppComponent,
    BlogComponent,
    NewBlogModalComponent,
    BlogDetailsComponent,
    BlogPostComponent,
    LoginComponent,
    RegisterComponent,
    NewPostModalComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    NgbModule,
    FormsModule,
    ReactiveFormsModule
  ],
  entryComponents: [
    NewBlogModalComponent,
    NewPostModalComponent
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
