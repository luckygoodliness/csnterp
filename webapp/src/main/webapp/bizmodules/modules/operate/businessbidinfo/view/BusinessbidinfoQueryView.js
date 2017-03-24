Ext.define('Businessbidinfo.view.BusinessbidinfoQueryView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/operate/businessbidinfo',
    aHeight: 480,  //指定内容面板高度
    aWidth: 1200,  //指定内容面板宽度
    cpHeight: 60,//查询条件面板高度
    epHeight: 0,//编辑面板高度
    allowNullConditions: true,//是否允许空查询条件
    queryLayoutFile: 'businessbidinfo-query-layout.xml',
    hideScroll: true,
    canEdit: false
});