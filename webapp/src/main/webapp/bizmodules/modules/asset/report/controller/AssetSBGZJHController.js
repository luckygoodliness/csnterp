Ext.define("AssetReport.controller.AssetSBGZJHController", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'AssetReport.view.AssetSBGZJHView',
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