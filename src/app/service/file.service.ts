import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  constructor(private _http:HttpClient) { }

  readFile(file:File){
    return this._http.post("http://localhost:1212/upload",file)
  }

  appendFile(file:ArrayBuffer){
    return this._http.post("http://localhost:1212/append",file)
  }
  //http:localhost:1212/actuator/health
  serverInfo(){
    return this._http.get("http://localhost:1212/act")
  }
}
