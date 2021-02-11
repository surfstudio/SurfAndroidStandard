/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.rx.extension.scheduler;

import io.reactivex.Scheduler;

/**
 * Abstract entity which can provide few types of {@link Scheduler}.
 */
public interface SchedulersProvider {

    /**
     * Get a scheduler for task execution in the UI-thread.
     */
    Scheduler main();

    /**
     * Get a scheduler for task execution in the background thread.
     */
    Scheduler worker();

    /**
     * Get a scheduler for non-blocking computational execution in the background thread.
     */
    Scheduler computation();
}
