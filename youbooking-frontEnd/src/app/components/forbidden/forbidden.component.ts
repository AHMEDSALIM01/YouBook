import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-forbidden',
  template: `
    <div class="forbidden-container">
      <h1>Forbidden</h1>
      <p>You do not have permission to access this page.</p>
    </div>
  `,
  styles: [`
    .forbidden-container {
      text-align: center;
      margin-top: 200px;
    }
  `]
})
export class ForbiddenComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

}
