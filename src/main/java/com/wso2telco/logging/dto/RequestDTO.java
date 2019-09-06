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
package com.wso2telco.logging.dto;

import lombok.Getter;
import lombok.Setter;

public class RequestDTO {

    private @Getter @Setter String apiRequestId;
    private @Getter @Setter String apiName;
    private @Getter @Setter String spName;
    private @Getter @Setter String apiPublisher;
    private @Getter @Setter String apiVersion;
    private @Getter @Setter String apiContext;
    private @Getter @Setter String applicationName;
    private @Getter @Setter String applicationId;
    private @Getter @Setter String consumerKey;
    private @Getter @Setter String restSubRequestPath;
    private @Getter @Setter String method;
    private @Getter @Setter String requestBody;
    private @Getter @Setter String userId;
    private @Getter @Setter String resource;
    private @Getter @Setter String httpMethod;

}
