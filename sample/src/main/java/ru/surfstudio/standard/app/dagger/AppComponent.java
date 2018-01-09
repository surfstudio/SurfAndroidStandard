package ru.surfstudio.standard.app.dagger;

import android.content.Context;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import dagger.Component;
import ru.golamago.android.app.intialization.InitializationModule;
import ru.golamago.android.app.intialization.InitializeAppInteractor;
import ru.golamago.android.interactor.analytics.AnalyticsModule;
import ru.golamago.android.interactor.analytics.AnalyticsService;
import ru.golamago.android.interactor.auth.AuthModule;
import ru.golamago.android.interactor.auth.AuthRepository;
import ru.golamago.android.interactor.auth.SessionChangedInteractor;
import ru.golamago.android.interactor.cart.CartInteractor;
import ru.golamago.android.interactor.cart.CartModule;
import ru.golamago.android.interactor.catalog.CatalogInteractor;
import ru.golamago.android.interactor.catalog.CatalogModule;
import ru.golamago.android.interactor.chat.ChatInteractor;
import ru.golamago.android.interactor.chat.ChatModule;
import ru.golamago.android.interactor.common.cache.file.CacheModule;
import ru.golamago.android.interactor.common.network.NetworkModule;
import ru.golamago.android.interactor.common.network.OkHttpModule;
import ru.golamago.android.interactor.common.network.ServerUrlModule;
import ru.golamago.android.interactor.common.network.connection.NetworkConnectionChecker;
import ru.golamago.android.interactor.location.LocationModule;
import ru.golamago.android.interactor.location.LocationService;
import ru.golamago.android.interactor.order.ActiveOrdersCountStorage;
import ru.golamago.android.interactor.order.OrderInteractor;
import ru.golamago.android.interactor.order.OrderModule;
import ru.golamago.android.interactor.order.OrderRepository;
import ru.golamago.android.interactor.order.ShopStorage;
import ru.golamago.android.interactor.place.GooglePlaceModule;
import ru.golamago.android.interactor.place.SuggestService;
import ru.golamago.android.interactor.profile.ProfileInteractor;
import ru.golamago.android.interactor.profile.ProfileModule;
import ru.golamago.android.interactor.profile.ProfilePhoneChangeInteractor;
import ru.golamago.android.interactor.profile.ProfileRepository;
import ru.golamago.android.interactor.progress.InitialProgressStorage;
import ru.golamago.android.interactor.push.PushInteractor;
import ru.golamago.android.interactor.shop.DeliveryDataStorage;
import ru.golamago.android.interactor.shop.ShopInteractor;
import ru.golamago.android.interactor.shop.ShopModule;
import ru.golamago.android.ui.common.image.ImageCacheLoader;
import ru.golamago.android.ui.common.image.ImageCacheModule;
import ru.golamago.android.ui.common.notification.FireBaseMessagingService;
import ru.golamago.android.ui.common.notification.FireBaseModule;
import ru.golamago.android.ui.common.notification.FirebaseInstanceIDService;
import ru.golamago.android.ui.common.remoteconfig.CacheExpiration;
import ru.golamago.android.ui.common.remoteconfig.RemoteConfigModule;
import ru.golamago.android.ui.common.remoteconfig.RemoteConfigRepository;
import ru.golamago.android.ui.util.ProfileUtil;
import ru.surfstudio.android.core.app.SharedPrefModule;
import ru.surfstudio.android.core.app.dagger.scope.PerApplication;
import ru.surfstudio.android.core.app.scheduler.SchedulerModule;
import ru.surfstudio.android.core.app.scheduler.SchedulersProvider;
import ru.surfstudio.android.core.util.ActiveActivityHolder;
import ru.surfstudio.android.core.util.push.ui.FcmTokenProvider;

@PerApplication
@Component(modules = {
        AppModule.class,
        AuthModule.class,
        ProfileModule.class,
        ShopModule.class,
        OrderModule.class,
        CartModule.class,
        CatalogModule.class,
        ChatModule.class,
        ServerUrlModule.class,
        OkHttpModule.class,
        NetworkModule.class,
        CacheModule.class,
        SchedulerModule.class,
        SharedPrefModule.class,
        InitializationModule.class,
        LocationModule.class,
        ActiveActivityHolderModule.class,
        GooglePlaceModule.class,
        AnalyticsModule.class,
        FireBaseModule.class,
        RemoteConfigModule.class,
        ImageCacheModule.class
})
public interface AppComponent {
    Context context();

    SchedulersProvider schedulerProvider();

    NetworkConnectionChecker conNetworkConnectionChecker();

    SessionChangedInteractor sessionChangedInteractor();

    InitializeAppInteractor initializeAppInteractor();

    AuthRepository authService();

    ProfileInteractor profileInteractor();

    ProfilePhoneChangeInteractor profilePhoneChangedInteractor();

    ProfileRepository profileRepository();

    OrderRepository orderRepository();

    LocationService locationService();

    ShopInteractor shopInteractor();

    CartInteractor cartInteractor();

    OrderInteractor orderInteractor();

    CatalogInteractor catalogInteractor();

    ChatInteractor chatIneractor();

    ActiveActivityHolder activeActivityHolder();

    InitialProgressStorage initialProgressStorage();

    SuggestService suggestService();

    DeliveryDataStorage deliveryDataStorage();

    ShopStorage shopStorage();

    ActiveOrdersCountStorage activeOrdersCountStorage();

    ProfileUtil profileUtil();


    PushInteractor pushInteractor();

    FcmTokenProvider fcmTokenProvder();

    FirebaseRemoteConfig fireBaseremoteConfig();

    CacheExpiration cacheExpiration();

    RemoteConfigRepository remoteConfigRepository();

    AnalyticsService analyticsService();

    ImageCacheLoader imageCacheLoader();

    void inject(FirebaseInstanceIDService service);

    void inject(FireBaseMessagingService service);
}
