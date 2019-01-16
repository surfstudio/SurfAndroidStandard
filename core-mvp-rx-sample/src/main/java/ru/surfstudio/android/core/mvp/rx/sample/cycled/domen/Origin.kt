/*
 * Copyright (c) 2019-present, SurfStudio LLC.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.surfstudio.android.core.mvp.rx.sample.cycled.domen

sealed class Origin {
    abstract val suffixes: Collection<String>
    abstract val defaultSuffix: String

    companion object {
        val allOrigins = listOf(RomeOrigin(), SOskanOrigin(), EtruscanOrigin(), UmbrianOrigin())
    }
}

class RomeOrigin : Origin() {
    override val suffixes: Collection<String> = listOf("ius", "is", "i")
    override val defaultSuffix: String = suffixes.first()
}

class SOskanOrigin : Origin() {
    override val suffixes: Collection<String> = listOf("enus")
    override val defaultSuffix: String = suffixes.first()
}

class UmbrianOrigin : Origin() {
    override val suffixes: Collection<String> = listOf(
            "as", "anas", "enas", "inas")
    override val defaultSuffix: String = suffixes.first()
}

class EtruscanOrigin : Origin() {
    override val suffixes: Collection<String> = listOf(
            "arna", "erna", "enna", "ina", "inna"
    )
    override val defaultSuffix: String = suffixes.first()
}

class UnknownOrigin : Origin() {
    override val suffixes: Collection<String> = emptyList()
    override val defaultSuffix: String = ""
}