<?xml version="1.0"?>
<recipe>
    <merge from="AndroidManifest.xml.ftl"
             to="${escapeXmlAttribute(manifestOut)}/AndroidManifest.xml" />
    <instantiate from="src/app_package/ActivityView.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${className}ActivityView.java" />
    <#if isUseScreenModel>
       <instantiate from="src/app_package/ScreenModel.java.ftl"
                      to="${escapeXmlAttribute(srcOut)}/${className}ScreenModel.java" />
    </#if>
    <instantiate from="src/app_package/Presenter.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${className}Presenter.java" />
    <instantiate from="src/app_package/ScreenConfigurator.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${className}ScreenConfigurator.java" />
    <instantiate from="src/app_package/ActivityRoute.java.ftl"
                   to="${escapeXmlAttribute(srcOut)}/${className}ActivityRoute.java" />

    <instantiate from="res/layout/activity_layout.xml.ftl"
                   to="${escapeXmlAttribute(resOut)}/layout/${layoutName}.xml" />

    <#if isUseController>
       <instantiate from="src/app_package/Controller.java.ftl"
             to="${escapeXmlAttribute(srcOut)}/${folderController}/${nameController}${defPostfixController}.java" />
      <instantiate from="res/layout/controller_layout.xml.ftl"
                   to="${escapeXmlAttribute(resOut)}/layout/${nameRes}.xml" />
    </#if>
     <open file="${escapeXmlAttribute(srcOut)}/${className}ActivityView.java" />
</recipe>
