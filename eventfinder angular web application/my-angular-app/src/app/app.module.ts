import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';


import { SearchComponent } from './search/search.component';
import { FavoritesComponent } from './favorites/favorites.component';


import { ReactiveFormsModule ,FormsModule } from '@angular/forms';


import { HttpClientModule } from '@angular/common/http';

// import mdb-tab
import { MatTabsModule } from '@angular/material/tabs';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

// autocomplete
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { ResultsComponent } from './results/results.component';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

// import google-map
import { GoogleMapsModule } from '@angular/google-maps';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { MyDialogComponent } from './my-dialog/my-dialog.component'


@NgModule({
  declarations: [
    AppComponent,
    SearchComponent,
    FavoritesComponent,
    ResultsComponent,
    MyDialogComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatTabsModule,
    MatInputModule,
    MatIconModule,
    MatButtonModule,
    MatAutocompleteModule,
    GoogleMapsModule,
    NgbModule,
    MatProgressSpinnerModule,
    MatDialogModule, 
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
