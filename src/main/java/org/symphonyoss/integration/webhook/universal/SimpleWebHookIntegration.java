/**
 * Copyright 2016-2017 Symphony Integrations - Symphony LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.symphonyoss.integration.webhook.universal;

import org.springframework.stereotype.Component;
import org.symphonyoss.integration.entity.MessageMLParseException;
import org.symphonyoss.integration.model.message.Message;
import org.symphonyoss.integration.model.message.MessageMLVersion;
import org.symphonyoss.integration.webhook.WebHookIntegration;
import org.symphonyoss.integration.webhook.WebHookPayload;
import org.symphonyoss.integration.webhook.exception.WebHookParseException;

/**
 * Implementation of a Simple WebHook Integration.
 *
 * Created by rsanchez on 24/08/16.
 */
@Component
public class SimpleWebHookIntegration extends WebHookIntegration {

  public static final String PAYLOAD = "payload";

  @Override
  public Message parse(WebHookPayload input) throws WebHookParseException {
    String body = input.getBody();

    if (body == null) {
      body = input.getParameters().get(PAYLOAD);
    }

    if (body == null) {
      throw new MessageMLParseException("Invalid message format. Message: " + input);
    }

    Message message = new Message();
    message.setMessage(body);
    message.setFormat(Message.FormatEnum.MESSAGEML);
    message.setVersion(MessageMLVersion.V1);

    return message;
  }

}
