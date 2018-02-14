package ru.surfstudio.android.datalistlimitoffset.domain.datalist;

/**
 * несовместимые блоки данных
 */
public class IncompatibleRangesException extends IllegalArgumentException {

    public IncompatibleRangesException(String s) {
        super(s);
    }
}
