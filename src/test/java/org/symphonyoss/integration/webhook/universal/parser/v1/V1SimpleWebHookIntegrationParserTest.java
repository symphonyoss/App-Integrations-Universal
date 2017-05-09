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

import static org.junit.Assert.assertEquals;
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
 * Unit test for {@link V1SimpleWebHookIntegrationParser}
 * Created by rsanchez on 09/05/17.
 */
public class V1SimpleWebHookIntegrationParserTest {

  private static final String VALID_MESSAGEML_FILENAME = "v1MessageML.xml";

  private V1SimpleWebHookIntegrationParser parser = new V1SimpleWebHookIntegrationParser();

  @Test
  public void testEmptyEvents() {
    assertTrue(parser.getEvents().isEmpty());
  }

  @Test(expected = MessageMLParseException.class)
  public void testEmptyForm() throws WebHookParseException {
    WebHookPayload payload = new WebHookPayload(Collections.<String, String>emptyMap(), Collections.<String, String>emptyMap(), null);
    parser.parse(payload);
  }

  @Test
  public void testValidBody() throws WebHookParseException, IOException {
    String messageML = FileUtils.readMessageMLFile(VALID_MESSAGEML_FILENAME);
    WebHookPayload payload = new WebHookPayload(Collections.<String, String>emptyMap(), Collections.<String, String>emptyMap(), messageML);

    Message result = parser.parse(payload);
    assertEquals(messageML, result.getMessage());
    assertEquals(MessageMLVersion.V1, result.getVersion());
  }

  @Test
  public void testValidForm() throws WebHookParseException, IOException {
    String messageML = FileUtils.readMessageMLFile(VALID_MESSAGEML_FILENAME);

    Map<String, String> parameters = new HashMap<>();
    parameters.put(V1SimpleWebHookIntegrationParser.PAYLOAD, messageML);

    WebHookPayload payload = new WebHookPayload(parameters, Collections.<String, String>emptyMap(), null);
    Message result = parser.parse(payload);
    assertEquals(messageML, result.getMessage());
    assertEquals(MessageMLVersion.V1, result.getVersion());
  }

}
