package com.example.xlSheet.controller;

import java.io.ByteArrayInputStream;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.xlSheet.service.FileRead;

@CrossOrigin(origins = "*")
@RestController
public class SheetController {
	@Autowired
	FileRead fileRead;
	
	@PostMapping("/upload")
	public ResponseEntity<List<List<Object>>> uploadFile(@RequestBody byte[] file){
		
		ByteArrayInputStream readFile=new ByteArrayInputStream(file);
		List<List<Object>> result=fileRead.XlToList(readFile);
		
		return new ResponseEntity<List<List<Object>>>(result,HttpStatus.OK);
	}
	@PostMapping("/append")
	public ResponseEntity<List<List<Object>>> appendFile(@RequestBody List<List<Object>> file){
		List<List<Object>> result=fileRead.ListToXl(file);
		
		return new ResponseEntity<List<List<Object>>>(result,HttpStatus.OK);
	}
	@GetMapping("/act")
	public ResponseEntity<Object> health(){
		
		RestTemplate restTemplate = new RestTemplate();
	    ResponseEntity<Object> response = restTemplate.exchange("http://localhost:1212/actuator/health", HttpMethod.GET, null,Object.class);

	    if (response.getStatusCode().is2xxSuccessful()) {
	        return new ResponseEntity<Object> (response.getBody(),HttpStatus.OK);
	    } else {
	    	return new ResponseEntity<Object> (response.getBody(),HttpStatus.BAD_REQUEST);
	    }
	    
	}
}
