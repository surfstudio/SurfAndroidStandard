package ru.surfstudio.android.recycler.decorator.sample.sample

import androidx.annotation.IntDef

@Retention(AnnotationRetention.SOURCE)
@IntDef(
    Rules.MIDDLE,
    Rules.END
)
annotation class DividerRule