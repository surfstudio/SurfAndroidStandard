<#import "macros/select_type_view_macros.ftl" as superClass>
package ${packageName};


public class ${className}${screenTypeCapitalized}View extends <@superClass.selectTypeView /> {

    @Inject
    ${className}Presenter presenter;

    <#if generateToolbar>
    private Toolbar toolbar;
    </#if>
    <#if (screenType=='activity' && typeViewActivity!='1' && typeViewActivity!='2') || (screenType=='fragment' && typeViewFragment!='1' && typeViewFragment!='2')>
    private PlaceHolderViewImpl placeHolderView;
    </#if>
    <#if (screenType=='activity' && typeViewActivity!='1' && typeViewActivity!='2' && typeViewActivity!='3') || (screenType=='fragment' && typeViewFragment!='1' && typeViewFragment!='2' && typeViewFragment!='3')>
    private SwipeRefreshLayout swipeRefreshLayout;
    </#if>

    <#if generateRecyclerView>
        private RecyclerView recyclerView;
        <#if (screenType=='activity' && typeViewActivity=='5') || (screenType=='fragment' && typeViewFragment=='5')>
            private PaginationableAdapter adapter;
        <#else>
            private EasyAdapter adapter;
        </#if>
    </#if>

    @Override
    public CorePresenter[] getPresenters() {
        return new CorePresenter[]{presenter};
    }

    <#if screenType=='activity'>
        @Override
        public ScreenConfigurator createScreenConfigurator(Activity activity, Intent intent) {
            return new ${className}ScreenConfigurator(activity, intent);
        }

        @Override
        public BaseActivityConfigurator createActivityConfigurator() {
            return new ActivityConfigurator(this);
        }
     
        @Override
        public int getContentView() {
            return R.layout.${layoutName};
        }
    <#else>
        @Override
        public ScreenConfigurator createScreenConfigurator(Activity activity, Bundle args) {
            return new ${className}ScreenConfigurator(activity, args);
        }

        @Override
        public View onCreateView(LayoutInflater inflater,
                                 @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.${layoutName}, container, false);
        }
    </#if>

    <#if screenType=='activity'>
    @Override
    public void onCreate(Bundle savedInstanceState,
                         @Nullable PersistableBundle persistentState,
                         boolean viewRecreated) {
        findViews(getWindow().getDecorView());
    <#else>
    @Override
    public void onActivityCreated(Bundle savedInstanceState, boolean viewRecreated) {
         findViews(getView());
   </#if>
        <#if generateToolbar>
        initToolbar();
        </#if>
        initListeners();
        <#if generateRecyclerView>
        initRecyclerView();
        </#if>
    }

    <#if (screenType=='activity' && typeViewActivity!='1') || (screenType=='fragment' && typeViewFragment!='1')>
        <#if (screenType=='activity' && typeViewActivity!='2') || (screenType=='fragment' && typeViewFragment!='2')>
            @Override
            protected PlaceHolderView getPlaceHolderView() {
                return placeHolderView;
            }

            <#if (screenType=='activity' && typeViewActivity!='3') || (screenType=='fragment' && typeViewFragment!='3')>
                @Override
                protected SwipeRefreshLayout getSwipeRefreshLayout() {
                    return swipeRefreshLayout;
                }

                <#if (screenType='activity' && typeViewActivity!='4') || (screenType=='fragment' && typeViewFragment!='4')>
                    @Override
                    protected BasePaginationableAdapter getPaginationableAdapter() {
                        return null;
                    }
                </#if>
            </#if>
        </#if>

        @Override
        protected void renderInternal(${className}ScreenModel screenModel) {
        }
    </#if>

    private void findViews(View view) {
        <#if generateToolbar>
        toolbar = view.findViewById(R.id.toolbar);
        </#if>
        <#if (screenType=='activity' && typeViewActivity!='1' && typeViewActivity!='2') || (screenType=='fragment' && typeViewFragment!='1' && typeViewFragment!='2')>
        placeHolderView = view.findViewById(R.id.placeholder);
        </#if>
        <#if (screenType=='activity' && typeViewActivity!='1' && typeViewActivity!='2' && typeViewActivity!='3') || (screenType=='fragment' && typeViewFragment!='1' && typeViewFragment!='2' && typeViewFragment!='3')>
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        </#if>
    }

    <#if generateToolbar>
    private void initToolbar() {
    }
    </#if>

    private void initListeners() {
    }

    <#if generateRecyclerView>
    private void initRecyclerView() {
    }
    </#if>
}
