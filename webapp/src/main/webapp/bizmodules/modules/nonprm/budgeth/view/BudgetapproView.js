Ext.define('Budgeth.view.BudgetapproView', {
    extend: 'Scdp.mvc.AbstractView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/nonprm/budgeth',
    aHeight: 400,  //指定内容面板高度
    aWidth: 850,  //指定内容面板宽度
    hideScroll: true,
    layoutFile: 'budgetappro-detail-layout.xml',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
    }
});
