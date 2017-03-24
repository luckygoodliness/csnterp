Ext.define('Cdmfile.view.CdmFilePopupView', {
    extend: 'Scdp.mvc.AbstractView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/cdm/cdmfile',
    aHeight: 400,  //指定内容面板高度
    aWidth: 900,  //指定内容面板宽度
    //cpHeight: 30,
    //epHeight: 50,
    allowNullConditions: true,
    layoutFile: 'cdm-file-popup-layout.xml',
    bindings: ['cdmFileRelationDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true
});