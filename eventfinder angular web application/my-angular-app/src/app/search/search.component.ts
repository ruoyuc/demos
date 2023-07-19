import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormControl } from '@angular/forms';
import { firstValueFrom } from "rxjs";
import { catchError } from 'rxjs/operators';
import { debounceTime, tap, switchMap, finalize, distinctUntilChanged, filter } from 'rxjs/operators';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
  constructor(public http: HttpClient) { }
  // AutoComplete

  public searchControl = new FormControl();
  public load = false;
  public samples: any;
  public results: Array<any> = [0];
  //show table
  public showTable = false;

  public errOfLoc = false;

  //searchForm
  public form: any = {
    keyword: "",
    distance: "10",
    category: "",
    location: "",
    check: false,
    // lat: "",
    // lgt: ""
    //locstr: "",

    latlgt:""
  }

  //init
  //The code for autocomplete is from the following link and I made some changes to simplify it :https://www.freakyjolly.com/mat-autocomplete-with-http-api-remote-search-results/
  ngOnInit(): void {
    this.searchControl.valueChanges
      .pipe(
        filter(res => {
          return res !== null && res.length >= 1
        }),
        distinctUntilChanged(),
        debounceTime(1000),
        tap(() => {
          this.samples = [];
          this.load = true;
        }),
 
        switchMap(value => this.http.get(`/autocomplete?text=${value}`)
         
          .pipe(
            finalize(() => {
              this.load = false
            }),
          )
        )
      )
      .subscribe((data: any) => {
        this.samples = data;
      });
  }

  checkBox() {
    this.form.location = '';
  }

  Clear() {
    this.form = {
      keyword: "",
      distance: "10",
      category: "",
      location: "",
      check: false,
      latlgt: "",
    }
    this.samples = [];
    this.results = [0];
    this.errOfLoc = false;

  }

  valid() {
    return (
      (this.form.keyword !== '' && this.form.distance !== '') &&
      (this.form.check || this.form.location !== '')
    );
  }

  async locAuto() {
    let url = `https://ipinfo.io/json?token=4992363e5c1e3c`;
    await firstValueFrom(this.http.get(url)).then((res: any) => {

      this.form.latlgt = res.loc;
      console.log(this.form.latlgt);
      // console.log(this.form.check)
    })
  }



  async locIn() {
    try {
      let url = `https://maps.googleapis.com/maps/api/geocode/json?address=${this.form.location}&key=AIzaSyBnVligqwXbQJORHbdxYjg40sBXew7Q23Q`;
      await firstValueFrom(
        this.http.get(url).pipe(
          catchError((error) => {
            console.error('Error occurred:', error);
            
            throw error;
          
          })
        )
      ).then((res: any) => {
        const location = res.results[0].geometry.location;
        this.form.latlgt = `${location.lat},${location.lng}`;
        console.log(this.form.latlgt);
       
      });
    } catch (error) {
      console.error('An error occurred while fetching location data:', error);
      // Handle the error as needed
      this.errOfLoc = true;
    }
  }




  async Submit() {
    console.log(this.form.check)
    console.log("submit")
    console.log(this.valid())
    if (!this.valid()) return;
    console.log("go")

    if (!this.form.check) {
      console.log("check")
      await this.locIn();

    } else {
      console.log("auto")
      await this.locAuto();
    }
    console.log("search")
    this.results = await this.getEvents();
    this.showTable = true;
    if(this.errOfLoc){
      this.results=[];
      this.errOfLoc = false;
    }
    console.log(typeof (this.results));
    console.log(this.results);

  }

  async getEvents() {
  
    let url = `/searchEvent?Keyword=${this.form.keyword}&Distance=${this.form.distance}&Category=${this.form.category}&Location=${this.form.latlgt}`;
    console.log(url);
    const res = await firstValueFrom(this.http.get(url));
    // console.log(res)
    return Object.values(res);
  }






}


