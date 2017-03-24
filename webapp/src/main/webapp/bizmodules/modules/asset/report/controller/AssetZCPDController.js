Ext.define("AssetReport.controller.AssetZCPDController", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'AssetReport.view.AssetZCPDView',
    queryAction: 'assetReport-query',
    extraEvents: [],
    beforeQuery: function () {
        var me = this;
        var view = me.view;
        return true;
    },
    afterInit: function () {
        var me = this;
        var view = me.view;
    }
});