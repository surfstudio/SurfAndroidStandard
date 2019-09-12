package ru.surfstudio.android.firebase.sample.interactor.common.analytics.event

import ru.surfstudio.android.firebaseanalytics.api.FirebaseAnalyticSetUserPropertyAction

class SetUserProperty(value1 :String, value2: Double)
    : FirebaseAnalyticSetUserPropertyAction(value1, value2.toString())