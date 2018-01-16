package ${packageName}

@PerScreen
class ${className}Presenter(basePresenterDependency: BasePresenterDependency) : BasePresenter<${className}${screenTypeCapitalized}View>(basePresenterDependency) {

    override fun onLoad(viewRecreated: Boolean) {
        super.onLoad(viewRecreated)
    }
}