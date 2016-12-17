package cn.itcast.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class TestPOI2Excel {

	@Test
	public void testWrite03Excel() throws Exception{
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("hello world");
		HSSFRow row = sheet.createRow(3);
		HSSFCell cell = row.createCell(3);
		cell.setCellValue("Hello World");
		
		FileOutputStream outputStream = new FileOutputStream("D:\\itcast\\测试.xls");
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	@Test
	public void testWrite07Excel() throws Exception{
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("hello world");
		XSSFRow row = sheet.createRow(3);
		XSSFCell cell = row.createCell(3);
		cell.setCellValue("Hello World");
		
		FileOutputStream outputStream = new FileOutputStream("D:\\itcast\\测试.xlsx");
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}

	
	@Test
	public void testRead03Excle() throws Exception{
		FileInputStream inputStream = new FileInputStream("D:\\itcast\\测试.xls");
		HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
		HSSFSheet sheet = workbook.getSheetAt(0);
		HSSFRow row = sheet.getRow(3);
		HSSFCell cell = row.getCell(3);
		
		System.out.println("测试表格第四行第四列数据：" + cell.getStringCellValue());
		
		workbook.close();
		inputStream.close();
	}

	@Test
	public void testRead07Excle() throws Exception{
		FileInputStream inputStream = new FileInputStream("D:\\itcast\\测试.xlsx");
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow row = sheet.getRow(3);
		XSSFCell cell = row.getCell(3);
		
		System.out.println("测试表格第四行第四列数据：" + cell.getStringCellValue());
		
		workbook.close();
		inputStream.close();
	}

	@Test
	public void testRead02And07Excel() throws Exception{
		String fileName = "D:\\itcast\\测试.xls";
		if( fileName.matches("^.+\\.(?i)((xls)|(xlsx))$") ){
			boolean is03Excel = fileName.matches("^.+\\.(?i)(xls)$");
			FileInputStream inputStream = new FileInputStream(fileName);
			Workbook workbook = is03Excel ? new HSSFWorkbook(inputStream) : new XSSFWorkbook(inputStream);
			Sheet sheet = workbook.getSheetAt(0);
			Row row = sheet.getRow(3);
			Cell cell = row.getCell(3);
			
			System.out.println(cell.getStringCellValue());
			
			workbook.close();
			inputStream.close();
		}
	}
	
	@Test
	public void testExcelStyle() throws Exception{
		HSSFWorkbook workbook = new HSSFWorkbook();
		CellRangeAddress cellRangAddress = new CellRangeAddress(2, 2, 3, 5);
		
		HSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		HSSFFont font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 16);
		
		style.setFont(font);
		
		style.setFillPattern(HSSFCellStyle.DIAMONDS);
//		style.setFillBackgroundColor(HSSFColor.YELLOW.index);
		style.setFillForegroundColor(HSSFColor.RED.index);
		
		HSSFSheet sheet = workbook.createSheet("Hello World");
		sheet.addMergedRegion(cellRangAddress);
		HSSFRow row = sheet.createRow(2);
		HSSFCell cell = row.createCell(3);
		cell.setCellStyle(style);
		cell.setCellValue("测试");
		FileOutputStream outputStream = new FileOutputStream("D:\\itcast\\测试.xls");
		workbook.write(outputStream);
		workbook.close();
		outputStream.close();
	}
	
	
}
