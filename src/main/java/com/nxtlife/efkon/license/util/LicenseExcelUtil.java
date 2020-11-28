package com.nxtlife.efkon.license.util;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellType;

import com.nxtlife.efkon.license.ex.NotFoundException;

public interface LicenseExcelUtil {

	public static Map<String, CellType> licenseAccessIdColumns() {
		Map<String, CellType> columnTypes = new LinkedHashMap<>();
		columnTypes.put("NUMBER", CellType.NUMERIC);
		columnTypes.put("ACCESS ID", CellType.STRING);
		columnTypes.put("REMARK", CellType.STRING);
		return columnTypes;
	}

	public static Map<String, CellType> sheetColumns(String sheetName) {
		switch (sheetName) {
		case "LICENSE ACCESS ID":
			return licenseAccessIdColumns();
		default:
			throw new NotFoundException("Sheet columns not found");
		}
	}

	public static List<String> sheets() {
		return Arrays.asList("LICENSE ACCESS ID");
	}
}
