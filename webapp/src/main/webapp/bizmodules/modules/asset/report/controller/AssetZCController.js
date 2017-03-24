Ext.define("AssetReport.controller.AssetZCController", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'AssetReport.view.AssetZCView',
    queryAction: 'assetReport-query',
    extraEvents: [{cid: 'assetReportType', name: 'change', fn: 'assetReportTypeChangeFn'}],
    beforeQuery: function () {
        var me = this;
        var view = me.view;
        return true;
    },
    afterInit: function () {
        var me = this;
        var view = me.view;
    },
    assetReportTypeChangeFn: function () {
        var me = this;
        var view = me.view;
        var assetReportTypeCmp= view.getCmp("assetReportType");
        var assetReportType= view.getCmp("assetReportType").gotValue();
        var isNewCmp= view.getCmp("isNew");
        var isMaintainCmp= view.getCmp("isMaintain");
        var isDiscardCmp= view.getCmp("isDiscard");
        if(assetReportType==1){
            isNewCmp.sotValue("1");
            isMaintainCmp.sotValue("");
            isDiscardCmp.sotValue("");
        }else if (assetReportType==2){
            isNewCmp.sotValue("");
            isMaintainCmp.sotValue("1");
            isDiscardCmp.sotValue("");
        }else if (assetReportType==3){
            isNewCmp.sotValue("");
            isMaintainCmp.sotValue("");
            isDiscardCmp.sotValue("1");
        }else {
            isNewCmp.sotValue("");
            isMaintainCmp.sotValue("");
            isDiscardCmp.sotValue("");
        }
    }
});