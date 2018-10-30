import ru.surfstudio.android.core.mvp.activity.BaseRenderableActivityView;
import ru.surfstudio.android.core.mvp.configurator.BaseActivityViewConfigurator;
import ru.surfstudio.android.core.mvp.presenter.CorePresenter;

/**
 * Created by vsokolova on 15.03.18.
 */

public class MockActivityView extends BaseRenderableActivityView<MockScreenModel> {
    @Override
    protected void renderInternal(MockScreenModel sm) {

    }

    @Override
    public BaseActivityViewConfigurator createConfigurator() {
        return null;
    }

    @Override
    protected CorePresenter[] getPresenters() {
        return new CorePresenter[0];
    }

    @Override
    public String getScreenName() {
        return null;
    }

    @Override
    protected int getContentView() {
        return 0;
    }
}
