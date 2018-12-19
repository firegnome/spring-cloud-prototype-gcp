import {Component} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-new-blog-modal',
  templateUrl: './new-blog-modal.component.html',
  styleUrls: ['./new-blog-modal.component.scss']
})
export class NewBlogModalComponent {
  myForm: FormGroup;

  constructor(public activeModal: NgbActiveModal, private formBuilder: FormBuilder) {
    this.myForm = this.formBuilder.group({
      name: '',
      description: ''
    });
  }

  submitForm(): void {
    this.activeModal.close(this.myForm.value);
  }

}
