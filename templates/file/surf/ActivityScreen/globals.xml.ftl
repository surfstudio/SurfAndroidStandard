<?xml version="1.0"?>
<globals>
    <global id="manifestOut" value="${manifestDir}" />
    <global id="srcOut" value="${srcDir}/${slashedPackageName(packageName)}" />
    <global id="resOut" value="${resDir}" />
    <global id="nameParam" value="${extractLetters(nameTypeData?lower_case)}" />
    <global id="folderController" value="list" />
    
    <global id="defPostfixController" value="ItemController" />
    <global id="defPostfixView" value="ActivityView" />
    <global id="defPostfixScreenModel" value="ScreenModel" />
    <global id="defPostfixRoute" value="ActivityRoute" />
    <global id="defPostfixPresenter" value="Presenter" />
    <global id="defPostfixScreenConfigurator" value="ScreenConfigurator" />
</globals>
