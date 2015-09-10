package com.doubletuan.sns.web.rest;

import javax.inject.Inject;

import org.igniterealtime.restclient.RestApiClient;
import org.igniterealtime.restclient.entity.AuthenticationToken;

import com.doubletuan.sns.domain.OpenFireConfiguration;
import com.doubletuan.sns.repository.OpenFireConfigurationRepository;
import com.doubletuan.sns.repository.SystemConfigurationRepository;

public class BaseResource {
	@Inject
    private OpenFireConfigurationRepository openFireConfigurationRepository;
	
//	@Inject
//	public SystemConfigurationRepository systemConfigurationRepository;
	
	public BaseResource() {
	}
	
	public RestApiClient getRestApiClient() {
		
		OpenFireConfiguration openFireConfiguration = openFireConfigurationRepository.findByIdentifier("RESTAPI");
    	AuthenticationToken authenticationToken = new AuthenticationToken(openFireConfiguration.getAuthenticationToken());
    	
		RestApiClient restApiClient = new RestApiClient(openFireConfiguration.getServerAddress(), Integer.valueOf(openFireConfiguration.getRestApiPort()), authenticationToken);
		
		return restApiClient;
	}
}
