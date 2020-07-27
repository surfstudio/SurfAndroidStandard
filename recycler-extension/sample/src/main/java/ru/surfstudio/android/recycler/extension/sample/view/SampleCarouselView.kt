package ru.surfstudio.android.recycler.extension.sample.view

import android.content.Context
import android.util.AttributeSet
import ru.surfstudio.android.recycler.extension.CarouselView
import ru.surfstudio.android.recycler.extension.sample.domain.Data

class SampleCarouselView @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null
) : CarouselView<Data>(context, attributeSet)