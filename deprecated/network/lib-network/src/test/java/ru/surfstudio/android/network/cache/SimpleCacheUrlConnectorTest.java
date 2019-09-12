package ru.surfstudio.android.network.cache;

import android.text.TextUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import ru.surfstudio.android.network.BaseUrl;
import ru.surfstudio.android.network.HttpMethods;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

/**
 * unit test for {@link SimpleCacheUrlConnector}
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(TextUtils.class)
public class SimpleCacheUrlConnectorTest {

    private final BaseUrl baseUrl = new BaseUrl("http://ya.ru/", "v2");
    private SimpleCacheInfo yaSci = new SimpleCacheInfo(HttpMethods.GET, "me/fu", "ya_test", 1);
    private SimpleCacheInfo paramPath = new SimpleCacheInfo(HttpMethods.GET, "me/{param_one}/like", "param_test", 1);
    private SimpleCacheInfo paramVal = new SimpleCacheInfo(HttpMethods.GET, "me/job&ginger=1", "param_test", 1);
    private SimpleCacheUrlConnector connector;

    @Before
    public void setup() {
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.when(TextUtils.isEmpty(any(CharSequence.class)))
                .thenAnswer(invocation -> {
                    CharSequence a = (CharSequence) invocation.getArguments()[0];
                    return !(a != null && a.length() > 0);
                });
        List<SimpleCacheInfo> list = new ArrayList<>();
        list.add(yaSci);
        list.add(paramPath);
        list.add(paramVal);

        connector = new SimpleCacheUrlConnector(baseUrl, list);
    }

    @Test
    public void getSimpleCacheInfo() {
        assertFalse(connector.getSimpleCacheInfo().isEmpty());
    }

    @Test
    public void getByUrl() {
        assertTrue(baseUrl.toString().endsWith("/"));
        assertEquals(yaSci, connector.getByUrl(HttpUrl.parse("http://ya.ru/v2/me/fu"), HttpMethods.GET));
        assertNull(connector.getByUrl(HttpUrl.parse("http://ya.ru/v2/me/ful"), HttpMethods.GET));

        assertEquals(paramPath, connector.getByUrl(HttpUrl.parse("http://ya.ru/v2/me/123/like"), HttpMethods.GET));
        assertEquals(paramPath, connector.getByUrl(HttpUrl.parse("http://ya.ru/v2/me/321/like"), HttpMethods.GET));

        assertEquals(paramVal, connector.getByUrl(HttpUrl.parse("http://ya.ru/v2/me/job&ginger=1"), HttpMethods.GET));
        assertNull(connector.getByUrl(HttpUrl.parse("http://ya.ru/v2/me/job&ginger=2"), HttpMethods.GET));

        assertEquals(yaSci, connector.getByUrl(HttpUrl.parse("http://ya.ru/v3/me/fu"), HttpMethods.GET)); //api version template


    }

}