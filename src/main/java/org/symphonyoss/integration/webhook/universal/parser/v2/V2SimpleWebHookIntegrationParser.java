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

package org.symphonyoss.integration.webhook.universal.parser.v2;

import org.apache.commons.lang3.StringUtils;
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
import java.util.Map;

/**
 * Parses a message received from Universal Webhook Integration using MessageML v2.
 *
 * Created by rsanchez on 09/05/17.
 */
@Component
public class V2SimpleWebHookIntegrationParser implements WebHookParser {

  public static final String MESSAGE = "message";

  public static final String DATA = "data";

  @Override
  public List<String> getEvents() {
    return Collections.emptyList();
  }

  @Override
  public Message parse(WebHookPayload payload) throws WebHookParseException {
    Message message = new Message();

    Map<String, String> parameters = payload.getParameters();

    String messageML = parameters.get(MESSAGE);

    if (StringUtils.isEmpty(messageML)) {
      throw new MessageMLParseException("Invalid message format. No messageML defined");
    }
    messageML = SafeStringUtils.escapeAmpersand(messageML);

    message.setMessage(messageML);

    String entityJSON = parameters.get(DATA);

    if (StringUtils.isNotEmpty(entityJSON)) {
      entityJSON = SafeStringUtils.escapeAmpersand(entityJSON);
      message.setData(entityJSON);
    }

    message.setVersion(MessageMLVersion.V2);

    return message;
  }

}

