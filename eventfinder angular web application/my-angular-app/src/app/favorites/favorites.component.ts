import { Component } from '@angular/core';
interface StorageItem {
  key: string;
  value: any;
}




@Component({
  selector: 'app-favorites',
  templateUrl: './favorites.component.html',
  styleUrls: ['./favorites.component.scss']
})



export class FavoritesComponent {


  allStorageItems: StorageItem[] = [];

  constructor() {
    this.getAllLocalStorageItems();
  }

  getAllLocalStorageItems(): void {
    this.allStorageItems = [];
    for (let i = 0; i < localStorage.length; i++) {
      const key = localStorage.key(i);
      if (key) {
        const value = localStorage.getItem(key);
        const parsedValue = value ? JSON.parse(value) : null;
        this.allStorageItems.push({ key: key, value: parsedValue });
      }
    }
  }

  removeItem(key: string): void {
    localStorage.removeItem(key);
    this.allStorageItems = this.allStorageItems.filter(item => item.key !== key);
    alert("Removed from favorites!")
  }
}
