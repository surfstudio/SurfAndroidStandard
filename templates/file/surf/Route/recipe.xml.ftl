<?xml version="1.0"?>
<recipe>
  <#if typeRoute=='1'>
    <instantiate from="src/app_package/${defPostfixActivityRoute}.java.ftl"
             to="${escapeXmlAttribute(srcOut)}/${className}${defPostfixActivityRoute}.java" />
     <open file="${escapeXmlAttribute(srcOut)}/${className}${defPostfixActivityRoute}.java" />
  </#if>
  <#if typeRoute=='2'>
    <instantiate from="src/app_package/${defPostfixFragmentRoute}.java.ftl"
             to="${escapeXmlAttribute(srcOut)}/${className}${defPostfixFragmentRoute}.java" />
     <open file="${escapeXmlAttribute(srcOut)}/${className}${defPostfixFragmentRoute}.java" />
  </#if>
  <#if typeRoute=='3'>
    <instantiate from="src/app_package/${defPostfixDialogRoute}.java.ftl"
             to="${escapeXmlAttribute(srcOut)}/${className}${defPostfixDialogRoute}.java" />
     <open file="${escapeXmlAttribute(srcOut)}/${className}${defPostfixDialogRoute}.java" />
  </#if>
</recipe>
