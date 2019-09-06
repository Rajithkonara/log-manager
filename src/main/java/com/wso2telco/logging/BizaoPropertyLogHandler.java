/*******************************************************************************
 * Copyright  (c) 2015-2016, WSO2.Telco Inc. (http://www.wso2telco.com) All Rights Reserved.
 *
 * WSO2.Telco Inc. licences this file to you under  the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.wso2telco.logging;

import com.wso2telco.logging.dto.ErrorDTO;
import com.wso2telco.logging.dto.RequestDTO;
import com.wso2telco.logging.dto.ResponseDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.config.Entry;

/**
 * Bizao log handler
 */
public class BizaoPropertyLogHandler extends LogHandler {

    private static final Log logHandler = LogFactory.getLog("REQUEST_RESPONSE_LOGGER");
    private static final String REGISTRY_PATH = "gov:/apimgt/";
    private static final String PAYLOAD_LOGGING_ENABLED = "payload.logging.enabled";
    private static final String BIZAO_TOKEN = "bizao-token";
    private static final String BIZAO_ALIAS = "bizao-alias";

    @Override
    public void logRequestProperties(MessageContext messageContext,
                                     org.apache.axis2.context.MessageContext axis2MessageContext,
                                     boolean isPayloadLoggingEnabled) {

        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setApiRequestId(String.valueOf(messageContext.getProperty(UUID)));
        requestDTO.setApiName(String.valueOf(messageContext.getProperty(API_NAME)));
        requestDTO.setSpName(String.valueOf(messageContext.getProperty(SP_NAME)));
        requestDTO.setApiPublisher(String.valueOf(messageContext.getProperty(API_PUBLISHER)));
        requestDTO.setApiVersion(String.valueOf(messageContext.getProperty(API_VERSION)));
        requestDTO.setApiContext(String.valueOf(messageContext.getProperty(API_CONTEXT)));
        requestDTO.setApplicationName(String.valueOf(messageContext.getProperty(APPLICATION_NAME)));
        requestDTO.setApplicationId(String.valueOf(messageContext.getProperty(APPLICATION_ID)));
        requestDTO.setConsumerKey(String.valueOf(messageContext.getProperty(CONSUMER_KEY)));
        requestDTO.setRestSubRequestPath(String.valueOf(messageContext.getProperty(REST_SUB_REQUEST_PATH)));
        requestDTO.setMethod(String.valueOf(messageContext.getProperty(METHOD)));
        requestDTO.setRequestBody(super.handleAndReturnPayload(messageContext));

        String orange = super.captureRequestProperties(requestDTO, isPayloadLoggingEnabled);
        orange += ",BIZAO_TOKEN:" + messageContext.getProperty(BIZAO_TOKEN)
                + ",BIZAO_ALIAS:" + messageContext.getProperty(BIZAO_ALIAS);

        super.printLog(logHandler, orange);
    }

    @Override
    public void logResponseProperties(MessageContext messageContext,
                               org.apache.axis2.context.MessageContext axis2MessageContext,
                               boolean isPayloadLoggingEnabled) {

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setApiRequestId(String.valueOf(messageContext.getProperty(UUID)));
        responseDTO.setHttpStatus(String.valueOf(axis2MessageContext.getProperty(HTTP_SC)));
        responseDTO.setResponseTime(String.valueOf(messageContext.getProperty(RESPONSE_TIME)));
        responseDTO.setResponseBody(super.handleAndReturnPayload(messageContext));

        super.printLog(logHandler,super.captureResponseProperties(responseDTO, isPayloadLoggingEnabled));

    }

    @Override
    public void logErrorProperties(MessageContext messageContext,
                            org.apache.axis2.context.MessageContext axis2MessageContext,
                            boolean isPayloadLoggingEnabled) {

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setApiRequestId(String.valueOf(messageContext.getProperty(UUID)));
        errorDTO.setRequestBody(messageContext.getEnvelope().getBody().toString());
        errorDTO.setRestFullRequestPath(String.valueOf(messageContext.getProperty(REST_FULL_REQUEST_PATH)));
        errorDTO.setSynapseRestApi(String.valueOf(messageContext.getProperty(SYNAPSE_REST_API)));
        errorDTO.setSynapseRestApiVersion(String.valueOf(messageContext.getProperty(API_VERSION)));
        errorDTO.setApiResourceCacheKey(String.valueOf(messageContext.getProperty(API_RESOURCE_CACHE_KEY)));
        errorDTO.setErrorException(String.valueOf(messageContext.getProperty(ERROR_EXCEPTION)));
        errorDTO.setApplicationName(String.valueOf(messageContext.getProperty(APPLICATION_NAME)));
        errorDTO.setApplicationId(String.valueOf(messageContext.getProperty(APPLICATION_ID)));
        errorDTO.setErrorCode(String.valueOf(messageContext.getProperty(ERROR_CODE)));
        errorDTO.setHttpStatusCode(String.valueOf(axis2MessageContext.getProperty(HTTP_SC)));
        errorDTO.setErrorMessage(String.valueOf(messageContext.getProperty(ERROR_MESSAGE)));

        String orange = super.captureErrorProperties(errorDTO, isPayloadLoggingEnabled);
        orange += ",BIZAO_TOKEN:" + messageContext.getProperty(BIZAO_TOKEN)
                + ",BIZAO_ALIAS:" + messageContext.getProperty(BIZAO_ALIAS);

        super.printLog(logHandler, orange);
    }

    @Override
    public Entry getPayloadEntry() {
        return new Entry(REGISTRY_PATH + PAYLOAD_LOGGING_ENABLED);
    }

}
