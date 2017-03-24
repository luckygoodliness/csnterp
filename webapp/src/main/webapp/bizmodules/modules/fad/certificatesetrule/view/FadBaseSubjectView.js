Ext.define('Certificatesetrule.view.FadBaseSubjectView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/certificatesetrule',
    aHeight: 480,  //指定内容面板高度
    aWidth: 900,  //指定内容面板宽度
    cpHeight: 100,//查询条件面板高度
    epHeight: 0,//编辑面板高度
    allowNullConditions: true,//是否允许空查询条件
    queryLayoutFile: 'fadbasesubject-query-layout.xml',
    hideScroll: true,
    canEdit: false
});