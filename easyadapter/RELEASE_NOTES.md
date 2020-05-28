# Easy-adapter Release Notes

- [1.1.0-alpha](#110-alpha)
- [1.0.0](#100)
- [0.3.0](#030)
- [0.2.1](#021)

## 1.1.0-alpha
##### Easyadapter
* ANDDEP-646 - Improved EasyAdapter. In BaseItem added next and previous links to items, adapterPosition, position in ItemList.
* ANDDEP-795 - Fixed the appear of the invisible item in all rows except first in case of using GridLayoutManager
* ANDDEP-784 Added unbind method to BaseItemController, fixed view type collision by generating each id for item controller class 
* Added support for asynchronous item list updates diff calculation
* ANDDEP-812 - Fixed crash after enabling infinite scroll, fixed scroll position saving mechanism
* ANDDEP-829 Fixed synchronous DiffResult dispatching in DefaultDiffer.
* ANDDEP-785 Added method getItem() in EasyAdapter, to get BaseItem by position
* ANDDEP-785 Added getter in EasyAdapter, for *firstInvisibleItemEnabled* property
* **NO BACKWARD COMPATIBILITY** ANDDEP-909 Removed
  `setHasStableIds(true)` in EasyAdapter.
* **NO BACKWARD COMPATIBILITY** ANDDEP-958 Change getItemId and
  getItemHash to Object for BaseItemController; getItemId became
  abstract
* ANDDEP-1017 Added DiffResultListener invoked after the DiffResult calculation
* ANDDEP-1038 **NO BACKWARD COMPATIBILITY**
  * Move `BasePaginationableAdapter` to easyadapter-pagination module
    and rename to `EasyPaginationAdapter`
  * Move `PaginationState` to easyadapter-pagination
  * Add `OnShowMoreListener` and `paginationFooterController` as
    constructor params for `EasyPaginationAdapter`. Remove
    `getPaginationFooterController` method
  * Make `EasyPaginationAdapter` not abstract
  * Add `shouldShowMoreElements` for `EasyPaginationAdapter` in order to
    implement custom logic for showing more elements trigger
* ANDDEP-1044 **NO BACKWARD COMPATIBILITY**
  * Remove animations methods from `BaseViewHolder` to interface `AnimatableViewHolder`
  * Move `AnimatableViewHolder` and `BaseItemAnimator` to
    `recycler-extension` module
  * Rename `BaseItemAnimator` to `ViewHolderItemAnimator`
* ANDDEP-853 Translate README
##### Easyadapter Pagination
* ANDDEP-1038 Add easyadapter-pagination module
* ANDDEP-853 Translate README
## 1.0.0
##### Easyadapter
* ANDDEP-270 Added support for asynchronous view inflate in ViewHolder
## 0.3.0
##### Easyadapter
* ANDDEP-200 - Removed ViewType randomization from ItemController
* `getItemId` at the controller - returns String
## 0.2.1
##### Easyadapter
* ANDDEP-145 Added to BasePaginationableAdpater StaggeredGrid Support
* ANDDEP-142 fixed a bug with the wrong addition of an element in the ItemList.addStickyHeaderIf() method