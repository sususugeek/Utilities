package com.pccw.util.printing.pdf;

import java.io.InputStream;

public interface Pdf2Postscript {
	public InputStream convert(InputStream pPdfStream) throws Exception;
}
