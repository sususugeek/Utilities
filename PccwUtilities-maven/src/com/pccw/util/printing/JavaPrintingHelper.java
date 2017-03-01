package com.pccw.util.printing;

import java.awt.print.Paper;
import java.io.OutputStream;

import javax.print.DocFlavor;
import javax.print.StreamPrintService;
import javax.print.StreamPrintServiceFactory;

public class JavaPrintingHelper {

	public static double mmToPrintingDimension(double mm) {
		return (mm / 25.4) * 72;
	}
	
	public static double inchToPrintingDimension(double inch) {
		return inch * 72;
	}
	
	public static final Paper ISO_PAPER_A0 = new BasePapermm(841, 1189);

	public static final Paper ISO_PAPER_A1 = new BasePapermm(594, 841);

	public static final Paper ISO_PAPER_A2 = new BasePapermm(420, 594);

	public static final Paper ISO_PAPER_A3 = new BasePapermm(297, 420);

	public static final Paper ISO_PAPER_A4 = new BasePapermm(210, 297);

	public static final Paper ISO_PAPER_A5 = new BasePapermm(148, 210);

	public static final Paper ISO_PAPER_A6 = new BasePapermm(105, 148);
	
	public static StreamPrintService getPostscriptPrintService(OutputStream pOutputStream) throws Exception {
		DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
		String psMimeType = DocFlavor.BYTE_ARRAY.POSTSCRIPT.getMimeType();
		StreamPrintServiceFactory[] factories = StreamPrintServiceFactory
				.lookupStreamPrintServiceFactories(flavor, psMimeType);

		if (factories.length == 0) {
			throw new Exception("No Postscript factories available!");
		}

		// Use the first service available
		return factories[0].getPrintService(pOutputStream);
	}
}
