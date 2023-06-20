package com.example.xlSheet.service;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class ServiceImpl implements FileRead {

	List<Object> data;
	public List<List<Object>> XlToList(ByteArrayInputStream file) {
		
		try{
			Workbook workbook = new XSSFWorkbook(file);
			Sheet sheet =  workbook.getSheetAt(0);
			List<List<Object>> rows= new ArrayList<>(); 
			for (Row row : sheet) {
				data=new ArrayList<>();
                for (Cell cell : row) {
                	CellType cellType = cell.getCellType();
                	if (cellType == CellType.FORMULA){
                    	FormulaEvaluator evaluator= workbook.getCreationHelper().createFormulaEvaluator();
                    	CellValue value = evaluator.evaluate(cell);
                    	CellType type= value.getCellType();
                    	CheckType(type, cell);
                    }
                	else {
                		
                    	CheckType(cellType,cell);
                    }
                }
                rows.add(data); 
            }
			
            System.out.println("File processed successfully.");
            workbook.close();
            file.close();
            return rows;
        } catch (IOException e) {
            e.printStackTrace();
			
		} catch (EncryptedDocumentException e1) {
			e1.printStackTrace();
		}
		return null;
		}
	
	public List<List<Object>> ListToXl(List<List<Object>> file){
			
			List<List<Object>> data =  new ArrayList<>();
			
			try (Workbook workbook = new XSSFWorkbook()) {
	            Sheet sheet = workbook.createSheet("Sheet1");

	            int rowNum = 0;
	            for (List<Object> row : file) {
	            	List<Object> result = new ArrayList<>();
	                Row excelRow = sheet.createRow(rowNum++);
	                int colNum = 0;
	                for (Object cellData : row) {
	                    Cell cell = excelRow.createCell(colNum++);
	                    if (cellData != null) {
	                        if (cellData instanceof String) {
	                            cell.setCellValue((String) cellData);
	                            result.add(cell.getStringCellValue());
	                        } else if (cellData instanceof Number) {
	                            cell.setCellValue(((Number) cellData).doubleValue());
	                            result.add(cell.getNumericCellValue());
	                        } else if (cellData instanceof Boolean) {
	                            cell.setCellValue((Boolean) cellData);
	                            result.add(cell.getBooleanCellValue());
	                        } else {
	                            cell.setCellValue(cellData.toString());
	                            result.add(cell.getStringCellValue());
	                        }
	                    }
	                }
	                data.add(result);
	            }
//	            try (FileOutputStream fileOut = new FileOutputStream("./output.xlsx")) {
//	                workbook.write(fileOut);
//	            }
	            return data;
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
			return null;
		}
	
	public void CheckType(CellType type,Cell cell) {
		
		if (type == CellType.STRING) {
        	data.add(cell.getStringCellValue());
        	
        } if (type == CellType.NUMERIC) {
        	if (DateUtil.isCellDateFormatted(cell)) {
        		data.add(cell.getDateCellValue());
        	}
        	data.add(cell.getNumericCellValue());
        	
        } if (type == CellType.BOOLEAN) {
        	data.add(cell.getBooleanCellValue());
        	
        } if (type == CellType.BLANK) {
        	data.add(null);
        }
	}
}
