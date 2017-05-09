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

package org.symphonyoss.integration.webhook.universal.parser;

import org.symphonyoss.integration.model.config.IntegrationSettings;
import org.symphonyoss.integration.webhook.WebHookPayload;
import org.symphonyoss.integration.webhook.parser.WebHookParser;
import org.symphonyoss.integration.webhook.parser.WebHookParserFactory;

/**
 * Common methods to retrieve the parser required.
 *
 * Created by rsanchez on 09/05/17.
 */
public abstract class SimpleWebHookParserFactory implements WebHookParserFactory {

  private WebHookParser parser;

  public SimpleWebHookParserFactory(WebHookParser parser) {
    this.parser = parser;
  }

  @Override
  public void onConfigChange(IntegrationSettings settings) {
    // Do nothing
  }

  @Override
  public WebHookParser getParser(WebHookPayload payload) {
    return parser;
  }

}
