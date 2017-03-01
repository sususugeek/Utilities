package com.pccw.util.email;

import java.io.Serializable;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.pccw.util.FastByteArrayOutputStream;
import com.pccw.util.spring.SpringApplicationContext;

public class EmailUtil {
	public static void sendEmail(JavaMailSender pJavaMailSender, EmailMessage pEmail)
			throws Exception {
		if (pJavaMailSender == null) {
			pJavaMailSender = SpringApplicationContext.getBean(JavaMailSender.class);
		}
		MimeMessage message = pJavaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom(pEmail.getSender());
		if (StringUtils.isNotBlank(pEmail.getReplyTo())) {
			helper.setReplyTo(pEmail.getReplyTo());
		}
		
		String[] emails = pEmail.getReceipantTo();
		int receipantCnt = 0;
		if (ArrayUtils.isNotEmpty(emails)) {
			helper.setTo(emails);
			receipantCnt += emails.length;
		}
		
		emails = pEmail.getReceipantCc();
		if (ArrayUtils.isNotEmpty(emails)) {
			helper.setCc(emails);	
			receipantCnt += emails.length;
		}
		
		emails = pEmail.getReceipantBcc();
		if (ArrayUtils.isNotEmpty(emails)) {
			helper.setBcc(emails);	
			receipantCnt += emails.length;
		}

		if (receipantCnt <= 0) {
			throw new Exception("NO Receipant found to send");
		}
		
		helper.setSubject(pEmail.getSubject());

		helper.setText(pEmail.getContent(), pEmail.isHtml());
		
		for (Entry<String, byte[]> entry : pEmail.getAttachmentMap().entrySet()) {
			helper.addAttachment(entry.getKey(), new ByteArrayResource(entry.getValue(), entry.getKey()));
		}
		pJavaMailSender.send(message);
	}
	
	public static class EmailMessage implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2169622314009201294L;
		
		public static final int CONTENT_FORMAT_TXT = 0;
		
		public static final int CONTENT_FORMAT_HTML = 1;

		private JavaMailSender javaMailSender;
		
		private String sender;
		
		private String replyTo;
		
		private String[] receipantTo;
		
		private String[] receipantCc;

		private String[] receipantBcc;
		
		private String subject;
		
		private int contentFormat;
		
		private String content;
		
		private TreeMap<String, byte[]> attachmentMap = new TreeMap<String, byte[]>();

		public JavaMailSender getJavaMailSender() {
			return this.javaMailSender;
		}

		public void setJavaMailSender(JavaMailSender pJavaMailSender) {
			this.javaMailSender = pJavaMailSender;
		}

		public String getSender() {
			return this.sender;
		}

		public void setSender(String pSender) {
			this.sender = pSender;
		}

		public String[] getReceipantTo() {
			return this.receipantTo;
		}

		public void setReceipantTo(String pReceipantTo) {
			this.receipantTo = this.getEmailAddress(pReceipantTo);
		}

		public String[] getReceipantCc() {
			return this.receipantCc;
		}

		public void setReceipantCc(String pReceipantCc) {
			this.receipantCc = this.getEmailAddress(pReceipantCc);
		}

		public String[] getReceipantBcc() {
			return this.receipantBcc;
		}

		public void setReceipantBcc(String pReceipantBcc) {
			this.receipantBcc = this.getEmailAddress(pReceipantBcc);
		}

		public String getSubject() {
			return this.subject;
		}

		public void setSubject(String pSubject) {
			this.subject = pSubject;
		}

		public int getContentFormat() {
			return this.contentFormat;
		}

		public void setContentFormat(int pContentFormat) {
			this.contentFormat = pContentFormat;
		}

		public String getContent() {
			return this.content;
		}

		public void setContent(String pContent) {
			this.content = pContent;
			if (StringUtils.isNotBlank(pContent) 
					&& (pContent.indexOf("</") != -1 || pContent.indexOf("/>") != -1 || pContent.indexOf("<br>") != -1)) {
				this.contentFormat = CONTENT_FORMAT_HTML;
			}
		}

		public TreeMap<String, byte[]> getAttachmentMap() {
			return this.attachmentMap;
		}

		public void setReceipantTo(String[] pReceipantTo) {
			this.receipantTo = pReceipantTo;
		}

		public void setReceipantCc(String[] pReceipantCc) {
			this.receipantCc = pReceipantCc;
		}

		public void setReceipantBcc(String[] pReceipantBcc) {
			this.receipantBcc = pReceipantBcc;
		}

		public boolean isHtml() {
			return this.contentFormat == CONTENT_FORMAT_HTML;
		}
		
		public void addAttachment(String pDescription, byte[] pAttachment) throws Exception {
			byte[] attachment = pAttachment;
			if (attachment.length > 2 * 1024 * 1024) {
				FastByteArrayOutputStream baos = new FastByteArrayOutputStream();
	               
				ZipOutputStream zout = new ZipOutputStream(baos);
				zout.putNextEntry(new ZipEntry(pDescription));
				zout.write(pAttachment);
				zout.closeEntry();
				zout.flush();
				zout.close();
				
				attachment = baos.getByteArray();
				pDescription = pDescription + ".zip";
			}
			
			this.attachmentMap.put(pDescription, attachment);
		}
		
		public void send() throws Exception {
			sendEmail(this.javaMailSender, this);
		}
		
		private String[] getEmailAddress(String pEmailAddress) {
			if (StringUtils.isBlank(pEmailAddress)) {
				return new String[0];
			}
			return pEmailAddress.split(";");
		}

		public String getReplyTo() {
			return this.replyTo;
		}

		public void setReplyTo(String pReplyTo) {
			this.replyTo = pReplyTo;
		}
	}
}