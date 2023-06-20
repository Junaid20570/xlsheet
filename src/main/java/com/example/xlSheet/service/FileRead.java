package com.example.xlSheet.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public interface FileRead {
	
	public List<List<Object>> XlToList(ByteArrayInputStream file);
	public List<List<Object>> ListToXl(List<List<Object>> file);
	
}

