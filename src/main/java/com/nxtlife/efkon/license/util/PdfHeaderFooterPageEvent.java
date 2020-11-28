package com.nxtlife.efkon.license.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfHeaderFooterPageEvent extends PdfPageEventHelper {

	public String heading;
	public PdfPTable headerPdfTable;
	public String generatedDate;

	public PdfHeaderFooterPageEvent(String heading, PdfPTable headerPdfTable, String generatedDate) {
		this.headerPdfTable = headerPdfTable;
		this.heading = heading;
		this.generatedDate = generatedDate;
	}

	@Override
	public void onStartPage(PdfWriter writer, Document document) {
		try {
			document.add(ITextPdfUtil.getHeading(heading, Element.ALIGN_CENTER, 16, 2f));
			headerPdfTable.setWidthPercentage(95f);
			document.add(headerPdfTable);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
				new Phrase("Generated on : " + generatedDate, FontFactory.getFont(FontFactory.TIMES, 10f)), 110, 30, 0);
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
				new Phrase("page " + document.getPageNumber(), FontFactory.getFont(FontFactory.TIMES, 10f)), 550, 30,
				0);
	}
}
