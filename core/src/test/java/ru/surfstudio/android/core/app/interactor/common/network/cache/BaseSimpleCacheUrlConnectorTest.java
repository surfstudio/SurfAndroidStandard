package ru.surfstudio.android.core.app.interactor.common.network.cache;

import android.text.TextUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import okhttp3.HttpUrl;
import ru.surfstudio.android.core.app.interactor.common.network.HttpMethods;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;

/**
 *
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest(TextUtils.class)
public class BaseSimpleCacheUrlConnectorTest {
    private BaseSimpleCacheUrlConnector connector = new BaseSimpleCacheUrlConnector() {
        @Override
        Collection<SimpleCacheInfo> getSimpleCacheInfo() {
            Set<SimpleCacheInfo> set = new HashSet<>();
            set.add(new SimpleCacheInfo(HttpMethods.GET, "http://ya.ru/v2/me/fu", "ya_test", 1));
            return set;
        }

        @Override
        int getApiVersionSegmentOrder() {
            return 3;
        }
    };

    @Before
    public void setup() {
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.when(TextUtils.isEmpty(any(CharSequence.class)))
                .thenAnswer(invocation -> {
                    CharSequence a = (CharSequence) invocation.getArguments()[0];
                    return !(a != null && a.length() > 0);
                });
    }

    @Test
    public void getSimpleCacheInfo() throws Exception {
        assertFalse(connector.getSimpleCacheInfo().isEmpty());
    }

    @Test
    public void getApiVersionSegmentOrder() throws Exception {
    }

    @Test
    public void getByUrl() throws Exception {
        assertEquals(connector.getByUrl(HttpUrl.parse("http://ya.ru/v2/me/fu"), HttpMethods.GET), "");
    }

}