package com.pccw.util.ftp;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import com.pccw.util.FastByteArrayInputStream;
import com.pccw.util.printing.pdf.Pdf2Postscript;
import com.pccw.util.spring.SpringApplicationContext;

public class HpDirectPdfPrinter {
	
	private static final Logger logger = Logger.getLogger(HpDirectPdfPrinter.class);
	
	public static void print(String pIpAddress, String pFilePath) throws Exception {
		FTPClient ftpClient = null;
		try {
			ftpClient = FtpUtil.getFtpClient(pIpAddress, "admin", "admin");
			FtpUtil.uploadFile(ftpClient, pFilePath);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw e;
		} finally {
			FtpUtil.disconnect(ftpClient);
		}
	}

	public static void print(String pIpAddress, String[] pFilePaths) throws Exception {
		for (int i = 0; pFilePaths != null && i < pFilePaths.length; i++) {
			print(pIpAddress, pFilePaths[i]);
		}
	}
	
	public static void print(String pIpAddress, InputStream pInputStream) throws Exception {
		FTPClient ftpClient = null;
		try {
			ftpClient = FtpUtil.getFtpClient(pIpAddress, "admin", "admin");
			FtpUtil.uploadFile(ftpClient, pInputStream);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw e;
		} finally {
			FtpUtil.disconnect(ftpClient);
		}
	}
	
	public static void print(String pIpAddress, InputStream[] pInputStreams) throws Exception {
		for (int i = 0; pInputStreams != null && i < pInputStreams.length; i++) {
			print(pIpAddress, pInputStreams[i]);
		}
	}

	public static void printPdf(String pIpAddress, String pPdfFile, boolean pIsPrinterSupportPdf) throws Exception {
		byte[] pdfFile = FileUtils.readFileToByteArray(new File(pPdfFile));
		if (ArrayUtils.isEmpty(pdfFile)) {
			return;
		}
		printPdf(pIpAddress, new FastByteArrayInputStream(pdfFile, pdfFile.length), pIsPrinterSupportPdf);
	}
	
	public static String getPrinterModel(String pIpAddress) throws Exception {
		FTPClient ftpClient = null;
		StringBuffer response = new StringBuffer();
		try {
			ftpClient = FtpUtil.getFtpClient(pIpAddress, 21, "admin", "admin", false, response);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw e;
		} finally {
			FtpUtil.disconnect(ftpClient);
		}
		if (response.length() <= 0) {
			return null;
		}
		String[] lines = response.toString().split("\n");
		for (String line : lines) {
			if (StringUtils.contains(line, "PORT")) {
				return StringUtils.trim(StringUtils.substring(line, 4));
			}
		}
		return null;
	}
	
	public static void printPdf(String pIpAddress, String[] pPdfFiles, boolean pIsPrinterSupportPdf) throws Exception {
		for (int i = 0; pPdfFiles != null && i < pPdfFiles.length; i++) {
			printPdf(pIpAddress, pPdfFiles[i], pIsPrinterSupportPdf);
		}
	}
	
	public static void printPdf(String pIpAddress, InputStream[] pInputStreams, boolean pIsPrinterSupportPdf) throws Exception {
		for (int i = 0; pInputStreams != null && i < pInputStreams.length; i++) {
			printPdf(pIpAddress, pInputStreams[i], pIsPrinterSupportPdf);
		}
	}
	
	public static void printPdf(String pIpAddress, InputStream pInputStream, boolean pIsPrinterSupportPdf) throws Exception {
		FTPClient ftpClient = null;
		InputStream printStream = null;
		StringBuffer response = new StringBuffer();
		try {
			printStream = (pIsPrinterSupportPdf ? pInputStream : SpringApplicationContext.getBean(Pdf2Postscript.class).convert(pInputStream));
			ftpClient = FtpUtil.getFtpClient(pIpAddress, 21, "admin", "admin", false, response);
			FtpUtil.uploadFile(ftpClient, printStream);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			throw e;
		} finally {
			FtpUtil.disconnect(ftpClient);
		}
	}
}