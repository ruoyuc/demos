import { Component, Input, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { firstValueFrom } from "rxjs"
import { FormBuilder } from '@angular/forms'
import { Validators } from '@angular/forms'
import { MatDialog } from '@angular/material/dialog';

import { MyDialogComponent } from '../my-dialog/my-dialog.component';
@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.scss']
})
export class ResultsComponent {
  @Input() eventArr: Array<any> = [];

  images = [944, 1011, 984].map((n) => `https://picsum.photos/id/${n}/900/500`);
  public event: any;
  public artArr: Array<any> = [];
  public venue: any;

  public albumArr: Array<any> = [];
  public twitter:string = '';
  public facebook:string ='';

  // map
  public mapOptions: google.maps.MapOptions = {};
  public marker: any;

  //circle
  // color: ThemePalette = 'primary';
  // mode: ProgressSpinnerMode = 'determinate';

  color = 'warn' as const;
  mode = 'determinate' as const;

  public disCard: boolean = false;

  public isRedHeart: boolean = false;
  constructor(public http: HttpClient, public fb: FormBuilder, public dialog: MatDialog) { }

  // public storage:StorageService,

 
  public isCollapsed: Array<boolean> = [true, true, true];

  toggleCollapse(i: number): void {
    this.isCollapsed[i] = !this.isCollapsed[i];
  }

  toggleHeartColor(): void {

    if(!this.isRedHeart){
      localStorage.setItem(this.event.Id, JSON.stringify(this.event));
      alert('Event added to Favorites');
    }else{
      localStorage.removeItem(this.event.Id);
      alert('Event removed from Favorites')
    }


    this.isRedHeart = !this.isRedHeart;



  }



  async clickEvent(id: string) {
    this.artArr = [];
    this.event = {};
    this.venue = {};
    this.mapOptions = {};
    this.marker = {}
    this.albumArr = [];
    this.disCard = true;
    this.isCollapsed = [true, true, true];
    
    if(localStorage.getItem(id) != null)
    {
      this.isRedHeart = true;
    }else{
      this.isRedHeart = false;
    }
    await this.eventDetail(id);
    await this.getVenue(this.event.Venue);



    if (!this.event.isMusic) {
      this.artArr = [0];
    } else {
      for (let i = 0; i < this.event.artArray.length; i++) {
        let x: string = this.event.artArray[i];
        let artist: any = await this.spotify(x.trim());
        this.artArr.push(artist);
        let album = await this.getAlbum(artist.id);
        this.albumArr.push(album);
      }



      for (let i = 0; i < this.artArr.length; i++) {
        this.artArr[i].images = this.albumArr[i];
      }
      console.log("artists Array")
      console.log(this.artArr);
    }

  }


  openDialog(): void {
    const dialogRef = this.dialog.open(MyDialogComponent, {
      data: { latitude: Number(this.venue.Latitude), longitude: Number(this.venue.Longitude)},
      //data: { latitude: 38.9987208, longitude: -77.2538699 },
      width: '500px',
      height: '600px',
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
    });
  }

  async eventDetail(id: string) {

    //let url = `http://localhost:8080/event?id=${id}`;
    let url = `/event?id=${id}`;
    const da: any = await firstValueFrom(this.http.get(url));
    console.log('eventdetail')
    console.log(typeof (da))
    console.log(da);
    this.event = da;
    //https://twitter.com/compose/tweet?text=Check%20Taylor%20Swift%20|%20The%20Eras%20Tour%20on%20TicketMaster%21%0Ahttps://www.ticketmaster.com.au/taylor-swift-tickets/artist/1094215
    //this.twitter = `https://twitter.com/intent/tweet?text=Check%20${this.event.Name}%20on%20TicketMaster%21%0A{{${this.event.Link}}}`;
    this.twitter = `https://twitter.com/intent/tweet?url=${this.event.Link}&text=Check%20${this.event.Name}%20on%20Ticketmaster`;
    this.facebook = `https://www.facebook.com/sharer/sharer.php?u=${this.event.Link}`;

  }

  async spotify(name: string) {
    //let url = `http://localhost:8080/spotify?name=${name}`;
    let url = `/spotify?name=${name}`;
    const s: any = await firstValueFrom(this.http.get(url));
    //console.log(typeof(s))
    //console.log(s);
    return s;
    // this.artArr.push(s);
  }

  async getAlbum(id: string) {
    //let url = `http://localhost:8080/albums?id=${id}`;
    let url = `/albums?id=${id}`;
    const a: any = await firstValueFrom(this.http.get(url));
    // console.log(typeof(a))
    // console.log(a);
    return a;
    // this.albumArr.push(a);      
  }


  async getVenue(name: string) {
    //let url = `http://localhost:8080/venue?name=${name}`;
    let url = `/venue?name=${name}`;
    const d: any = await firstValueFrom(this.http.get(url));
    console.log('venue');
    console.log(typeof (d));
    console.log(d);
    this.venue = d;
  }



  Back() {
    this.disCard = false;
    this.isRedHeart = false;
  }
}
