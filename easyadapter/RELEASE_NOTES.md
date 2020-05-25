[TOC]
# Easy-adapter Release Notes
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
* ANDDERP-1052 Added class for testing
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