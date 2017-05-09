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

import static javax.ws.rs.core.HttpHeaders.CONTENT_TYPE;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.symphonyoss.integration.entity.MessageMLParseException;
import org.symphonyoss.integration.event.MessageMLVersionUpdatedEventData;
import org.symphonyoss.integration.model.message.Message;
import org.symphonyoss.integration.model.message.MessageMLVersion;
import org.symphonyoss.integration.webhook.WebHookPayload;
import org.symphonyoss.integration.webhook.exception.WebHookParseException;
import org.symphonyoss.integration.webhook.universal.parser.SimpleWebHookParserFactory;
import org.symphonyoss.integration.webhook.universal.parser.SimpleWebHookParserResolver;
import org.symphonyoss.integration.webhook.universal.parser.v1.V1SimpleWebHookIntegrationParser;
import org.symphonyoss.integration.webhook.universal.parser.v1.V1SimpleWebHookParserFactory;
import org.symphonyoss.integration.webhook.universal.parser.v2.V2SimpleWebHookIntegrationParser;
import org.symphonyoss.integration.webhook.universal.parser.v2.V2SimpleWebHookParserFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

/**
 * Unit test for {@link SimpleWebHookIntegration}
 * Created by rsanchez on 25/08/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class SimpleWebHookIntegrationTest {

  private static final String VALID_MESSAGEML = "<messageML><mention email=\"rsanchez@symphony"
      + ".com\"/> created SAM-25 (<a href=\"https://whiteam1.atlassian.net/browse/SAM-25\"/>) "
      + "(<b>Highest</b> Story in Sample 1 with labels &quot;production&quot;)<br/>Description: Issue "
      + "Test<br/>Assignee: <mention email=\"rsanchez@symphony.com\"/></messageML>";

  private static final String VALID_MESSAGEML_V2 = "<messageML><div class=\"entity\" data-entity-id"
      + "=\"testMessage\"><header><span>${entity['testMessage'].message"
      + ".header}</span></header><body><div class=\"labelBackground "
      + "badge\"><span>${entity['testMessage'].message"
      + ".body}</span></div></body></card></div></messageML>";

  private static final String VALID_ENTITY_JSON = "{\"testMessage\": {\"type\": \"com"
      + ".symphony.integration.test.event.v2.testMessage\", \"version\": \"1.0\", \"message\" : "
      + "{\"type\": \"com.symphony.integration.test.event.message\", \"version\": \"1.0\", "
      + "\"header\": \"New message\", \"body\": \"Test message\"} }";

  @Spy
  private List<SimpleWebHookParserFactory> factories = new ArrayList<>();

  @InjectMocks
  private SimpleWebHookParserResolver resolver;

  private SimpleWebHookIntegration simpleWebHookIntegration;

  private V1SimpleWebHookIntegrationParser v1Parser = new V1SimpleWebHookIntegrationParser();

  private V2SimpleWebHookIntegrationParser v2Parser = new V2SimpleWebHookIntegrationParser();

  private V1SimpleWebHookParserFactory v1ParserFactory = new V1SimpleWebHookParserFactory(v1Parser);

  private V2SimpleWebHookParserFactory v2ParserFactory = new V2SimpleWebHookParserFactory(v2Parser, v1ParserFactory);

  @Before
  public void init() {
    factories.add(v1ParserFactory);
    factories.add(v2ParserFactory);

    this.simpleWebHookIntegration = new SimpleWebHookIntegration(resolver);
  }

  @Test
  public void testValidBodyMessageMLV1() throws WebHookParseException {
    MessageMLVersionUpdatedEventData eventData = new MessageMLVersionUpdatedEventData(MessageMLVersion.V1);
    resolver.handleMessageMLVersionUpdatedEvent(eventData);

    WebHookPayload payload = new WebHookPayload(Collections.<String, String>emptyMap(), Collections.<String, String>emptyMap(), VALID_MESSAGEML);

    Message result = simpleWebHookIntegration.parse(payload);
    assertEquals(VALID_MESSAGEML, result.getMessage());
    assertEquals(MessageMLVersion.V1, result.getVersion());
  }

  @Test
  public void testValidBodyMessageMLV2() throws WebHookParseException {
    MessageMLVersionUpdatedEventData eventData = new MessageMLVersionUpdatedEventData(MessageMLVersion.V2);
    resolver.handleMessageMLVersionUpdatedEvent(eventData);

    Map<String, String> headers = new HashMap<>();
    headers.put(CONTENT_TYPE.toLowerCase(), MediaType.MULTIPART_FORM_DATA);

    Map<String, String> parameters = new HashMap<>();
    parameters.put(V2SimpleWebHookIntegrationParser.MESSAGE, VALID_MESSAGEML_V2);
    parameters.put(V2SimpleWebHookIntegrationParser.DATA, VALID_ENTITY_JSON);

    WebHookPayload payload = new WebHookPayload(parameters, headers, null);

    Message result = simpleWebHookIntegration.parse(payload);

    assertEquals(VALID_MESSAGEML_V2, result.getMessage());
    assertEquals(VALID_ENTITY_JSON, result.getData());
    assertEquals(MessageMLVersion.V2, result.getVersion());
  }
}
