package ru.surfstudio.android.navigation.observer.deprecated.executor;

import ru.surfstudio.android.navigation.observer.deprecated.ScreenResultEmitter;
import ru.surfstudio.android.navigation.observer.deprecated.command.EmitScreenResult;
import ru.surfstudio.android.navigation.observer.deprecated.route.ResultRoute;
import ru.surfstudio.android.navigation.route.BaseRoute;

public class ScreenResultDispatcher {

    public ScreenResultDispatcher() {
        //empty
    }

    public void dispatch(ScreenResultEmitter emitter, EmitScreenResult command) {
        emitter.emit(castTargetRoute(command.getRoute()), command.getResult());
    }

    private <T extends BaseRoute & ResultRoute> T castTargetRoute(BaseRoute route) {
        return (T) route;
    }
}
