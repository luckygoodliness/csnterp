Ext.define("AssetReport.controller.AssetZTJCController", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'AssetReport.view.AssetZTJCView',
    queryAction: 'assetReport-query',
    extraEvents: [
        {cid: 'deviceType', name: 'change', fn: 'deviceTypeChangeFe'}
    ],
    beforeQuery: function () {
        var me = this;
        var view = me.view;
        return true;
    },
    afterInit: function () {
        var me = this;
        var view = me.view;
        var now = new Date(); //当前日期
        var month = now.getMonth();
        var quarterCmp = view.getCmp("quarter");
        if (month < 4) {
            quarterCmp.sotValue("1");
        } else if (month < 7) {
            quarterCmp.sotValue("2");
        } else if (month < 10) {
            quarterCmp.sotValue("3");
        } else {
            quarterCmp.sotValue("4");
        }

    },
    deviceTypeChangeFe: function () {
        var me = this;
        var view = me.view;
        var deviceTypeCmp = view.getCmp("deviceType");
        var deviceTypeDescCmp = view.getCmp("deviceTypeDesc");
        deviceTypeDescCmp.sotValue(deviceTypeCmp.rawValue);
    }
});