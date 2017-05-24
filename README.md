[![Symphony Software Foundation - Incubating](https://cdn.rawgit.com/symphonyoss/contrib-toolbox/master/images/ssf-badge-incubating.svg)](https://symphonyoss.atlassian.net/wiki/display/FM/Incubating) [![Build Status](https://travis-ci.org/symphonyoss/App-Integrations-Universal.svg?branch=dev)](https://travis-ci.org/symphonyoss/App-Integrations-Universal) [![Dependencies](https://www.versioneye.com/user/projects/58d049f86893fd0037a30b1a/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/58d049f86893fd0037a30b1a)

# Universal Webhook Integration
The Universal Webhook Integration enables you to send messages directly from any service into a Symphony conversation of your choice - either a 1:1 IM with you, or a room that you are the owner of.

## How it works
If you have a service that can be configured to send webhooks, all you have to do is point it to the URL you generate in the Universal Webhook Application available on Symphony Market, and setup your service to post webhook payloads to that URL, in messageML format.

## What formats and events it support and what it produces
Every integration will receive a message sent in a specific format (depending on the system it ingests) and will usually convert it into a Symphony MessageML before it reaches the Symphony platform. It will also, usually, identify the kind of message based on an "event" identifier, which varies based on the third-party system.

The Universal Webhook in the other hand does not support any special events, and it merely forwards the message received (if valid). You can send in either messageMLv2 content, or legacy messageMLv1 content.


#### How to send messages into Symphony using the universal webhook

It deals with messages in the `xml`, `x-www-form-urlencode`, and `form-data` formats.

To send messages using messageMLv2 you can use the following technique:

* Send your payload as `form-data`. You'll need to set the Content-Type to `multipart/form-data` and submit the messageML v2
template in the "message" form field and the Entity JSON in the "data" form field. This option is only available when
 the Integration Bridge posts messages through the Agent that has version equal or greater than '1.46.0'
 
 Your message will need to be compliant with Symphony [MessageML v2](https://symphonyoss.atlassian.net/wiki/display/WGFOS/MessageML+V2+Draft+Proposal+-+For+Discussion)
 
 Here is an example of a message sent through the Universal Webhook, that looks like a Zapier card. You can customize the MessageMLv2 and Entity JSON to your liking, as long as it is valid, it will be passed through and rendereed.
 
* This is the messageML v2 that the Universal Webhook integration received, which defines the layout of the card and how the front end will render it within Symphony:

```xml
<messageML>
    <div class="entity" data-entity-id="zapierPostMessage">
        <card class="barStyle">
            <header>        
	      <span>${entity['zapierPostMessage'].message.header}</span>
            </header>
            <body>
              <span>${entity['zapierPostMessage'].message.body}</span>    
            </body>
        </card>
    </div>
</messageML>
```
* This is the EntityJSON that the Universal Webhook integration received after parsing, which defines the content of the card that the front-end will use in combination with the MessageML v2 to render the card:

```json
{
	"zapierPostMessage": {
		"type": "com.symphony.integration.zapier.event.v2.postMessage",
		"version": "1.0",
		"message" : {
		    "type": "com.symphony.integration.zapier.event.message",
		    "version": "1.0",
		    "header": "New Trello Card Created",
		    "body": "Card Name: Card added for symphony innovate<br/>Card Link: https://trello.com/c/8Md51YdW/15-card-added-for-symphony-innovate"
		}
	}
}
```
When rendered, the above MessageML v2 example will appear like so:

![Rendered MessageML v2](src/docs/images/sample_universal_rendered_v2.png)

To send legacy messages using MessageMLV1 you can use the following techniques:

* If you chose `xml`, you'll need to set the Content-Type to `application/xml` and submit the messageML payload in the message body.

* If you chose `x-www-form-urlencode`, you'll need to set the Content-Type to `application/x-www-form-urlencoded` and submit the messageML payload in the "payload" form field.

Your message will need to be compliant with Symphony [MessageML v1](https://rest-api.symphony.com/docs/message-format/)

* MessageML v1

```sh
<messageML>
This is an example of the sort of text that you can fit within the Universal Webhook Integration. Your service can post updates here!<br/>

<b>You can also bold me</b>: Or not.<br/>

<b>You can submit links as well: </b><a href="https://google.com" /><br/>

<i>You can make text render in italic font</i><br/>
Labels can also come through: <hash tag="label"/> and you can make tickers appear too: <cash tag="GOOG"/><br/>

You can directly mention people using email matching: <mention email="vincent@symphony.com"/><br/>

You can send lists too:<br/>
<ul><li>item1</li><li>item2</li></ul><br/>

You can even send tables:<br/>

<table><tr><td>header1</td><td>header2</td></tr><tr><td>info1</td><td>info2</td></tr><tr><td>info1</td><td>info2</td></tr><tr><td>info1</td><td>info2</td></tr></table>
</messageML>
```
When rendered, the above MessageML v1 example will appear like so:

![Rendered Message](src/docs/images/sample_universal_rendered.png)

# Build instructions for the Java developer

## What you’ll build
You’ll build an integration module to be used with the [Integration Bridge](https://github.com/symphonyoss/App-Integrations-Core).

## What you’ll need
* [JDK 1.8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven 3.0.5+](https://maven.apache.org/download.cgi)
* [Node 6.10](https://nodejs.org/en/)
* [Gulp (globally installed)](http://gulpjs.com/)
* [Webpack (globally installed)](https://webpack.github.io/)

## Build with maven
Universal WebHook Integration is compatible with Apache Maven 3.0.5 or above. If you don’t already have Maven installed you can follow the instructions at maven.apache.org.

To start from scratch, do the following:

1. Build the _App-Integrations-Universal_ dependencies (so you have them in your Maven local repository):
> [_App-Integrations-Commons_](https://github.com/symphonyoss/App-Integrations-Commons)
2. Clone the source repository using Git: `git clone git@github.com:symphonyoss/App-Integrations-Universal.git`
3. cd into _App-Integrations-Universal_
4. Build using maven: `mvn clean install`

## Run locally

1. Define your certificate paths and passwords
```
cp local-run/env.sh.sample env.sh
open env.sh
```

Make sure that
- Paths and passwords are correct
- You can reach all Symphony Pod endpoints
- Service accounts exists and cert CNs match with account's usernames. **_Note: The team is working on a integration-provisioning module that will automate this process; until further notice, please contact [Symphony Support](https://symphony.com/support) to get your Symphony integration deployed on your pod, as the pod will need an exact match of service account name, certs and app name in the pod for your app to be visible in your pod and usable._**
- `./env.sh`, `./application.yaml` and `./certs/` are ignored by Git and don't end up in any code repository

2. Run the integrations
```
./run.sh
```

This command will create an `application.yaml` file in the project root folder, using `local-run/application.yaml.template` as template.

## Expose local endpoint to a public host

In order to be able to create the app in the Foundation pod, you must provide a public `App Url`; you can use [ngrok](https://ngrok.com/) (or similar) to tunnel your local connection and expose it via a public DNS:
```
ngrok http 8080
```
Your local port 8080 is now accessible via `<dynamic_id>.ngrok.io`

If you have a paid subscription, you can also use
```
ngrok http -subdomain=my.static.subdomain 8080
```

## Add your locally running application to the Symphony Market

Adjust your [bundle.json](src/main/webapp/bundle.json) located src/main/webapp/ with the URL you are exposing via ngrok, the configuration and bot id's, and the application context.

**_Note: The team is working on an integration-provisioning module that will automate this process; until further notice, please contact Symphony Support to get your configuration and bot id's.

For the application context, you should always use app/<your app id> provided in the env.sh. That id should also match what you have on [application-universal.yml](src/main/resources/application-universal.yml)

For instance, see apps/universal present in the URL's for the controller.html and appstore-logo.png, as well as in the **context** query parameter for the controller:

```
{
  "applications": [
    {
      "type": "sandbox",
      "id": "devUniversalWebHookIntegration",
      "name": "Universal",
      "blurb": "Universal Webhook Integration in Development Mode",
      "publisher": "Symphony",
      "url": "https://d74a790c.ngrok.io/apps/universal/controller.html?configurationId=58598bf8e4b057438e69f517&botUserId=346621040656485&id=devUniversalWebHookIntegration&context=apps/universal",
      "icon": "https://d74a790c.ngrok.io/apps/universal/img/appstore-logo.png",
      "domain": ".ngrok.io"
    }
  ]
}
```

Access the application icon on your browser to make sure it works and to accept any unsafe certificates (if necessary). In the above example, the URL to acces is https://6f3420e6.ngrok.io/img/appstore-logo.png).

**Run your application again as indicated above, to get the new bundle.js information packaged.**

Launch the Symphony client on your browser, adding your bundle.js as path of the query parameters in the URL. For instance, using the Foundation Dev Pod with the above ngrok sample URL: https://foundation-dev.symphony.com?bundle=https://d74a790c.ngrok.io/apps/universal/bundle.json.

Access the Symphony Market on the browser, and you should be notified to allow unauthorized apps. That is your development app added through bundle.json. Accept the notification and you should see your application in the application list, with the name and description provided in the bundle.json.
