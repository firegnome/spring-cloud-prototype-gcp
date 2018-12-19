import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {BlogComponent} from './blog/blog.component';
import {LoginComponent} from './login/login.component';
import {BlogDetailsComponent} from './blog/blog-details/blog-details.component';
import {RegisterComponent} from './register/register.component';
import {BlogPostComponent} from './blog-post/blog-post.component';

const routes: Routes = [
  {path: '', redirectTo: '/blog', pathMatch: 'full'},
  {path: 'blog', component: BlogComponent},
  {path: 'blog/:id', component: BlogDetailsComponent},
  {path: 'blog/:blogid/post/:postid', component: BlogPostComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
