package com.ctel.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class EncryptDecryptHelper {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Value("${AES.KEY}")

	private String key;

	@Value("${AES.IV}")

	private String iv;

	private static String CIPHER_NAME = "AES/CBC/PKCS5PADDING";

	private static int CIPHER_KEY_LEN = 16; // 128 bits



	public String encrypt(String data) {

		try {

			if (key.length() < EncryptDecryptHelper.CIPHER_KEY_LEN) {

				int numPad = EncryptDecryptHelper.CIPHER_KEY_LEN - key.length();

				for (int i = 0; i < numPad; i++) {

					key += "0"; // 0 pad to len 16 bytes

				}

			} else if (key.length() > EncryptDecryptHelper.CIPHER_KEY_LEN) {

				key = key.substring(0, CIPHER_KEY_LEN); // truncate to 16 bytes

			}

			IvParameterSpec initVector = new IvParameterSpec(iv.getBytes("UTF-8"));

			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance(EncryptDecryptHelper.CIPHER_NAME);

			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, initVector);

			byte[] encryptedData = cipher.doFinal((data.getBytes()));

			String base64_EncryptedData = Base64.getEncoder().encodeToString(encryptedData);

			// return base64_EncryptedData;

			String base64_IV = Base64.getEncoder().encodeToString(iv.getBytes("UTF-8"));

			return base64_EncryptedData + ":" + base64_IV;

		} catch (Exception ex) {

			ex.printStackTrace();

		}

		return null;

	}

	public String encrypt(String key, String iv, String data) {
		log.info("emtered into encrypt method");

		try {

			System.out.println("length of key:" + key.length());

			if (key.length() < EncryptDecryptHelper.CIPHER_KEY_LEN) {
				System.out.println("in if block");

				int numPad = EncryptDecryptHelper.CIPHER_KEY_LEN - key.length();

				for (int i = 0; i < numPad; i++) {

					key += "0"; // 0 pad to len 16 bytes

				}

			} else if (key.length() > EncryptDecryptHelper.CIPHER_KEY_LEN) {
				System.out.println("in else block");

				key = key.substring(0, CIPHER_KEY_LEN); // truncate to 16 bytes

			}
			IvParameterSpec initVector = new IvParameterSpec(iv.getBytes("UTF-8"));

			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance(EncryptDecryptHelper.CIPHER_NAME);

			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, initVector);

			System.out.println(data.getBytes());

			byte[] encryptedData = cipher.doFinal((data.getBytes()));

			String base64_EncryptedData = Base64.getEncoder().encodeToString(encryptedData);

			String base64_IV = Base64.getEncoder().encodeToString(iv.getBytes("UTF-8"));

			System.out.println("base64_EncryptedData  :  " + base64_EncryptedData);
			System.out.println("base64_IV : " + base64_IV);

			return base64_EncryptedData + ":" + base64_IV;

		} catch (Exception ex) {

			ex.printStackTrace();

		}

		return null;

	}

	/**
	 * 
	 * Decrypt data using AES Cipher (CBC) with 128 bit key
	 *
	 * 
	 * 
	 * @param key  - key to use should be 16 bytes long (128 bits)
	 * 
	 * @param data - encrypted data with iv at the end separate by :
	 * 
	 * @return decrypted data string
	 * 
	 */

	public String decrypt(String key, String data) {

		try {

			String[] parts = data.split(":");

			IvParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(parts[1]));

			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance(EncryptDecryptHelper.CIPHER_NAME);

			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

			byte[] decodedEncryptedData = Base64.getDecoder().decode(parts[0]);

			byte[] original = cipher.doFinal(decodedEncryptedData);

			return new String(original);

		} catch (Exception ex) {

			ex.printStackTrace();

		}

		return null;

	}

	public static void main(String[] args) {

		EncryptDecryptHelper encryptDecryptHelper = new EncryptDecryptHelper();

		// String data="{\r\n"

		// + " \"shipmentNoteId\": 1,\r\n"

		// + " \"locCode\": 1001,\r\n"

		// + " \"loginId\": 2\r\n"

		// + "}";

		// String encrptData = encryptDecryptHelper.encrypt("BPclh@mTt$Byel89",
		// "bpcl@123IntVec$1", data);

		// System.out.println(" encrptData ===>>>" + encrptData);

		String encrptData = "1rrifPIB/KyGxreAqiQxqIY5xPD/L+SnWsrsYpEQ5eeOEYD8DSihsl9S/4pFWGczcMoEmUhBZhrURNzud5pRFQ==:Y3RlbEAxMjNJbnRWZWMkMQ==";

		String decrptData = encryptDecryptHelper.decrypt("nagamanivarkala@", encrptData);

		System.out.println(" decrptData ===>>>" + decrptData);

	}

}
