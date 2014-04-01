package at.knowcenter.code;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;

import at.knowcenter.code.api.service.EnrichmentServiceResponse;
import at.knowcenter.code.api.service.HttpUtils;
import at.knowcenter.code.api.toc.Figure;
import at.knowcenter.code.api.toc.Section;
import at.knowcenter.code.api.toc.StructureElement;
import at.knowcenter.code.api.toc.Table;
import at.knowcenter.code.api.toc.TableOfContents;

/**
 * Usage: java -jar code-test.jar pdf-file</p>
 * 
 * Uses the synchronous (read: demo purposes only!) CODE enrichment service
 * endpoint to upload a local PDF file, extract the main text, table of content,
 * images and figures, print these things and write a json file that encodes the
 * results.
 * 
 * @author mzechner
 * 
 */
public class PdfExtractor {
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.out.println("Usage: PdfUploader <pdf-file>");
			System.exit(-1);
		}

		// read the file, post to the service, store results in file + ".json"
		byte[] bytes = IOUtils.toByteArray(new FileInputStream(args[0]));
		long start = System.nanoTime();
		String json = HttpUtils.postHttp("http://external.know-center.tugraz.at:7070/code-server-1.0-SNAPSHOT/enrichment/uploadPdf", "application/octet-stream", bytes);
		FileUtils.write(new File(args[0] + ".json"), json);
		System.out.println("processing took: " + (System.nanoTime() - start) / 1000000000.0 + " secs");

		// convert json response to a POJO via Jackson
		EnrichmentServiceResponse response = new ObjectMapper().readValue(json, EnrichmentServiceResponse.class);

		// the extracted main text in the correct reading order
		// elements in the TOC reference this text with start/end spans
		System.out.println(response.getText());

		// sections of the table of content
		TableOfContents toc = response.getToc();
		for (StructureElement element : toc.getElements()) {
			printElement("", element);
		}

		// print the tables and figures found in the pdf
		for (StructureElement el : response.getToc().getAllElements()) {
			if (el instanceof Table) {
				Table table = (Table) el;
				System.out.println("table: " + table.getLabel());
				System.out.println(table.getData());
			}
			if (el instanceof Figure) {
				Figure figure = (Figure) el;
				System.out.println("figure: " + figure.getLabel());
				System.out.println("cached image url: " + figure.getImageUrl());
			}
		}
	}

	private static void printElement(String level, StructureElement element) {
		System.out.println(level + element.getClass().getSimpleName() + ": " + element.getLabel());
		if (element instanceof Section) {
			for (StructureElement child : ((Section) element).getChildren()) {
				printElement(level + "   ", child);
			}
		}
	}
}
