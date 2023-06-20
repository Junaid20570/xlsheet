import { Component } from '@angular/core';
import { FileService } from './service/file.service';
import { interval } from 'rxjs';
import { saveAs } from 'file-saver';
import * as XLSX from 'xlsx';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'XL_angular';
  health : any;
  resultData : any;
  updatedData : any;
  pagination:number=1
  siNo:number=0
  constructor(private _service:FileService){}

  ngOnInit(){
    interval(1000).subscribe(x=>{
      this._service.serverInfo().subscribe(
        data=>{
          this.health = JSON.parse(JSON.stringify(data)).status
        },error=>{
          this.health='Down'
        }
      )
    })
    
  }
  change(event:any,i:number,j:number){
    this.resultData[i][j]=event.target.value;
  }
  onFileSelected(event: any){
    const file: File = event.target.files[0];
    const fileReader: FileReader = new FileReader();
    this._service.readFile(file).subscribe(
      data=>{
        this.resultData=data
      }
    )
  }

  saveFile(){
    this._service.appendFile(this.resultData).subscribe(
      data=>{
        this.updatedData=data
        alert('updated')
      }
    )
  }

  saveNdownload(){
    this.saveFile();
    const workbook = XLSX.utils.book_new();
    const worksheet = XLSX.utils.aoa_to_sheet(this.updatedData);
    XLSX.utils.book_append_sheet(workbook, worksheet, 'Sheet 1');
    const wbout = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
    const blob = new Blob([wbout], { type: 'application/octet-stream' });
    let fileName=prompt("Enter a file name")
    if(fileName!=null){
      if(fileName!=''){
        saveAs(blob, fileName+'.xlsx');
      }
      else{
        alert('file name not given')
      }
    }
  }

  getevent(page:number){
    this.pagination=page
    this.siNo=(page-1)*10
  }
  
}
