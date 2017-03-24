/**
 * Created by lenovo on 2016/7/20.
 */
Ext.define("Supplierinfor.controller.PickSupplierQueryController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Supplierinfor.view.PickSupplierQueryView',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'picksupplierinfor-query',
    afterInit: function () {
        var me = this;
        var view = me.view;
        if (me.actionParams) {
            var queryPanelCmp = view.getQueryPanel();
            var supplierCodeSCmp = queryPanelCmp.getCmp("uuid");
            var  supplierGenreCmp=queryPanelCmp.getCmp("changeType");
            var notInRow = me.actionParams.notInRow;
            var changeType=me.actionParams.changeType;
            supplierCodeSCmp.sotValue(notInRow);
            supplierGenreCmp.sotValue(changeType)
            if (changeType=="1") {
                me.view.getQueryPanel().getCmp("supplierGenre").sotValue("4");
                me.view.getQueryPanel().getCmp("supplierGenre").sotEditable(false);
            }
        }
        //me.doQuery();
    },
    doReset: function () {
        var me = this;
        me.callParent(arguments);
        var changeType=me.actionParams.changeType;

        if (changeType != null && changeType != '') {
            me.view.getQueryPanel().getCmp("changeType").sotValue(changeType);
        }
        if (changeType=="1") {
            me.view.getQueryPanel().getCmp("supplierGenre").sotValue("4");
        }
    }


});