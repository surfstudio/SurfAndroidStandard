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

package ru.surfstudio.android.core.mvp.binding.rx.ui

/**
 * Интерфейс помечающий класс, который будет служить связью между презентором и вью
 *
 * В классе отмеченном этим интерфейсом могут находится:
 * * Action, Command, State, Bond — для динамического взаимодействия вью и презентора. Т.е. все те
 * действия которые могут быть важны для приложения.
 * * `val` поля которые неизменяются на протяжении жизни всего экрана и должны быть заданы при его создании
 *
 * Все трансформации для приведения к типам полей этого класса рекомендуется делать в презенторе либо вью
 *
 * Рекомендуемый порядок расположения членов класса:
 * 1. Неизменяемые `val` поля. (далее по алфавиту)
 * 2. Action
 * 3. Bond
 * 4. Command
 * 5. State
 */
interface BindModel