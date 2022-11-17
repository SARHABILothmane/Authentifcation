package com.sarhabil.demo.email;

public interface I_emailSRV {
	public boolean  sendEmail(String toEmail, String subject, String body);
	public boolean sendVerificationMail(String toEmail, String verificationCode);
	public boolean sendRestPassawordEmail(String toEmail, String resetToken);
}
