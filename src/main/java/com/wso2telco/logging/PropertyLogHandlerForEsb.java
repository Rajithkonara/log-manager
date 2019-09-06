package com.wso2telco.logging;

import com.wso2telco.logging.dto.ErrorDTO;
import com.wso2telco.logging.dto.RequestDTO;
import com.wso2telco.logging.dto.ResponseDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.config.Entry;

import java.util.TreeMap;

/**
 * Generic log handler for ESB
 */
public class PropertyLogHandlerForEsb extends LogHandler {

    private static final String REGISTRY_PATH = "gov:/event/";
    private static final String PAYLOAD_LOGGING_ENABLED = "payload.logging.enabled";
    private static final Log logHandler = LogFactory.getLog("REQUEST_RESPONSE_LOGGER");

    @Override
    public void logRequestProperties(MessageContext messageContext,
                              org.apache.axis2.context.MessageContext axis2MessageContext,
                              boolean isPayloadLoggingEnabled) {

        TreeMap<String, String> headers = (TreeMap<String, String>) axis2MessageContext
                .getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);

        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setApiRequestId(headers.get(REQUEST_ID));
        requestDTO.setApiName(headers.get(API_NAME));
        requestDTO.setSpName(headers.get(USER_ID));
        requestDTO.setApiPublisher(headers.get(TRP_API_PUBLISHER));
        requestDTO.setApiVersion(headers.get(TRP_API_VERSION));
        requestDTO.setApiContext(headers.get(TRP_API_CONTEXT));
        requestDTO.setApplicationName(headers.get(TRP_APPLICATION_NAME));
        requestDTO.setApplicationId(headers.get(TRP_APPLICATION_ID));
        requestDTO.setConsumerKey(headers.get(TRP_CONSUMER_KEY));
        requestDTO.setRestSubRequestPath(headers.get(TRP_RESOURCE));
        requestDTO.setMethod(headers.get(TRP_HTTP_METHOD));
        requestDTO.setRequestBody(super.handleAndReturnPayload(messageContext));

        super.printLog(logHandler, super.captureRequestProperties(requestDTO, isPayloadLoggingEnabled));

    }

    @Override
    public void logResponseProperties(MessageContext messageContext,
                               org.apache.axis2.context.MessageContext axis2MessageContext,
                               boolean isPayloadLoggingEnabled) {

        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setApiRequestId(String.valueOf(messageContext.getProperty(REQUEST_ID)));
        responseDTO.setHttpStatus(String.valueOf(axis2MessageContext.getProperty(HTTP_SC)));
        responseDTO.setResponseTime(String.valueOf(messageContext.getProperty(RESPONSE_TIME)));
        responseDTO.setResponseBody(super.handleAndReturnPayload(messageContext));

        super.printLog(logHandler, super.captureResponseProperties(responseDTO, isPayloadLoggingEnabled));

    }

    @Override
    public void logErrorProperties(MessageContext messageContext,
                            org.apache.axis2.context.MessageContext axis2MessageContext,
                            boolean isPayloadLoggingEnabled) {

        TreeMap<String, String> headers = (TreeMap<String, String>) axis2MessageContext
                .getProperty(org.apache.axis2.context.MessageContext.TRANSPORT_HEADERS);

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setApiRequestId(String.valueOf(messageContext.getProperty(REQUEST_ID)));
        errorDTO.setRequestBody(messageContext.getEnvelope().getBody().toString());
        errorDTO.setRestFullRequestPath(headers.get(TRP_RESOURCE));
        errorDTO.setSynapseRestApi(headers.get(API_NAME));
        errorDTO.setSynapseRestApiVersion(headers.get(TRP_API_VERSION));
        errorDTO.setApiResourceCacheKey(String.valueOf(messageContext.getProperty(API_RESOURCE_CACHE_KEY)));
        errorDTO.setErrorException(String.valueOf(messageContext.getProperty(ERROR_EXCEPTION)));
        errorDTO.setApplicationName(headers.get(TRP_APPLICATION_NAME));
        errorDTO.setApplicationId(headers.get(APPLICATION_ID));
        errorDTO.setErrorCode(String.valueOf(messageContext.getProperty(ERROR_CODE)));
        errorDTO.setHttpStatusCode(String.valueOf(axis2MessageContext.getProperty(HTTP_SC)));
        errorDTO.setErrorMessage(String.valueOf(messageContext.getProperty(ERRVAR)));

        String dto = super.captureErrorProperties(errorDTO, isPayloadLoggingEnabled);

        if (messageContext.getProperty(ERROR_CODE).equals(900800)){
            dto += ",THROTTLED_OUT_REASON:"+
                    messageContext.getProperty(THROTTLED_OUT_REASON);
        }

        super.printLog(logHandler, dto);

    }

    @Override
    public Entry getPayloadEntry() {
        return new Entry(REGISTRY_PATH + PAYLOAD_LOGGING_ENABLED);
    }
}
