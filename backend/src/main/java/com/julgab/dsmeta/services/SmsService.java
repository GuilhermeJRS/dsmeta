package com.julgab.dsmeta.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.julgab.dsmeta.entities.Sale;
import com.julgab.dsmeta.repositories.SaleRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SmsService {

	@Value("${twilio.sid}")
	private String twilioSid;

	@Value("${twilio.key}")
	private String twilioKey;

	@Value("${twilio.phone.from}")
	private String twilioPhoneFrom;

	@Value("${twilio.phone.to}")
	private String twilioPhoneTo;

	@Autowired
	private SaleRepository saleRepository;

	public void sendSms(Long saleId) {

		Twilio.init(twilioSid, twilioKey);

		PhoneNumber to = new PhoneNumber(twilioPhoneTo);
		PhoneNumber from = new PhoneNumber(twilioPhoneFrom);

		Sale sale = saleRepository.findById(saleId).get();

		Message message = Message.creator(to, from, "O vendedor " + sale.getSellerName() 
		                                          + " faturou " + String.format("%.2f", sale.getAmount()) 
			                                      + " em " + sale.getDate().getDayOfWeek()
			                                      + " dia " + sale.getDate().getDayOfMonth()
 			                                      + " de " + sale.getDate().getMonth() 
			                                      + " de " + sale.getDate().getYear()).create();

		System.out.println(message.getSid());
	}
}