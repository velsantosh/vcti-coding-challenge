package com.vcti.mail.controller;

import java.nio.ByteBuffer;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vcti.mail.model.UserDetails;
import com.vcti.mail.service.MailService;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 * This class contains a Mail API developed using Spring Boot
 * 
 * @author Santosh Verma
 *
 */
@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
public class MailServiceController {

	private final Logger LOG = LoggerFactory.getLogger(MailServiceController.class);

	@Autowired
	private MailService notificationService;

	@Autowired
	private UserDetails user;

	@GetMapping(value = "/healthcheck", produces = "application/json; charset=utf-8")
	public String getHealthCheck() {
		return "{ \"isWorking\" : true }";
	}
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping("send-mail")
	public String send(@RequestBody UserDetails userDetails) {
		LOG.info("Inside send email method of MailServiceController class");
		/*
		 * Creating a User with the help of User class that we have declared. Setting
		 * the First,Last and Email address of the sender.
		 */
		LOG.debug("setting user details before sending email");
		UserDetails refUserDetails = new UserDetails();

		refUserDetails.setMailSubject(
				(userDetails.getMailSubject() != null ? userDetails.getMailSubject() : "Test Eamil from CCT Mail API"));
		refUserDetails.setUserName(userDetails.getUserName() != null ? userDetails.getUserName() : "testCCTUser");
		refUserDetails.setToEmailAddress(userDetails.getToEmailAddress() != null ? userDetails.getToEmailAddress()
				: "santosh.verma.vcti@gmail.com");
		refUserDetails.setMessageText(userDetails.getMessageText() != null ? userDetails.getMessageText()
				: "Welcome email from VCTI CCT Tool");

		LOG.debug("user details has been set successfully");
		/*
		 * Here we will call sendEmail() for Sending mail to the sender.
		 */
		try {
			LOG.debug("Calling sendEmail method of MailService");
			notificationService.sendEmail(refUserDetails);
			LOG.debug("Congratulations! Your mail has been send to the user.");
		} catch (MailException mailException) {
			LOG.error("Exception while sending email : {} ", mailException.getMessage());
		}

		return "Congratulations! Your mail has been send to the user successfully.";
	}

	/**
	 * 
	 * @return
	 * @throws MessagingException
	 */
	@RequestMapping("send-mail-with-attachment")
	public String sendWithAttachment(@RequestBody UserDetails userDetails) throws MessagingException {
		LOG.info("Inside send email method of MailServiceController class");
		/*
		 * Creating a User with the help of User class that we have declared. Setting
		 * the First,Last and Email address of the sender.
		 */
		LOG.debug("setting user details before sending email");
		UserDetails refUserDetails = new UserDetails();

		refUserDetails.setMailSubject(
				(userDetails.getMailSubject() != null ? userDetails.getMailSubject() : "Test Eamil from CCT Mail API"));
		refUserDetails.setUserName(userDetails.getUserName() != null ? userDetails.getUserName() : "testCCTUser");
		refUserDetails.setToEmailAddress(userDetails.getToEmailAddress() != null ? userDetails.getToEmailAddress()
				: "santosh.verma.vcti@gmail.com");
		refUserDetails.setMessageText(userDetails.getMessageText() != null ? userDetails.getMessageText()
				: "Welcome email from VCTI CCT Tool");

		LOG.debug("user details has been set successfully");
		/*
		 * Here we will call sendEmail() for Sending mail to the sender.
		 */
		try {
			LOG.debug("Calling sendEmailWithAttachment method of MailService");
			notificationService.sendEmailWithAttachment(refUserDetails);
			LOG.debug("Congratulations! Your mail with attachment has been send to the user.");
		} catch (MailException mailException) {
			LOG.error("Exception while sending email : {} ", mailException.getMessage());
		}

		return "Congratulations! Your mail with attachment has been send to the user successfully.";
	}
	
	@RequestMapping("send/mail/with/dynamic/attachment")
	public String sendWithDynamicAttachment(@RequestBody UserDetails userDetails) throws MessagingException {
		LOG.info("Inside send email method of MailServiceController class");
		/*
		 * Creating a User with the help of User class that we have declared. Setting
		 * the First,Last and Email address of the sender.
		 */
		LOG.debug("setting user details before sending email");
		UserDetails refUserDetails = new UserDetails();

		refUserDetails.setMailSubject(
				(userDetails.getMailSubject() != null ? userDetails.getMailSubject() : "Test Eamil from CCT Mail API"));
		refUserDetails.setUserName(userDetails.getUserName() != null ? userDetails.getUserName() : "testCCTUser");
		refUserDetails.setToEmailAddress(userDetails.getToEmailAddress() != null ? userDetails.getToEmailAddress()
				: "santosh.verma.vcti@gmail.com");
		refUserDetails.setMessageText(userDetails.getMessageText() != null ? userDetails.getMessageText()
				: "Welcome email from VCTI CCT Tool");
		refUserDetails.setAttachement(userDetails.getAttachement());
		
		refUserDetails.setCandidateName(userDetails.getCandidateName());
		LOG.debug("user details has been set successfully");
		/*
		 * Here we will call sendEmail() for Sending mail to the sender.
		 */
		try {
			LOG.debug("Calling sendEmailWithAttachment method of MailService");
			notificationService.sendEmailWithDynamicAttachment(refUserDetails);
			LOG.debug("Congratulations! Your mail with attachment has been send to the user.");
		} catch (MailException mailException) {
			LOG.error("Exception while sending email : {} ", mailException.getMessage());
		}

		return "Congratulations! Your mail with attachment has been send to the user successfully.";
	}
}
