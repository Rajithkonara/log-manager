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
import org.apache.axiom.om.impl.llom.OMTextImpl;
import org.apache.commons.logging.Log;
import org.apache.synapse.MessageContext;
import org.apache.synapse.config.Entry;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

public abstract class LogHandler extends AbstractMediator {

    //todo: change the build artifact name to get wso2telco

    public static final String MESSAGE_TYPE = "message.type";
    public static final String APPLICATION_ID = "api.ut.application.id";
    public static final String API_PUBLISHER = "api.ut.apiPublisher";
    public static final String API_NAME = "API_NAME";
    public static final String SP_NAME = "api.ut.userName";
    public static final String REQUEST = "request";
    public static final String RESPONSE = "response";
    public static final String API_VERSION = "SYNAPSE_REST_API_VERSION";
    public static final String API_CONTEXT = "api.ut.context";
    public static final String UUID = "MESSAGE_ID";
    public static final String ERROR = "error";
    public static final String APPLICATION_NAME = "api.ut.application.name";
    public static final String REST_FULL_REQUEST_PATH = "REST_FULL_REQUEST_PATH";
    public static final String REST_SUB_REQUEST_PATH = "REST_SUB_REQUEST_PATH";
    public static final String SYNAPSE_REST_API = "SYNAPSE_REST_API";
    public static final String ERROR_EXCEPTION = "ERROR_EXCEPTION";
    public static final String API_RESOURCE_CACHE_KEY = "API_RESOURCE_CACHE_KEY";
    public static final String ERROR_MESSAGE = "ERROR_MESSAGE";
    public static final String ERROR_CODE = "ERROR_CODE";
    public static final String HTTP_SC = "HTTP_SC";
    public static final String METHOD = "api.ut.HTTP_METHOD";
    public static final String RESPONSE_TIME = "RESPONSE_TIME";
    public static final String CONSUMER_KEY = "api.ut.consumerKey";
    public static final String REQUEST_ID = "REQUEST_ID";
    public static final String USER_ID = "USER_ID";
    public static final String TRP_API_PUBLISHER = "API_PUBLISHER";
    public static final String TRP_API_VERSION = "API_VERSION";
    public static final String TRP_API_CONTEXT = "CONTEXT";
    public static final String TRP_APPLICATION_NAME = "APPLICATION_NAME";
    public static final String TRP_APPLICATION_ID = "APPLICATION_ID";
    public static final String TRP_CONSUMER_KEY = "CONSUMER_KEY";
    public static final String TRP_RESOURCE = "RESOURCE";
    public static final String TRP_HTTP_METHOD = "HTTP_METHOD";
    public static final String ERRVAR = "errvar";
    public static final String THROTTLED_OUT_REASON = "THROTTLED_OUT_REASON";

    //abstract 4 methods
    abstract void logRequestProperties(MessageContext messageContext,
                                       org.apache.axis2.context.MessageContext axis2MessageContext,
                                       boolean isPayloadLoggingEnabled);

    abstract void logResponseProperties(MessageContext messageContext,
                                        org.apache.axis2.context.MessageContext  axis2MessageContext,
                                        boolean isPayloadLoggingEnabled);

    abstract void logErrorProperties(MessageContext messageContext,
                                     org.apache.axis2.context.MessageContext axis2MessageContext,
                                     boolean isPayloadLoggingEnabled);

    abstract Entry getPayloadEntry();

    public boolean extractPayloadLoggingStatus(MessageContext messageContext, Entry payloadEntry) {

        boolean isPayloadLoggingEnabled = false;

        OMTextImpl payloadEnableRegistryValue = (OMTextImpl) messageContext.getConfiguration().getRegistry()
                .getResource(payloadEntry, null);

        if (payloadEnableRegistryValue != null) {
            String payloadLogEnabled = payloadEnableRegistryValue.getText();

            if (nullOrTrimmed(payloadLogEnabled) != null) {
                isPayloadLoggingEnabled = Boolean.valueOf(payloadLogEnabled);
            }
        }

        return isPayloadLoggingEnabled;
    }

    public boolean mediate(MessageContext messageContext) {

        boolean isPayloadLoggingEnabled;

        org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) messageContext).
                getAxis2MessageContext();

        isPayloadLoggingEnabled = extractPayloadLoggingStatus(messageContext, getPayloadEntry());
     //  should we invoke if only payload is true
        String direction = (String) axis2MessageContext.getProperty(MESSAGE_TYPE);
        if (direction != null && direction.equalsIgnoreCase(REQUEST)) {
                    logRequestProperties(messageContext, axis2MessageContext, isPayloadLoggingEnabled);
        } else if (direction != null && direction.equalsIgnoreCase(RESPONSE)) {
            logResponseProperties(messageContext, axis2MessageContext, isPayloadLoggingEnabled);
        } else if (direction != null && direction.equalsIgnoreCase(ERROR)) {
            logErrorProperties(messageContext, axis2MessageContext, isPayloadLoggingEnabled);
        }

        return true;
    }

    public String logRequestProperties2(RequestDTO dto, boolean isPayloadLoggingEnabled) {

        if (isPayloadLoggingEnabled) {
            String requestPayload = dto.getRequestBody();
            return  "TRANSACTION:request,API_REQUEST_ID:" + dto.getApiRequestId() + "" +
                    ",API_NAME:" + dto.getApiName() + "" +
                    ",SP_NAME:" + dto.getSpName() + "" +
                    ",API_PUBLISHER:" + dto.getApiPublisher() + "" +
                    ",API_VERSION:" + dto.getApiVersion() + "" +
                    ",API_CONTEXT:" + dto.getApiContext() + "" +
                    ",APPLICATION_NAME:" + dto.getApplicationName() + "" +
                    ",APPLICATION_ID:" +  dto.getApplicationId() + "" +
                    ",CONSUMER_KEY:" + dto.getConsumerKey() + "" +
                    ",API_RESOURCE_PATH:" + dto.getRestSubRequestPath() + "" +
                    ",METHOD:" + dto.getMethod() +
                    ",BODY:" + requestPayload.replaceAll("\n", "");
        }
        return null;
    }

    public String logResponseProperties2(ResponseDTO dto,
                                         boolean isPayloadLoggingEnabled) {

        if (isPayloadLoggingEnabled) {

            String responsePayload = dto.getResponseBody();

            return "TRANSACTION:response," +
                    "API_REQUEST_ID:" + dto.getApiRequestId() + "" +
                    ",HTTP_STATUS:" + dto.getHttpStatus() + "" +
                    ",RESPONSE_TIME:" + dto.getResponseTime() + "" +
                    ",BODY:" + responsePayload.replaceAll("\n", "");
        }

        return null;

    }

    public String logErrorProperties2(ErrorDTO dto, boolean isPayloadLoggingEnabled) {

        if (isPayloadLoggingEnabled) {
            return  "TRANSACTION:errorResponse" +
                    ",API_REQUEST_ID:" + dto.getApiRequestId() +
                    ",REQUEST_BODY:" + dto.getRequestBody() +
                    ",REST_FULL_REQUEST_PATH:" + dto.getRestFullRequestPath() +
                    ",SYNAPSE_REST_API:" + dto.getSynapseRestApi() +
                    ",SYNAPSE_REST_API_VERSION:" + dto.getSynapseRestApiVersion() +
                    ",API_RESOURCE_CACHE_KEY:" + dto.getApiResourceCacheKey() +
                    ",ERROR_EXCEPTION:" + dto.getErrorException() +
                    ",APPLICATION_NAME:" + dto.getApplicationName() +
                    ",APPLICATION_ID:" + dto.getApplicationId() +
                    ",ERROR_CODE:" + dto.getErrorCode() +
                    ",HTTP_STATUS:" + dto.getHttpStatusCode() + "" +
                    ",ERROR_MESSAGE:" + dto.getErrorMessage();
        }
        return null;
    }

    protected String handleAndReturnPayload(MessageContext messageContext) {
        String payload = "";
        try {
            payload = messageContext.getEnvelope().getBody().toString();
        } catch (Exception e) {
            payload = "payload dropped due to invalid format";
        } finally {
            return payload;
        }
    }

    protected void printLog(Log handler, String log) {
        handler.info(log);
    }

    private String nullOrTrimmed(String inputString) {
        String result = null;
        if (inputString != null && inputString.trim().length() > 0) {
            result = inputString.trim();
        }
        return result;
    }

}
