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

package org.symphonyoss.integration.webhook.universal.parser.v1;

import org.springframework.stereotype.Component;
import org.symphonyoss.integration.entity.MessageMLParseException;
import org.symphonyoss.integration.model.message.Message;
import org.symphonyoss.integration.model.message.MessageMLVersion;
import org.symphonyoss.integration.parser.SafeStringUtils;
import org.symphonyoss.integration.webhook.WebHookPayload;
import org.symphonyoss.integration.webhook.exception.WebHookParseException;
import org.symphonyoss.integration.webhook.parser.WebHookParser;

import java.util.Collections;
import java.util.List;

/**
 * Parses a message received from Universal Webhook Integration using MessageML v1.
 *
 * Created by rsanchez on 09/05/17.
 */
@Component
public class V1SimpleWebHookIntegrationParser implements WebHookParser {

  public static final String PAYLOAD = "payload";

  @Override
  public List<String> getEvents() {
    return Collections.emptyList();
  }

  @Override
  public Message parse(WebHookPayload payload) throws WebHookParseException {
    String body = payload.getBody();

    if (body == null) {
      body = payload.getParameters().get(PAYLOAD);
    }

    if (body == null) {
      throw new MessageMLParseException("Invalid message format. Message: " + payload);
    }
    body = SafeStringUtils.escapeAmpersand(body);

    Message message = new Message();
    message.setMessage(body);
    message.setFormat(Message.FormatEnum.MESSAGEML);
    message.setVersion(MessageMLVersion.V1);

    return message;
  }
}

