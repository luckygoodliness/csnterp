/**
 * Created by lenovo on 2015/9/29.
 */
Ext.define('Scmcontract.view.ScmcontractQueryView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/scm/scmcontract',
    aHeight: 480,  //指定内容面板高度
    aWidth: 900,  //指定内容面板宽度
    cpHeight: 100,//查询条件面板高度
    epHeight: 0,//编辑面板高度
    allowNullConditions: true,//是否允许空查询条件
    queryLayoutFile: 'jscmcontract-query-layout.xml',
    hideScroll: true,
    canEdit: false,
    selMode: 'SINGLE'
});