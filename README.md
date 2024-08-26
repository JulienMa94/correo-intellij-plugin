# Correo Intellij plugin 

<!-- Plugin description -->
This Intellij Plugin is wrapping the opensource MQTT Client CorreoMQTT.
The goal of this plugin is to provide a graphical MQTT client for IntelliJ IDEs and reduce context switching between the IDE and the MQTT client.
<!-- Plugin description end -->

This specific section is a source for the [plugin.xml](/src/main/resources/META-INF/plugin.xml) file which will be extracted by the [Gradle](/build.gradle.kts) during the build process.

To keep everything working, do not remove `<!-- ... -->` sections.

![Build](https://github.com/JulienMa94/intellij-plugin-test/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

## Intellij plugin Todo list
- [x] Create a new [IntelliJ Platform Plugin Template][template] project.
- [x] Get familiar with the [template documentation][template].
- [ ] Adjust the [pluginGroup](./gradle.properties), [plugin ID](./src/main/resources/META-INF/plugin.xml) and [sources package](./src/main/kotlin).
- [x] Adjust the plugin description in `README` (see [Tips][docs:plugin-description])
- [ ] Review the [Legal Agreements](https://plugins.jetbrains.com/docs/marketplace/legal-agreements.html?from=IJPluginTemplate).
- [ ] [Publish a plugin manually](https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html?from=IJPluginTemplate) for the first time.
- [ ] Set the `PLUGIN_ID` in the above README badges.
- [ ] Set the [Plugin Signing](https://plugins.jetbrains.com/docs/intellij/plugin-signing.html?from=IJPluginTemplate) related [secrets](https://github.com/JetBrains/intellij-platform-plugin-template#environment-variables).
- [ ] Set the [Deployment Token](https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html?from=IJPluginTemplate).
- [ ] Click the <kbd>Watch</kbd> button on the top of the [IntelliJ Platform Plugin Template][template] to be notified about releases containing new features and fixes.

## Correo Plugin Roadmap 
- [x] Add toolbar to treeview 
  - [x] Create connection dialog for basic broker information
  - [ ] Add additional information like port forwarding, tls, ssl, lwt, etc.
  - [ ] Add keyring manager for storing passwords & master password
- [x] Improve broker connection information
- [x] Improve icon selection
- [x] Refactor connection tree using Jetbrains components
- [ ] Consider changing the order of subscribe / publish to publish / subscribe
- [ ] Implement the toolbar for the publish / subscribe view
  - [ ] Implement the CorreoMQTT publish view toolbar. Should contain the following buttons:
    - [ ] Clear messages
    - [ ] Open history
  - [ ] Implement the CorreoMQTT publish view toolbar
    - [ ] Clear topics
    - [ ] Open history
- [ ] Fix subscription topic taking to much space
- [ ] Implement the CorreoMQTT history views
  - [ ] Implement the CorreoMQTT history view for publish
    - [ ] save old message as cqm file
    - [ ] Add old message to publish view
  - [ ] Implement the CorreoMQTT history view for subscribe
    - [ ] save old message as cqm file
    - [ ] Add old topic to subscribe view
  - [ ] Improve message item structure / information
    - [ ] Add timestamp
    - [ ] Add QoS
    - [ ] Add Retained


