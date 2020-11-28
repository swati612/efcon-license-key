package com.nxtlife.efkon.license.util;

import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public interface PdfTableUtil {

	public static void addTableHeader(PdfPTable pdfTable, List<String> columnHeaders) {
		columnHeaders.stream().forEach(columnTitle -> {
			PdfPCell header = new PdfPCell();
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			header.setBorderWidth(1);
			header.setPhrase(new Phrase(columnTitle, FontFactory.getFont(FontFactory.TIMES_BOLD, 10f)));
			header.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfTable.addCell(header);
		});
	}

	public static void addTableRows(PdfPTable pdfTable, List<String> columnValues) {
		columnValues.stream().forEach(columnValue -> {
			PdfPCell cell = new PdfPCell(new Phrase(columnValue == null ? "NA" : columnValue.toString(),
					FontFactory.getFont(FontFactory.TIMES, 10f)));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pdfTable.addCell(cell);
		});
	}
}
