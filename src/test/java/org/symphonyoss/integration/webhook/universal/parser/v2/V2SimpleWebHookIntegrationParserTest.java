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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.symphonyoss.integration.entity.MessageMLParseException;
import org.symphonyoss.integration.model.message.Message;
import org.symphonyoss.integration.model.message.MessageMLVersion;
import org.symphonyoss.integration.webhook.WebHookPayload;
import org.symphonyoss.integration.webhook.exception.WebHookParseException;
import org.symphonyoss.integration.webhook.universal.FileUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Unit test for {@link V2SimpleWebHookIntegrationParser}
 * Created by rsanchez on 09/05/17.
 */
public class V2SimpleWebHookIntegrationParserTest {

  private static final String VALID_MESSAGEML_V2_FILENAME = "v2MessageML.xml";

  private static final String VALID_ENTITY_JSON_FILENAME = "entityJSON.json";

  private V2SimpleWebHookIntegrationParser parser = new V2SimpleWebHookIntegrationParser();

  @Test
  public void testEmptyEvents() {
    assertTrue(parser.getEvents().isEmpty());
  }

  @Test(expected = MessageMLParseException.class)
  public void testEmptyMessageML() throws WebHookParseException {
    WebHookPayload payload = new WebHookPayload(Collections.<String, String>emptyMap(), Collections.<String, String>emptyMap(), null);
    parser.parse(payload);
  }

  @Test
  public void testEmptyData() throws WebHookParseException, IOException {
    String messageML = FileUtils.readMessageMLFile(VALID_MESSAGEML_V2_FILENAME);

    Map<String, String> parameters = new HashMap<>();
    parameters.put(V2SimpleWebHookIntegrationParser.MESSAGE, messageML);

    WebHookPayload payload = new WebHookPayload(parameters, Collections.<String, String>emptyMap(), null);

    Message result = parser.parse(payload);
    assertEquals(messageML, result.getMessage());
    assertEquals(MessageMLVersion.V2, result.getVersion());
    assertNull(result.getData());
  }

  @Test
  public void testFullMessage() throws WebHookParseException, IOException {
    String messageML = FileUtils.readMessageMLFile(VALID_MESSAGEML_V2_FILENAME);
    String entityJSON = FileUtils.readJsonFromFile(VALID_ENTITY_JSON_FILENAME);

    Map<String, String> parameters = new HashMap<>();
    parameters.put(V2SimpleWebHookIntegrationParser.MESSAGE, messageML);
    parameters.put(V2SimpleWebHookIntegrationParser.DATA, entityJSON);

    WebHookPayload payload = new WebHookPayload(parameters, Collections.<String, String>emptyMap(), null);

    Message result = parser.parse(payload);
    assertEquals(messageML, result.getMessage());
    assertEquals(entityJSON, result.getData());
    assertEquals(MessageMLVersion.V2, result.getVersion());
  }

}
