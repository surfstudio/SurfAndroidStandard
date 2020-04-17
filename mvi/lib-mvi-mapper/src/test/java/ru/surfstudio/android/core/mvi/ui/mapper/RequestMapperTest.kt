package ru.surfstudio.android.core.mvi.ui.mapper

import org.junit.Test
import ru.surfstudio.android.core.mvp.binding.rx.request.Request
import ru.surfstudio.android.core.mvp.binding.rx.request.data.RequestUi
import ru.surfstudio.android.core.mvp.binding.rx.request.data.SimpleLoading

class RequestMapperTest {

    @Test
    fun emptyMapping() {
        val request = Request.Success("test")
        val requestUi = RequestUi<String>()
        val newRequestUi = RequestMapper.builder(request, requestUi).build()
        assert(!newRequestUi.hasData)
        assert(!newRequestUi.hasError)
        assert(!newRequestUi.isLoading)
    }

    @Test
    fun simpleMapping() {
        run { // no data at all, loading
            val request = Request.Loading<String>()
            val requestUi = RequestUi<String>()
            val newRequestUi = RequestMapper.builder(request, requestUi)
                    .mapData(simpleDataMapper())
                    .mapLoading(simpleLoadingMapper())
                    .build()

            assert(!newRequestUi.hasData)
            assert(newRequestUi.isLoading)
        }

        run { // request success, no local data
            val request = Request.Success("newData")
            val requestUi = RequestUi<String>()
            val newRequestUi = RequestMapper.builder(request, requestUi)
                    .mapData(simpleDataMapper())
                    .mapLoading(simpleLoadingMapper())
                    .build()

            assert(newRequestUi.data == "newData")
            assert(!newRequestUi.isLoading)
        }

        run { // request success, local data exist
            val request = Request.Success("newData")
            val requestUi = RequestUi<String>("oldData")
            val newRequestUi = RequestMapper.builder(request, requestUi)
                    .mapData(simpleDataMapper())
                    .mapLoading(simpleLoadingMapper())
                    .build()

            assert(newRequestUi.data == "newData")
            assert(!newRequestUi.isLoading)
        }

        run { // request failed, local data exist
            val request = Request.Error<String>(NoSuchElementException())
            val requestUi = RequestUi<String>("oldData")
            val newRequestUi = RequestMapper.builder(request, requestUi)
                    .mapData(simpleDataMapper())
                    .mapLoading(simpleLoadingMapper())
                    .build()

            assert(newRequestUi.data == "oldData")
            assert(!newRequestUi.isLoading)
        }
    }

    @Test
    fun errorHandling() {
        var isErrorHandled = false
        val request = Request.Error<String>(NoSuchElementException())
        val requestUi = RequestUi<String>("oldData")
        val newRequestUi = RequestMapper.builder(request, requestUi)
                .mapData(simpleDataMapper())
                .handleError { error, _, _ ->
                    isErrorHandled = error is NoSuchElementException
                    true
                }
                .build()

        assert(newRequestUi.data == "oldData")
        assert(!newRequestUi.isLoading)
        assert(newRequestUi.error is NoSuchElementException)
        assert(isErrorHandled)
    }

    @Test
    fun requestTypeTransformation() {
        run { // string to int transformation
            val request = Request.Success<String>("123")
            val requestUi = RequestUi<Int>(456)
            val newRequestUi = RequestMapper.builder(request, requestUi)
                    .mapRequest { data -> data.toIntOrNull() ?: 0 }
                    .mapData(simpleDataMapper())
                    .mapLoading(simpleLoadingMapper())
                    .build()

            assert(newRequestUi.data == 123)
            assert(!newRequestUi.isLoading)
        }

        run { // list of strings to list of ints transformation
            val request = Request.Success<List<String>>(listOf("1", "2", "3"))
            val requestUi = RequestUi<List<Int>>()
            val newRequestUi = RequestMapper.builder(request, requestUi)
                    .mapRequest { data -> data.map { it.toIntOrNull() ?: 0 } }
                    .mapData(simpleDataMapper())
                    .mapLoading(simpleLoadingMapper())
                    .build()

            assert(newRequestUi.data is List<Int>)
            assert(newRequestUi.data?.get(0) == 1)
            assert(newRequestUi.data?.get(1) == 2)
            assert(newRequestUi.data?.get(2) == 3)
            assert(!newRequestUi.isLoading)
        }
    }

    private fun <T> simpleDataMapper(): RequestDataMapper<T, T, T> = { request, data ->
        request.getDataOrNull() ?: data
    }

    private fun <T> simpleLoadingMapper(): RequestLoadingMapper<T, T> = { request, _ ->
        SimpleLoading(request.isLoading)
    }
}