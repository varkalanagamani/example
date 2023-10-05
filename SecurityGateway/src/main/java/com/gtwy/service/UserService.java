package com.gtwy.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtwy.entity.User;
import com.gtwy.exception.GlobalException;
import com.gtwy.model.Response;
import com.gtwy.model.UserInputReq;
import com.gtwy.oauthReqRes.AuthRequest;
import com.gtwy.oauthReqRes.AuthResponse;
import com.gtwy.repo.MRolePermissionMapRepo;
import com.gtwy.repo.UserRepo;
import com.gtwy.utility.EncryptDecryptHelper;
import com.gtwy.utility.EncryptedDecryptResponseHelper;

@Service
public class UserService {

	@Value("${AES.KEY}")
	private String key;

	private EncryptDecryptHelper encryptDecryptHelper;
	private UserRepo userRepo;
	private MRolePermissionMapRepo mRolePermissionMapRepo;
	private EncryptedDecryptResponseHelper responseHelper;
	private ObjectMapper objectMapper;

	@Autowired
	public UserService(ObjectMapper objectMapper, EncryptDecryptHelper encryptDecryptHelper, UserRepo userRepo,
			MRolePermissionMapRepo mRolePermissionMapRepo, EncryptedDecryptResponseHelper responseHelper) {
		this.encryptDecryptHelper = encryptDecryptHelper;
		this.objectMapper = objectMapper;
		this.userRepo = userRepo;
		this.mRolePermissionMapRepo = mRolePermissionMapRepo;
		this.responseHelper = responseHelper;
	}

	public ResponseEntity<?> findByUsername(AuthRequest authRequest) {
		System.out.println("UserService.findByUsername()");
		System.out.println("AuthRequest :" + authRequest);
		Optional<User> user = null;

		try {
			UserInputReq inputReq = objectMapper.readValue(encryptDecryptHelper.decrypt(key, authRequest.getDataKey()),
					UserInputReq.class);

			System.out.println("UserInputReq : " + inputReq);

			user = userRepo.findByUserNameAndPwd(inputReq.getUsername(), inputReq.getPassword());

		} catch (JsonMappingException e) {
			System.out.println("JsonMappingException{}");
			throw new GlobalException(HttpStatus.EXPECTATION_FAILED, "JsonMappingException");
		} catch (JsonProcessingException e) {
			System.out.println("JsonProcessingException{}");
			throw new GlobalException(HttpStatus.EXPECTATION_FAILED, "JsonProcessingException");
		} catch (Exception e) {
			System.out.println("Exception occured in findByUsername{}");
			e.printStackTrace();
		}

		if (user.isPresent()) {
			User usr = user.get();
			Integer roleId = userRepo.findByRoleId(usr.getId());
			usr.setRolePermissionMap(mRolePermissionMapRepo.findByRolePermissionMap("AND_MENU", roleId, "A"));

			Map<String, String> map = responseHelper.encryptedSuccessResponse(
					ResponseEntity.ok(new AuthResponse(HttpStatus.OK.value(), "success", usr)), UserService.class);
			return ResponseEntity.ok(map);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(new Response<>(HttpStatus.UNAUTHORIZED.value(), "Unauthorized Request", null));
		}
	}
}
