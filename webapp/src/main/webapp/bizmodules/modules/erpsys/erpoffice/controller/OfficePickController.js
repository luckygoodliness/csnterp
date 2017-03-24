/**
 * Created by fisher on 2015/11/4.
 */
/**
 * Created by lenovo on 2015/9/29.
 * 合同发票录入界面选择相关合同界面
 */
Ext.define("Erpoffice.controller.OfficePickController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Erpoffice.view.OfficePickView',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'erpoffice-query',
    afterInit: function () {
        var me = this;
        var view = me.view;
        if (me.actionParams) {
            var queryPanelCmp = view.getQueryPanel();
            var orgCode = queryPanelCmp.getCmp("orgCodeS");
            var notInRow = me.actionParams.notInRow;
            orgCode.sotValue(notInRow);
        }
        me.doQuery();
    },
    doReset: function () {
        var me = this;
        me.callParent(arguments);

    }

});