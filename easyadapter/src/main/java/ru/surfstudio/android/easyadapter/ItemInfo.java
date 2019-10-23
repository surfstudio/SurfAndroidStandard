package ru.surfstudio.android.easyadapter;

/**
 * Content used in unique data checking.
 */
final class ItemInfo {
    private String id;
    private String hash;

    ItemInfo(String id, String hash) {
        this.id = id;
        this.hash = hash;
    }

    String getId() {
        return id;
    }

    String getHash() {
        return hash;
    }
}
