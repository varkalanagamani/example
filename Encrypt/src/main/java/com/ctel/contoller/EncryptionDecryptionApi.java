package com.ctel.contoller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ctel.util.EncryptDecryptHelper;

@RestController

public class EncryptionDecryptionApi {

	private Logger log = LoggerFactory.getLogger(getClass());

	@PostMapping("encrypt")
	public ResponseEntity<?> encryptData(@RequestBody String data) {

		log.info("entered into controller");

		EncryptDecryptHelper encryptDecryptHelper = new EncryptDecryptHelper();

		String encrptData = encryptDecryptHelper.encrypt("varkalanagamani@", "nagamanivarkala@", data);

		System.out.println(" encrptData ===>>>" + encrptData);

		return ResponseEntity.status(HttpStatus.OK).body(encrptData);
	}

	@PostMapping("decrypt")
	public ResponseEntity<?> decryptData(@RequestBody Map<String, String> data) {
		System.out.println("data : "+data);

		EncryptDecryptHelper encryptDecryptHelper = new EncryptDecryptHelper();

		String decrptData = encryptDecryptHelper.decrypt("varkalanagamani@", data.get("dataKey"));

		System.out.println(" decrptData ===>>>" + decrptData);

		return ResponseEntity.status(HttpStatus.OK).body(decrptData);

	}

}
