/**
 * Created by lenovo on 2015/9/29.
 */
Ext.define('Scmcontract.view.ScmPayPickContractView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/scm/scmcontract',
    aHeight: 500,  //指定内容面板高度
    aWidth: 1100,  //指定内容面板宽度
    cpHeight: 100,//查询条件面板高度
    epHeight: 0,//编辑面板高度
    xpHeight: 30,//扩展面板高度,
    allowNullConditions: true,//是否允许空查询条件
    queryLayoutFile: 'paycontract-query-layout.xml',
    extraLayoutFile: 'paycontract-extra-layout.xml',
    extraPanelCollapsible: false,
    extraPanelTitle: '',
    hideScroll: true,
    canEdit: false,
    afterInit: function () {
        var me = this;
        me.up('window').dockedItems.items[1].items.items[3].setText("关闭");
    }
});