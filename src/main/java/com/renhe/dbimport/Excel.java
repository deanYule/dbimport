package com.renhe.dbimport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.IndexedColors;

public class Excel {

	private static String[] HEADERS = { "Name", "Type", "Nullable", "Key", "Default", "Extra", "Index", "Comments" };
	private static int[] HEADER_LENGTH = { 10000, 5000, 5000, 5000, 5000, 5000, 5000, 10000 };

	private HSSFWorkbook workbook = new HSSFWorkbook();

	public void createSheet() {
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet("字段");

		this.createExcelHeader(sheet, HEADERS, HEADER_LENGTH);
		HSSFCellStyle tableHaderStyle = this.getTableHeaderStyle();

		DBMeta dbMeta = new DBMeta();
		List<Table> tables = dbMeta.getAllTableInfo();

		int rowNumber = 0;
		for (int i = 0; i < tables.size(); i++) {
			Table table = tables.get(i);
			this.createTableHeader(sheet.createRow(++rowNumber), table.getName(), tableHaderStyle);
			List<Column> columns = table.getColumns();
			for (int j = 0; j < columns.size(); j++) {
				this.createTableColumn(sheet.createRow(++rowNumber), columns.get(j));
			}
		}

		this.create("D:\\数据库设计.xls");
	}

	public void createExcelHeader(HSSFSheet sheet, String[] headers, int[] headerLength) {
		for (int ix = 0; ix < headerLength.length; ix++) {
			sheet.setColumnWidth(ix, headerLength[ix]);
		}
		
		HSSFRow row = sheet.createRow(0);
		for (int ix = 0; ix < headers.length; ix++) {
			HSSFCell cell = row.createCell(ix);
			cell.setCellValue(headers[ix]);
		}
	}

	public void createTableHeader(HSSFRow row, String tableName, HSSFCellStyle style) {
		HSSFCell tableNameCell = row.createCell(0);
		tableNameCell.setCellValue(tableName);
		row.setRowStyle(style);
		tableNameCell.setCellStyle(style);
	}

	public void createTableColumn(HSSFRow row, Column column) {
		HSSFCell nameCell = row.createCell(0);
		HSSFCell typeCell = row.createCell(1);
		HSSFCell nullableCell = row.createCell(2);
		HSSFCell keyCell = row.createCell(3);
		HSSFCell defaultCell = row.createCell(4);
		HSSFCell extraCell = row.createCell(5);
		HSSFCell indexCell = row.createCell(6);
		HSSFCell commentCell = row.createCell(7);

		nameCell.setCellValue(column.getCode());
		typeCell.setCellValue(column.getDataType() + "(" + column.getDataLength() + ")");
		nullableCell.setCellValue(column.getMandatory());
		commentCell.setCellValue(column.getComment());
	}

	public HSSFCellStyle getTableHeaderStyle() {
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFillBackgroundColor(HSSFColor.YELLOW.index2);
		style.setFillForegroundColor(HSSFColor.YELLOW.index2);
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		return style;
	}

	public void create(String path) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(new File(path));
			workbook.write(fos);
		} catch (IOException e) {

		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}
	}
}
