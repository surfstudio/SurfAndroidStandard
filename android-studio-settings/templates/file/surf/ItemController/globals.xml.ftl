<?xml version="1.0"?>
<globals>

    <global id="srcOut" value="${escapeXmlAttribute(srcDir + '/' + slashedPackageName(packageName))}"/>
    <global id="resOut" value="${escapeXmlAttribute(resDir)}"/>

    <global id="controllerClassName" value="${controllerClassNameWithoutPostfix}ItemController"/>
    <global id="controllerItemName" value="${controllerItemClassName?uncap_first}"/>

</globals>
