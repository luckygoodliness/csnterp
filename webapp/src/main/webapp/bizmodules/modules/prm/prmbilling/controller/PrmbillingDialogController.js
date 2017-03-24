Ext.define("Prmbilling.controller.PrmbillingDialogController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Prmbilling.view.PrmbillingDialogView',
    uniqueValidateFields: [],
    extraEvents: [
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.prmbilling.dto.PrmBillingDto',
    queryAction: 'prmbilling-query',
    fillAction: 'prmbilling-fill',
    exportXlsAction: "prmbilling-exportxls",

    afterInit: function () {
        var me = this;
        me.callParent(arguments);
        me.view.getQueryPanel().getCmp("state").sotValue("2");
        me.view.getQueryPanel().getCmp("state").setReadOnly(true);
        me.doQuery();
    },
    doReset: function () {
        var me = this;
        me.callParent(arguments);
        me.view.getQueryPanel().getCmp("state").sotValue("2");
    }
});