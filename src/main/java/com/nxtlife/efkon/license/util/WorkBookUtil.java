package com.nxtlife.efkon.license.util;

import java.util.Arrays;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public interface WorkBookUtil {

	public static void createRow(CellStyle style, Sheet sheet, List<String> columnValues, Integer startRowCount) {
		Row row = sheet.createRow(startRowCount);
		int i = 0;
		for (String columnValue : columnValues) {
			row.createCell(i).setCellValue(columnValue == null ? null : columnValue.toString());
			row.getCell(i).setCellStyle(style);
			i++;
		}

	}

	public static Row createRow(Sheet sheet, List<String> columnValues, Integer startRowCount) {
		Row row = sheet.createRow(startRowCount);
		int i = 0;
		for (String columnvalue : columnValues) {
			row.createCell(i++).setCellValue(columnvalue == null ? null : columnvalue.toString());
		}

		return row;
	}

	public static CellStyle getHeaderCellStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setFontName("Arial");
		style.setFillForegroundColor(HSSFColorPredefined.AQUA.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		font.setBold(true);
		font.setColor(HSSFColorPredefined.WHITE.getIndex());
		style.setFont(font);
		return style;
	}

	public static List<String> stageColumnHeaders(String filter) {
		List<String> columnHeaders;
		columnHeaders = Arrays.asList();

		return columnHeaders;
	}

}
