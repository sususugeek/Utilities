package com.pccw.util.printing.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.environment.EnvironmentUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.bomwebportal.util.FastByteArrayInputStream;

public class Pdf2PostscriptGhostscriptImpl implements Pdf2Postscript {

	private static final Logger logger = Logger.getLogger(Pdf2PostscriptGhostscriptImpl.class);

	private String gsCommand;

	private String[] cmdArguments;

	private int timeout = 120;

	private int exitCode = 0;

	@Override
	public InputStream convert(InputStream pPdfStream) throws Exception {
		File tmpPdf = null;
		File tmpPs = null;
		CommandLine cmd = new CommandLine(this.gsCommand);

		try {
			String tmpFilePrefix = java.util.UUID.randomUUID().toString();
			tmpPdf = File.createTempFile(tmpFilePrefix, "pdf");
			FileOutputStream fout = null;
			try {
				fout = new FileOutputStream(tmpPdf);
				IOUtils.copy(pPdfStream, fout);
			} catch (Exception e) {
				logger.error(ExceptionUtils.getFullStackTrace(e));
				throw e;
			} finally {
				IOUtils.closeQuietly(pPdfStream);
				IOUtils.closeQuietly(fout);
			}

			tmpPs = File.createTempFile(tmpFilePrefix, "ps");

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pdfFile", tmpPdf);
			map.put("psFile", tmpPs);

			if (ArrayUtils.isNotEmpty(this.cmdArguments)) {
				for (String arg : this.cmdArguments) {
					cmd.addArgument(StringUtils.replace(arg, "'$'", "$"));
				}
			}
			cmd.setSubstitutionMap(map);
			DefaultExecutor executor = new DefaultExecutor();
			executor.setWorkingDirectory(new File(
					tmpPdf.getAbsolutePath().substring(0, tmpPdf.getAbsolutePath().lastIndexOf(File.separator))));
			executor.setExitValue(this.exitCode);
			ExecuteWatchdog watchdog = new ExecuteWatchdog(this.timeout * 1000);
			executor.setWatchdog(watchdog);
			executor.execute(cmd, EnvironmentUtils.getProcEnvironment());

			byte[] psFile = FileUtils.readFileToByteArray(tmpPs);
			return new FastByteArrayInputStream(psFile, psFile.length);
		} catch (Exception e) {
			logger.error(ExceptionUtils.getFullStackTrace(e));
			logger.error("Command Line - " + this.gsCommand);
			logger.error("Exe Command Line - " + cmd.getExecutable());
			throw e;
		} finally {
			try {
				FileUtils.forceDelete(tmpPdf);
				FileUtils.forceDelete(tmpPs);
			} catch (Exception ignore) {
			}
		}
	}

	public String getGsCommand() {
		return this.gsCommand;
	}

	public void setGsCommand(String pGsCommand) {
		this.gsCommand = pGsCommand;
	}

	public String[] getCmdArguments() {
		return this.cmdArguments;
	}

	public void setCmdArguments(String[] pCmdArguments) {
		this.cmdArguments = pCmdArguments;
	}

	public int getTimeout() {
		return this.timeout;
	}

	public void setTimeout(int pTimeout) {
		this.timeout = pTimeout;
	}

	public int getExitCode() {
		return this.exitCode;
	}

	public void setExitCode(int pExitCode) {
		this.exitCode = pExitCode;
	}
}