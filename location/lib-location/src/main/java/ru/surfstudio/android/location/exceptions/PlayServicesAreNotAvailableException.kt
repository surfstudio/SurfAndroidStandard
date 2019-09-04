/*
 * Copyright 2016 Valeriy Shtaits.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.surfstudio.android.location.exceptions

/**
 * Исключение, возникающее, если Google Play Services недоступны.
 *
 * @param connectionResult {@link ConnectionResult} результат подключения при проверке доступности Google Play Services.
 * Может быть одним из следующих значений: SERVICE_MISSING, SERVICE_UPDATING, SERVICE_VERSION_UPDATE_REQUIRED,
 * SERVICE_DISABLED, SERVICE_INVALID
 */
class PlayServicesAreNotAvailableException(val connectionResult: Int) : RuntimeException()