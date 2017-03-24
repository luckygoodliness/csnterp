Ext.define("AssetReport.controller.AssetSBGZZXController", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'AssetReport.view.AssetSBGZZXView',
    queryAction: 'assetReport-query',
    extraEvents: [
        {cid: 'year', name: 'change', fn: 'yearChangeFn'},
        {cid: 'quarter', name: 'change', fn: 'quarterChangeFn'}
    ],
    beforeQuery: function () {
        var me = this;
        var view = me.view;
        return true;
    },
    afterInit: function () {
        var me = this;
        var view = me.view;
    },
    yearChangeFn: function () {
        var me = this;
        var view = me.view;
        var year = view.getCmp("year").gotValue();
        var quarterCmp = view.getCmp("quarter");
        if(Scdp.ObjUtil.isEmpty(year)){
            quarterCmp.sotEditable(false);
        }else{
            quarterCmp.sotEditable(true);
        }
        if(Scdp.ObjUtil.isNotEmpty(quarterCmp.gotValue())){
            me.quarterChangeFn();
        }
    },
    quarterChangeFn: function () {
        var me = this;
        var view = me.view;
        var quarter = view.getCmp("quarter").gotValue();
        var year = view.getCmp("year").gotValue();
        var beginDateCmp =  view.getCmp("beginDate");
        var endDateCmp =  view.getCmp("endDate");
        if (quarter ==1 ){
            beginDateCmp.sotValue(year+"-01-01");
            endDateCmp.sotValue(year+"-03-31");
        }else if (quarter ==2 ){
            beginDateCmp.sotValue(year+"-04-01");
            endDateCmp.sotValue(year+"-06-30");
        }else if (quarter ==3 ){
            beginDateCmp.sotValue(year+"-07-01");
            endDateCmp.sotValue(year+"-09-30");
        }else if (quarter ==4 ){
            beginDateCmp.sotValue(year+"-10-01");
            endDateCmp.sotValue(year+"-12-31");
        }else{
            beginDateCmp.sotValue("");
            endDateCmp.sotValue("");
        }
    }
});