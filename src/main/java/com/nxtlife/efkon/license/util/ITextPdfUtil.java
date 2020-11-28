package com.nxtlife.efkon.license.util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;

public interface ITextPdfUtil {

	public static void setDocumentProperties(Document document) {
		document.addAuthor("Efkon");
		document.addCreationDate();
		document.addTitle("License Report");
	}

	public static void setProjectProductDocumentProperties(Document document) {
		document.addAuthor("Efkon");
		document.addCreationDate();
		document.addTitle("Project Product Report");
	}
	
	public static Paragraph getHeading(String heading, Integer alignment, Integer fontsize, Float specingAfter) {
		Font font = FontFactory.getFont(FontFactory.COURIER, fontsize, BaseColor.BLACK);
		Phrase phrase = new Phrase(heading, font);
		Paragraph paragraph = new Paragraph(phrase);
		paragraph.setAlignment(alignment);
		paragraph.setSpacingAfter(specingAfter);
		return paragraph;
	}

}
