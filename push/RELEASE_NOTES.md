# Push Release Notes

- [0.6.0-alpha](#060-alpha)
- [0.5.0](#050)
- [0.4.0](#040)
- [0.3.0](#030)

## 0.6.0-alpha
##### Push
* Fix PendingIntent flags for Android 12 support
* ANDDEP-1241 Fix unique pushes implementation
## 0.5.0
##### Push
* ANDDEP-930 Added support to custom actions in the notification. Added corresponding listener to the `PushEventListener`.
* **NO BACKWARD COMPATIBILITY** Before in PushInteractor you received `BaseNotificationTypeData<T>`, now in the `PushInteractor` you must register your listener to the object type which is used in the `BaseNotificationTypeData<T>` or its inherit classes.
* ANDDEP-1148 code updated for target sdk 30
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