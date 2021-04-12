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

package ru.surfstudio.android.core.mvp.binding.rx.relation.mvp

import ru.surfstudio.android.core.mvp.binding.rx.relation.RelationEntity

/*
 * Интерфейсы для обозначения направлений связи
 */

interface BondSource : RelationEntity
interface BondTarget : RelationEntity

interface ActionSource : RelationEntity
interface ActionTarget : RelationEntity

interface StateSource : RelationEntity
interface StateTarget : RelationEntity

interface CommandSource : RelationEntity
interface CommandTarget : RelationEntity

object VIEW : BondSource, BondTarget, ActionSource, StateTarget, CommandTarget, RelationEntity
object PRESENTER : BondSource, BondTarget, ActionTarget, StateSource, StateTarget, CommandSource, CommandTarget, RelationEntity