package com.wso2telco.logging.dto;

import lombok.Getter;
import lombok.Setter;

public class ErrorDTO {

    private @Getter @Setter String apiRequestId;
    private @Getter @Setter String requestBody;
    private @Getter @Setter String restFullRequestPath;
    private @Getter @Setter String synapseRestApi;
    private @Getter @Setter String synapseRestApiVersion;
    private @Getter @Setter String apiResourceCacheKey;
    private @Getter @Setter String errorException;
    private @Getter @Setter String applicationName;
    private @Getter @Setter String applicationId;
    private @Getter @Setter String errorCode;
    private @Getter @Setter String httpStatusCode;
    private @Getter @Setter String errorMessage;
    private @Getter @Setter String errvar;
    private @Getter @Setter String resource;

}
