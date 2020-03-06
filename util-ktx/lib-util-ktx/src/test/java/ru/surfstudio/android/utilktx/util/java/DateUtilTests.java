package ru.surfstudio.android.utilktx.util.java;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DateUtilTests {

    @Test
    public void parsingOfNullStringShouldReturnNull() {
        assertNull(DateUtil.parseDate(null));
    }

    @Test
    public void parsingOfValidDateShouldNotReturnNull() {
        assertNotNull(DateUtil.parseDate("10.10.2019", DateUtil.DATE_FORMAT_FULL));
    }

    @Test
    public void parsingOfInvalidDateShouldReturnNull() {
        assertNull(DateUtil.parseDate("2019-10-10", DateUtil.DATE_FORMAT_FULL));
    }

    @Test
    public void reformatOfValidInputShouldReturnValidOutput() {
        final String inputDate = "10.10.2019";
        final String outputDate = "2019-10-10";
        final String inputFormat = "dd.MM.yyyy";
        final String outputFormat = "yyyy-MM-dd";

        assertEquals(outputDate, DateUtil.reformatDate(inputDate, inputFormat, outputFormat));

    }

    @Test
    public void reformatOfNullShouldReturnNull() {
        final String inputFormat = "dd.MM.yyyy";
        final String outputFormat = "yyyy-MM-dd";

        assertNull(DateUtil.reformatDate(null, inputFormat, outputFormat));
    }

    @Test
    public void reformatOfInvalidInputShouldReturnNull() {
        final String inputDate = "2019-10-10";
        final String inputFormat = "dd.MM.yyyy";
        final String outputFormat = "yyyy-MM-dd";

        assertNull(DateUtil.reformatDate(inputDate, inputFormat, outputFormat));
    }
}
