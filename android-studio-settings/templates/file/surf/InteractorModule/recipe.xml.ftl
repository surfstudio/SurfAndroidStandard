<?xml version="1.0"?>
<recipe>
    <mkdir at="${moduleName}" />
    <mkdir at="${moduleName}/src/main/java/${slashedPackageName(packageName)}/${moduleName}" />
    <merge from="settings.gradle.ftl" to="settings.gradle" />
    <instantiate from="build.gradle.ftl" to="${moduleName}/build.gradle" />
    <instantiate from="AndroidManifest.xml.ftl" to="${moduleName}/src/main/AndroidManifest.xml" />
    <copy from="root://gradle-projects/common/gitignore" to="${moduleName}/.gitignore" />

</recipe>
