/*******************************************************************************
  * Copyright (c) 2017 DocDoku.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v1.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *    DocDoku - initial API and implementation
  *******************************************************************************/

package org.polarsys.eplmp.server.rest.converters;

import org.dozer.DozerConverter;
import org.polarsys.eplmp.core.hooks.SNSWebhookApp;
import org.polarsys.eplmp.core.hooks.SimpleWebhookApp;
import org.polarsys.eplmp.core.hooks.Webhook;
import org.polarsys.eplmp.core.hooks.WebhookApp;
import org.polarsys.eplmp.server.rest.dto.WebhookAppParameterDTO;
import org.polarsys.eplmp.server.rest.dto.WebhookDTO;

import java.util.ArrayList;
import java.util.List;


public class WebhookDozerConverter extends DozerConverter<Webhook, WebhookDTO> {


    public WebhookDozerConverter() {
        super(Webhook.class, WebhookDTO.class);
    }

    @Override
    public WebhookDTO convertTo(Webhook webhook, WebhookDTO pWebhookDTO) {

        if (webhook != null) {
            List<WebhookAppParameterDTO> parameters=new ArrayList<>();
            WebhookDTO webhookDTO = new WebhookDTO(webhook.getId(),webhook.getName(), webhook.isActive(),parameters,webhook.getAppName());

            WebhookApp app = webhook.getWebhookApp();
            if(app instanceof SimpleWebhookApp){
                SimpleWebhookApp simpleApp = (SimpleWebhookApp)app;
                if(simpleApp.getMethod()!=null)
                    parameters.add(new WebhookAppParameterDTO("method", simpleApp.getMethod()));
                if(simpleApp.getUri()!=null)
                    parameters.add(new WebhookAppParameterDTO("uri", simpleApp.getUri()));
                if(simpleApp.getAuthorization()!=null)
                    parameters.add(new WebhookAppParameterDTO("authorization", simpleApp.getAuthorization()));

            }else if(app instanceof SNSWebhookApp){
                SNSWebhookApp snsApp = (SNSWebhookApp)app;
                if(snsApp.getTopicArn()!=null)
                    parameters.add(new WebhookAppParameterDTO("topicArn", snsApp.getTopicArn()));
                if(snsApp.getRegion()!=null)
                    parameters.add(new WebhookAppParameterDTO("region", snsApp.getRegion()));
                if(snsApp.getAwsAccount()!=null)
                    parameters.add(new WebhookAppParameterDTO("awsAccount", snsApp.getAwsAccount()));
                if(snsApp.getAwsSecret()!=null)
                    parameters.add(new WebhookAppParameterDTO("awsSecret", snsApp.getAwsSecret()));
            }

            return webhookDTO;
        }

        return null;
    }

    @Override
    public Webhook convertFrom(WebhookDTO webhookDTO, Webhook webhook) {
        return webhook;
    }

}
