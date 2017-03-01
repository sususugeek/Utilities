package com.pccw.util.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class FtpUtil {
	
	public static FTPClient getFtpClient(String pIpAddress, String pUser, String pPassword) throws Exception {
		return getFtpClient(pIpAddress, pUser, pPassword, false);
	}

	public static FTPClient getFtpClient(String pIpAddress, String pUser, String pPassword, boolean pIsPassiveMode) throws Exception {
		return getFtpClient(pIpAddress, 21, pUser, pPassword, pIsPassiveMode);
	}

	public static FTPClient getFtpClient(String pIpAddress, int pPort, String pUser, String pPassword, boolean pIsPassiveMode) throws Exception {
		return getFtpClient(pIpAddress, pPort, pUser, pPassword, pIsPassiveMode, null);
	}
	
	public static FTPClient getFtpClient(String pIpAddress, int pPort, String pUser, String pPassword, boolean pIsPassiveMode, final StringBuffer pResponse) throws Exception {
		FTPClient ftp = new FTPClient();
		
		ftp.addProtocolCommandListener(new ProtocolCommandListener() {
			@Override
			public void protocolReplyReceived(
					ProtocolCommandEvent pParamProtocolCommandEvent) {
				if (pResponse != null) {
					pResponse.append(pParamProtocolCommandEvent.getMessage());
					pResponse.append("\n");
				}
			}
			
			@Override
			public void protocolCommandSent(
					ProtocolCommandEvent pParamProtocolCommandEvent) {
				if (pResponse != null) {
					pResponse.append(pParamProtocolCommandEvent.getMessage());
					pResponse.append("\n");
				}
			}
		}); 

		ftp.setDefaultTimeout( 60 * 1000 ); 
		ftp.setDataTimeout( 60 * 1000 );
		ftp.connect(pIpAddress, pPort);
		ftp.login(pUser, pPassword);
		if (pIsPassiveMode) {
			ftp.enterLocalPassiveMode();
		} else {
			ftp.enterLocalActiveMode();

		}
		return ftp;
	}

	public static void uploadFile(FTPClient pFtpClient, String pFilePath) throws Exception {
		UploadFile uploadFile = null;
		try {
			uploadFile = new UploadFile(pFilePath);
			uploadFile(pFtpClient, uploadFile);
		} finally {
			if (uploadFile != null) {
				uploadFile.close();
			}
		}
	}

	public static void uploadFile(FTPClient pFtpClient, InputStream pInputStream) throws Exception {
		UploadFile uploadFile = null;
		uploadFile = new UploadFile(pInputStream);
		uploadFile(pFtpClient, uploadFile);
	}
	
	public static void uploadFile(FTPClient pFtpClient, UploadFile pUploadFile) throws Exception {
		if (!pFtpClient.isConnected()) {
			throw new Exception("FTP NOT CONNECTED");
		}
		
		pFtpClient.setFileType(pUploadFile.getFileType());
		pFtpClient.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
		pFtpClient.storeFile(pUploadFile.getFileName(), pUploadFile.getInputStream());
	}

	public static void uploadFiles(FTPClient pFtpClient, String[] pFilePaths) throws Exception {
		for (String filePath : pFilePaths) {
			uploadFile(pFtpClient, filePath);
		}
	}

	public static void disconnect(FTPClient pFtpClient) throws Exception {
		if (pFtpClient == null) {
			return;
		}
		if (!pFtpClient.isConnected()) {
			return;
		}
		pFtpClient.logout();
		pFtpClient.disconnect();
	}
	
	public static class UploadFile {
		private int fileType;
		private String fileName;
		private InputStream inputStream;

		public int getFileType() {
			return this.fileType;
		}

		public void setFileType(int pFileType) {
			this.fileType = pFileType;
		}

		public String getFileName() {
			return this.fileName;
		}

		public void setFileName(String pFileName) {
			this.fileName = pFileName;
		}

		public InputStream getInputStream() {
			return this.inputStream;
		}

		public void setInputStream(InputStream pPInputStream) {
			this.inputStream = pPInputStream;
		}
		
		public UploadFile(String pFilePath) throws Exception {
			File file = new File(pFilePath);
			this.fileName = file.getName();
			this.inputStream = new FileInputStream(file);
			this.fileType = FTP.BINARY_FILE_TYPE;
			if (this.fileName != null && this.fileName.toUpperCase().contains("TXT")) {
				this.fileType = FTP.ASCII_FILE_TYPE;
			}
		}

		public UploadFile(InputStream pInputStream) throws Exception {
			this(String.valueOf((new Date()).getTime()), pInputStream);
		}
		
		public UploadFile(String pFileName, InputStream pInputStream) throws Exception {
			this.fileName = pFileName;
			this.inputStream = pInputStream;
			this.fileType = FTP.BINARY_FILE_TYPE;
			if (this.fileName != null && this.fileName.toUpperCase().contains("TXT")) {
				this.fileType = FTP.ASCII_FILE_TYPE;
			}
		}
		
		public void close() {
			if (this.inputStream != null) {
				try {
					this.inputStream.close();
				} catch (IOException ignore) { }
			}
		}
	}
}