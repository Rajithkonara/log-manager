package com.wso2telco.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

public class AnalyticsLogHandler extends AbstractMediator {

    private static final Log logHandler = LogFactory.getLog("REQUEST_RESPONSE_LOGGER");

    public boolean mediate(MessageContext context) {

        org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) context).
                getAxis2MessageContext();

        String direction = (String) axis2MessageContext.getProperty("message.type");

        if(direction.equalsIgnoreCase("request")) {
            String request = context.getEnvelope().getBody().toString();
            context.setProperty("requestBody", request);
        } else if (direction.equalsIgnoreCase("response")) {
            logResponse(context);
        } else if (direction.equalsIgnoreCase("error")) {
            logErrorProperties(context, axis2MessageContext);
        }

        return true;
    }


    public void logResponse(MessageContext context) {
        org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) context).
                getAxis2MessageContext();


        String direction = (String) axis2MessageContext.getProperty("message.type");

        if(direction.equalsIgnoreCase("response")) {

            String uuid = (String) context.getProperty("MESSAGE_ID");
            String request = (String) context.getProperty("requestBody");
            String response = context.getEnvelope().getBody().toString();

            logHandler.info("TRANSACTION:call, " + "uuid:" + uuid +" ,requestBody: " + request +
                    ",API_NAME:" + context.getProperty("API_NAME") +
                    ",SP_NAME:" + context.getProperty("api.ut.userName") +
                    ",API_PUBLISHER:" + context.getProperty("api.ut.apiPublisher")+
                    ",API_VERSION:" + context.getProperty("SYNAPSE_REST_API_VERSION") +
                    ",API_CONTEXT:" + context.getProperty("api.ut.context") +
                    ",APPLICATION_NAME:" + context.getProperty("api.ut.application.name") +
                    ",APPLICATION_ID:" + context.getProperty("api.ut.application.id") +
                    ",CONSUMER_KEY:" + context.getProperty("api.ut.consumerKey") +
                    ",API_RESOURCE_PATH:" + context.getProperty("REST_SUB_REQUEST_PATH") +
                    ",METHOD:"+ context.getProperty("api.ut.HTTP_METHOD") +
                    ",HTTP_STATUS: " + axis2MessageContext.getProperty("HTTP_SC") +
                    ",RESPONSE_TIME:" + context.getProperty("RESPONSE_TIME") +
                    ",BODY:" + response.replaceAll("\n", "")

            );

        }
    }


    public void logErrorProperties(MessageContext messageContext,
                                   org.apache.axis2.context.MessageContext axis2MessageContext) {

        String direction = (String) axis2MessageContext.getProperty("message.type");

        if (direction.equalsIgnoreCase("error")) {
            String request = messageContext.getEnvelope().getBody().toString();
            logHandler.info("TRANSACTION:call, " + "uuid:" + messageContext.getProperty("MESSAGE_ID") +
             ",requestBody: " + request +
             ",REST_FULL_REQUEST_PATH: " + messageContext.getProperty("REST_FULL_REQUEST_PATH") +
             ",SYNAPSE_REST_API: " + messageContext.getProperty("ERROR_MESSAGE") +
             ",SYNAPSE_REST_API_VERSION: " + messageContext.getProperty("SYNAPSE_REST_API_VERSION") +
             ",API_RESOURCE_CACHE_KEY: " + messageContext.getProperty("API_RESOURCE_CACHE_KEY") +
             ",ERROR_EXCEPTION: " + messageContext.getProperty("ERROR_EXCEPTION") +
             ",APPLICATION_NAME: " + messageContext.getProperty("api.ut.application.name") +
             ",APPLICATION_ID: " + messageContext.getProperty("api.ut.application.id") +
             ",ERROR_CODE: " + messageContext.getProperty("ERROR_CODE") +
             ",HTTP_STATUS: " + axis2MessageContext.getProperty("HTTP_SC") +
             ",ERROR_MESSAGE: " + messageContext.getProperty("ERROR_MESSAGE"));
        }

    }
}
