package com.gtwy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;

import com.gtwy.utility.CredentialsUtil;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	private BCryptPasswordEncoder passwordEncoder;
	private CredentialsUtil credentialsUtil;

	@Autowired
	public AuthorizationServerConfig(BCryptPasswordEncoder passwordEncoder, CredentialsUtil credentialsUtil) {
		this.passwordEncoder = passwordEncoder;
		this.credentialsUtil = credentialsUtil;
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.tokenKeyAccess("isAuthenticated()").checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		clients.inMemory()
				// 1st client
				.withClient(credentialsUtil.getClient1ClientId()).authorizedGrantTypes("client_credentials")
				.secret(passwordEncoder.encode(credentialsUtil.getClient1Secret())).scopes("user_info", "read", "write")
				.accessTokenValiditySeconds(credentialsUtil.getClient1validityInSec()).autoApprove(false).and()

				// 2nd client
				.withClient(credentialsUtil.getClient2ClientId())
				.secret(passwordEncoder.encode(credentialsUtil.getClient2Secret())).scopes("user_info", "read", "write")
				.accessTokenValiditySeconds(credentialsUtil.getClient2validityInSec()).autoApprove(false);
	}

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(authenticationManager);
	}
}
