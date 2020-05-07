/*
 * Copyright 2016 Maxim Tuev.
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
package ru.surfstudio.android.easyadapter.pagination;

/**
 * States for {@link EasyPaginationAdapter}.
 */
public enum PaginationState {

    /**
     * The pagination is over, nothing to load, nothing to show
     */
    COMPLETE(false),

    /**
     * Pagination is not over yet, should show loader.
     */
    READY(true),
    /**
     * Error in pagination, should show error message and "Retry" button.
     */
    ERROR(true);     //footer button "showSimpleDialog more"

    /**
     * Means that list has pagination footer.
     */
    boolean visible;

    PaginationState(boolean visible) {
        this.visible = visible;
    }

    /**
     * Get the current pagination footer visibility
     *
     * @return is pagination footer visible
     */
    public boolean isVisible() {
        return visible;
    }
}
