[TOC]
# Push Release Notes
## 0.5.0-alpha
##### Push
* ANDDEP-950 Add different gradle script for modules without deploy
## 0.4.0
##### Push
* NotificationCenter removed - use `PushHandler`
* Added push notification grouping
* Now you can subscribe to events such as opening and rejecting push notifications
* by default, when you click on the push notification, the `PushEventListener` callback is called, in order to change the behavior, you can override preparePendingIntent in` PushHandlerStrategy`
## 0.3.0
##### Push
* NotificationCenter - Deprecated
* The main one is `PushHandler` and its implementation
* Now there is the ability to configure the assistant through the dagger.
* It is possible to subscribe to push through `PushInteractor`
* Added `FcmStorage`