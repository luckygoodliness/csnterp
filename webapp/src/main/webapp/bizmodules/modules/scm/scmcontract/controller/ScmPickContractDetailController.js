/**
 * Created by lenovo on 2015/9/29.
 * 合同发票录入界面选择合同明细到入库单中
 */
Ext.define("Scmcontract.controller.ScmPickContractDetailController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Scmcontract.view.ScmPickContractDetailView',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'contractdetail-query',
    afterInit: function () {
        var me = this;
        var view = me.view;
//        if (me.actionParams) {
//            var queryPanelCmp = view.getQueryPanel();
//            var supplierCodeCmp = queryPanelCmp.getCmp("supplierCode");
//            var supplierNameCmp = queryPanelCmp.getCmp("supplierName");
//            var supplierCodeSCmp = queryPanelCmp.getCmp("supplierCodeS");
//            var notInRow = me.actionParams.notInRow;
//            var supplierId=me.actionParams.supplierId;
//            var supplierName=me.actionParams.supplierName;
////            var isupplierIdCmp = queryPanelCmp.getCmp("isStandard")
//            supplierCodeSCmp.sotValue(notInRow)
//            if(!supplierId.isEmpty&&supplierId!=''){
//                supplierCodeCmp.sotValue(supplierId);
//                supplierNameCmp.sotValue(supplierName);
//                supplierNameCmp.sotEditable(false);
//                me.doQuery();
//            }
//        }
    }
});