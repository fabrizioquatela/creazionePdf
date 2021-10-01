package it.cybsec.formazione.creazionePdf.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.stream.Stream;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import it.cybsec.formazione.creazionePdf.utility.SharedConstants;

@RestController
@RequestMapping(SharedConstants.PDF_CONTROLLER)
public class PdfController {

	/********************
	 * Table Settings
	 ********************/
	private final static int NUMBER_OF_COLUMNS = 3;
	private final static String COLUMN_1 = "COGNOME";
	private final static String COLUMN_2 = "NOME";
	private final static String COLUMN_3 = "RUOLO";

	/********************
	 * Fonts
	 ********************/
	private final static Font HEADER_FONT = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
	private final static Font FONT_COURIER = FontFactory.getFont(FontFactory.COURIER, 12, BaseColor.BLACK);
	private final static Font FONT_COURIER_BOLD = FontFactory.getFont(FontFactory.COURIER_BOLD, 12, BaseColor.BLACK);

	/********************
	 * API Begin
	 ********************/

	@GetMapping(value = SharedConstants.PDF_CONTROLLER_CREATE)
	public ResponseEntity<InputStreamResource> create() throws DocumentException, URISyntaxException, IOException {

		ByteArrayOutputStream file = null;
		file = new ByteArrayOutputStream();
		Document document = new Document();
		PdfWriter.getInstance(document, file);
		document.open();
		PdfPTable table = new PdfPTable(NUMBER_OF_COLUMNS);
		addTableHeader(table);
		addRows(table);
		// addCustomRows(table);
		document.add(table);
		document.close();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline; filename = example.pdf");
		ByteArrayInputStream pdf = new ByteArrayInputStream(file.toByteArray());
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
				.body(new InputStreamResource(pdf));

	}

	/********************
	 * API End
	 ********************/

	/********************
	 * Private Methods
	 ********************/

	private void addTableHeader(PdfPTable table) {

		Stream.of(COLUMN_1, COLUMN_2, COLUMN_3).forEach(columnTitle -> {
			PdfPCell header = new PdfPCell();
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			header.setBorderWidth(1);
			header.setPhrase(new Phrase(columnTitle, HEADER_FONT));
			table.addCell(header);
		});

	}

	private void addRows(PdfPTable table) {

		table.addCell(new Phrase("Rossi", FONT_COURIER_BOLD));
		table.addCell(new Phrase("Mario", FONT_COURIER));
		table.addCell(new Phrase("IT Developer", FONT_COURIER));

	}
	/*
	 * private void addCustomRows(PdfPTable table) throws URISyntaxException,
	 * BadElementException, IOException {
	 * 
	 * Path path =
	 * Paths.get(ClassLoader.getSystemResource("Java_logo.png").toURI()); Image img
	 * = Image.getInstance(path.toAbsolutePath().toString()); img.scalePercent(10);
	 * 
	 * PdfPCell imageCell = new PdfPCell(img); table.addCell(imageCell);
	 * 
	 * PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
	 * horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
	 * table.addCell(horizontalAlignCell);
	 * 
	 * PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
	 * verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
	 * table.addCell(verticalAlignCell);
	 * 
	 * }
	 */
}
