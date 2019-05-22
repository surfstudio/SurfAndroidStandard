package ru.surfstudio.android.build

class Util {

    private static String value = ""
    static String util() {
        return value
    }

    static init(String val){
        value = val
    }
}