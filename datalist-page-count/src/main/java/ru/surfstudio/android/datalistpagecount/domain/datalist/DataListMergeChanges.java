package ru.surfstudio.android.datalistpagecount.domain.datalist;

/**
 * результат изменения списка после {@link DataList#merge(DataList)}
 */
public class DataListMergeChanges {
    private final int lastSize;
    private final int newBlockSize;
    private final int newBlockStartPage;
    private final int pageSize;

    public DataListMergeChanges(int lastSize, int newBlockSize, int newBlockStartPage, int pageSize) {
        this.lastSize = lastSize;
        this.newBlockSize = newBlockSize;
        this.newBlockStartPage = newBlockStartPage;
        this.pageSize = pageSize;
    }

    public int getLastSize() {
        return lastSize;
    }

    public int getNewBlockSize() {
        return newBlockSize;
    }

    public int getNewBlockStartPage() {
        return newBlockStartPage;
    }

    public int getPageSize() {
        return pageSize;
    }
}
