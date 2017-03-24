Ext.define("Bidcontractplan.controller.BidcontractplanQueryController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Bidcontractplan.view.BidcontractplanQueryView',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'bidcontractplan-query',
    exportXlsAction: "bidcontractplan-exportxls",
    doQuery: function () {
        var me = this;
        window.ACTIVE_WINDOW=me.view.up('window');
        me.callParent(arguments);
    }
});