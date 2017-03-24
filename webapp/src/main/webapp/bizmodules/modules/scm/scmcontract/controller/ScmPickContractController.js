/**
 * Created by lenovo on 2015/9/29.
 * 合同发票录入界面选择相关合同界面
 */
Ext.define("Scmcontract.controller.ScmPickContractController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Scmcontract.view.ScmPickContractView',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'invoicecontract-query',
    afterInit: function () {
        var me = this;
        var view = me.view;
        if (me.actionParams) {
            var queryPanelCmp = view.getQueryPanel();
            var supplierCodeCmp = queryPanelCmp.getCmp("supplierCode");
            var supplierNameCmp = queryPanelCmp.getCmp("supplierName");
            var supplierCodeSCmp = queryPanelCmp.getCmp("supplierCodeS");
            var notInRow = me.actionParams.notInRow;
            var supplierId = me.actionParams.supplierId;
            var supplierName = me.actionParams.supplierName;
//            var isupplierIdCmp = queryPanelCmp.getCmp("isStandard")
            supplierCodeSCmp.sotValue(notInRow);
            if (Scdp.ObjUtil.isNotEmpty(supplierId)) {
                supplierCodeCmp.sotValue(supplierId);
                supplierNameCmp.sotValue(supplierName);
                supplierNameCmp.sotEditable(false);
            }
        }
        me.doQuery();
    },
    doReset: function () {
        var me = this;
        me.callParent(arguments);
        var supplierId = me.actionParams.supplierId;
        var notInRow = me.actionParams.notInRow;
        var supplierName = me.actionParams.supplierName;
        if (supplierId != null && supplierId != '') {
            me.view.getQueryPanel().getCmp("supplierCode").sotValue(supplierId);
            me.view.getQueryPanel().getCmp("supplierName").sotValue(supplierName);
        }
        if (notInRow != null && notInRow != '') {
            me.view.getQueryPanel().getCmp("supplierCodeS").sotValue(notInRow);
        }
    }

});