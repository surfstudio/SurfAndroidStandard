package ru.surfstudio.android.navigation.observer.executor;

import ru.surfstudio.android.navigation.observer.ScreenResultEmitter;
import ru.surfstudio.android.navigation.observer.command.EmitScreenResult;
import ru.surfstudio.android.navigation.observer.route.ResultRoute;
import ru.surfstudio.android.navigation.route.BaseRoute;

public class ScreenResultDispatcher {

    public ScreenResultDispatcher() {
        //empty
    }

    public void dispatch(ScreenResultEmitter emitter, EmitScreenResult command) {
        emitter.emit(command.getSourceRoute(), castTargetRoute(command.getRoute()), command.getResult());
    }

    private <T extends BaseRoute & ResultRoute> T castTargetRoute(BaseRoute route) {
        return (T) route;
    }
}
