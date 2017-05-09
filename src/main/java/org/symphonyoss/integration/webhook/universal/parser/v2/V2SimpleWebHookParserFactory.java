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

import static javax.ws.rs.core.MediaType.MULTIPART_FORM_DATA_TYPE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.symphonyoss.integration.model.message.MessageMLVersion;
import org.symphonyoss.integration.webhook.WebHookPayload;
import org.symphonyoss.integration.webhook.parser.WebHookParser;
import org.symphonyoss.integration.webhook.universal.parser.SimpleWebHookParserFactory;
import org.symphonyoss.integration.webhook.universal.parser.v1.V1SimpleWebHookParserFactory;

import javax.ws.rs.core.MediaType;

/**
 * Factory to build the parser related to the messageML v2.
 *
 * Created by rsanchez on 09/05/17.
 */
@Component
public class V2SimpleWebHookParserFactory extends SimpleWebHookParserFactory {

  private V1SimpleWebHookParserFactory v1Factory;

  @Autowired
  public V2SimpleWebHookParserFactory(V2SimpleWebHookIntegrationParser parser,
      V1SimpleWebHookParserFactory factory) {
    super(parser);
    this.v1Factory = factory;
  }

  @Override
  public boolean accept(MessageMLVersion version) {
    return MessageMLVersion.V2.equals(version);
  }

  @Override
  public WebHookParser getParser(WebHookPayload payload) {
    MediaType contentType = payload.getContentType();

    if ((!MediaType.WILDCARD_TYPE.equals(contentType)) && (MULTIPART_FORM_DATA_TYPE.isCompatible(contentType))) {
      return super.getParser(payload);
    }

    return v1Factory.getParser(payload);
  }

}
