<?xml version="1.0"?>
<globals>

    <global id="srcOut" value="${escapeXmlAttribute(srcDir + '/' + slashedPackageName(packageName))}"/>
    <global id="resOut" value="${escapeXmlAttribute(resDir)}"/>

    <global id="dialogClassName" value="${dialogClassNameWithoutPostfix}Dialog"/>
    <global id="dialogRouteClassName" value="${dialogClassNameWithoutPostfix}DialogRoute"/>

</globals>