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
import org.symphonyoss.integration.model.message.MessageMLVersion;
import org.symphonyoss.integration.webhook.WebHookPayload;

import java.util.Collections;

/**
 * Unit test for {@link V1SimpleWebHookParserFactory}
 * Created by rsanchez on 09/05/17.
 */
public class V1SimpleWebHookParserFactoryTest {

  private V1SimpleWebHookIntegrationParser parser = new V1SimpleWebHookIntegrationParser();

  private V1SimpleWebHookParserFactory parserFactory = new V1SimpleWebHookParserFactory(parser);

  @Test
  public void testAccept() {
    assertTrue(parserFactory.accept(MessageMLVersion.V1));
  }

  @Test
  public void testGetParser() {
    WebHookPayload payload = new WebHookPayload(Collections.<String, String>emptyMap(), Collections.<String, String>emptyMap(), null);
    assertEquals(parser, parserFactory.getParser(payload));
  }

}
