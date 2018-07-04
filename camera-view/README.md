#Camera view
Поставляет вью для вывода изображения с камеры в реальном времени.

#Использование
##Layout
Добавьте в `layout` ресурс элемент. 
```xml
    <com.google.android.cameraview.CameraView
        android:layout_width="20dp"
        android:layout_height="30dp" />
```
[атрибуты](/src/main/res/values/attrs.xml)

##Жизненный цикл
Вью должно быть связано с жизенным циклом активити/фрагмента для освобождения ресурса камеры. В противном случае к камере не удастся обратится из другого места.
```
onResume() { 
    ... 
    cameraView.start() 
}
onPause() {
    ...
    cameraView.stop() 
}
```
##Разрешения(permissions)
Необходим одобренный `android.permission.CAMERA` для корректной работы перед вызовом `start()`, иначе получим `java.lang.RuntimeException: Fail to connect to camera service`

#Подключение
Gradle:
```
    implementation "ru.surfstudio.android:camera-view:X.X.X"
```