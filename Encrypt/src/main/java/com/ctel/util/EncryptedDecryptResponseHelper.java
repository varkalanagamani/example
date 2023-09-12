package com.ctel.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Configuration
public class EncryptedDecryptResponseHelper {

	private Logger logger = LoggerFactory.getLogger(EncryptedDecryptResponseHelper.class);
	@Autowired
	private EncryptDecryptHelper encryptDecryptHelper;

	@Value("${AES.KEY}")
	private String key;
	@Value("${AES.IV}")
	private String iv;

	public Map<String, String> encryptedSuccessResponse(ResponseEntity<?> response, Class<?> service) {
		logger.info("in Suceess Encryption method :", service.getName());

		String encrpt = null;
		String decrpt = null;
		Map<String, String> map = new HashMap<>();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			ResponseEntity<?> responseEntity = response;
			String resp = objectMapper.writeValueAsString(responseEntity.getBody());
			encrpt = encryptDecryptHelper.encrypt(key, iv, resp);
			decrpt = encryptDecryptHelper.decrypt(key, encrpt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("userResponse", encrpt);
		return map;
	}

	public Map<String, String> encryptedErrorResponse(ResponseEntity<?> response, Class<?> service) {
		logger.info("in Error Encryption method :", service.getName());
		String encrpterror = null;
		Map<String, String> map = new HashMap<>();
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			ResponseEntity<?> responseEntity = response;
			String resp = objectMapper.writeValueAsString(responseEntity.getBody());
			encrpterror = encryptDecryptHelper.encrypt(key, iv, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("userResponse", encrpterror);

		return map;
	}

	public String decryptRequest(String decryptClass) {
		String decrptReq = null;
		decrptReq = encryptDecryptHelper.decrypt(key, decryptClass.toString());
		logger.info("in Decription  method,After Decrypting The Requested Data {}:", decrptReq);
		return decrptReq;

	}

}
