# Firebase Sample

Пример использования модулей:

+ [analytics](../analytics)
+ [firebase-analytics](../firebase-analytics)
+ [push](../push)

Посмотреть проект в консоли Firebase можно [здесь](https://console.firebase.google.com/u/1/project/androidstandard-sample-7707a/overview).

Проект доступен для аккаунта surfstudio36@gmail.com

### Инструкция для тестирования пуш-уведомлений

#### URL:
https://fcm.googleapis.com/fcm/send

#### Headers:

Key | Value
--- | ---
Authorization | key=SERVER_KEY
Content-Type | application/json

#### Body:

```
{
    "to" : "FCM_TOKEN",
    "notification" : {
        "body" : "Body",
        "title": "Title"
    },
    "data" : {
        "id" : "1",
        "event": "EVENT_TYPE",
        "previewText": "Preview"
    }
}
```

+ SERVER_KEY можно найти в консоли Firebase:
Project Overview - Settings - Cloud Messaging;

+ FCM_TOKEN можно найти в логах приложения при его запуске;
+ EVENT_TYPE - тип события, возможные значения представлены [здесь](../firebase-sample/src/main/java/ru/surfstudio/android/firebase/sample/domain/notification/NotificationType.kt).