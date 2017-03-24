/**
 * Created by fisher on 2015/11/4.
 */
Ext.define('Erpoffice.view.OfficePickView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/erpsys/erpoffice',
    aHeight: 500,  //指定内容面板高度
    aWidth: 950,  //指定内容面板宽度
    cpHeight: 100,//查询条件面板高度
    epHeight: 0,//编辑面板高度
    allowNullConditions: true,//是否允许空查询条件
    queryLayoutFile: 'erpoffice-query-layout.xml',
    hideScroll: true,
    canEdit: false
});