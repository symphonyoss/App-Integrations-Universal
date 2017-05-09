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

import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.symphonyoss.integration.model.message.MessageMLVersion;
import org.symphonyoss.integration.webhook.WebHookPayload;
import org.symphonyoss.integration.webhook.universal.parser.v1.V1SimpleWebHookIntegrationParser;
import org.symphonyoss.integration.webhook.universal.parser.v1.V1SimpleWebHookParserFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

/**
 * Unit test for {@link V2SimpleWebHookParserFactory}
 * Created by rsanchez on 09/05/17.
 */
public class V2SimpleWebHookParserFactoryTest {

  private V1SimpleWebHookIntegrationParser v1Parser = new V1SimpleWebHookIntegrationParser();

  private V2SimpleWebHookIntegrationParser v2Parser = new V2SimpleWebHookIntegrationParser();

  private V1SimpleWebHookParserFactory v1ParserFactory = new V1SimpleWebHookParserFactory(v1Parser);

  private V2SimpleWebHookParserFactory parserFactory = new V2SimpleWebHookParserFactory(v2Parser, v1ParserFactory);

  @Test
  public void testAccept() {
    assertTrue(parserFactory.accept(MessageMLVersion.V2));
  }

  @Test
  public void testGetV1Parser() {
    WebHookPayload payload = new WebHookPayload(Collections.<String, String>emptyMap(), Collections.<String, String>emptyMap(), null);
    assertEquals(v1Parser, parserFactory.getParser(payload));

    Map<String, String> headers = new HashMap<>();
    headers.put(CONTENT_TYPE, MediaType.APPLICATION_XML);

    payload = new WebHookPayload(Collections.<String, String>emptyMap(), headers, null);
    assertEquals(v1Parser, parserFactory.getParser(payload));
  }

  @Test
  public void testGetV2Parser() {
    Map<String, String> headers = new HashMap<>();
    headers.put(CONTENT_TYPE.toLowerCase(), MediaType.MULTIPART_FORM_DATA);

    WebHookPayload payload = new WebHookPayload(Collections.<String, String>emptyMap(), headers, null);
    assertEquals(v2Parser, parserFactory.getParser(payload));
  }
}
