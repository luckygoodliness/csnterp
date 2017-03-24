Ext.define("Monitor.controller.MonitorController", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'Monitor.view.MonitorView',
    queryAction: 'monitor-query',
    extraEvents: [
        {cid: 'btnMonitorM', name: 'click', fn: 'openMonitorMView'},
        {cid: 'isRealTime', name: 'change', fn: 'doChgeSearchState'}
    ],
    beforeQuery: function () {
        var me = this;
        var view = this.view;
        var year = view.getCmp("year").gotValue();
        var month = view.getCmp("month").gotValue();
        var isRealTime = view.getCmp("isRealTime").gotValue();
        if (isRealTime == 0 && (Scdp.ObjUtil.isEmpty(year) || Scdp.ObjUtil.isEmpty(month))) {
            Scdp.MsgUtil.warn("非实时条件下，年和月非空！");
            return false;
        }
        return true;
    },
    openMonitorMView: function () {
        var me = this;
        var view = me.view;
        var yearParent = view.getCmp("year").gotValue();
        var monthParent = view.getCmp("month").gotValue();
        var isRealTime = view.getCmp("isRealTime").gotValue();
        if (isRealTime == 1) {
            Scdp.MsgUtil.warn("请取消实时勾选，录入年和月！");
            return;
        }
        if (Scdp.ObjUtil.isEmpty(yearParent) || Scdp.ObjUtil.isEmpty(monthParent)) {
            Scdp.MsgUtil.warn("录入月度实际发生值时，请先填写查询条件的年和月！");
            return;
        }
        var postData = {"year": yearParent, "month": monthParent};
        Scdp.doAction('monitorm-load-actual', postData, function (retData) {
            var operateAgreementDto = retData["operateAgreementDto"];
            var otherNoPrmOutDto = retData["otherNoPrmOutDto"];

            var callback = function (subView) {
                var operateAgreementGrid = subView.getCmp("operateAgreementDto");
                var otherNoPrmOutGrid = subView.getCmp("otherNoPrmOutDto");
                if (!operateAgreementGrid.isDirty() && !otherNoPrmOutGrid.isDirty()) {
                    Scdp.MsgUtil.warn("数据未发生变化");
                    return;
                }
                var operateAgreementUpdRecords = operateAgreementGrid.store.getUpdatedRecords();
                var otherNoPrmOutUpdRecords = otherNoPrmOutGrid.store.getUpdatedRecords();
                var operateAgreementArray = [];
                var otherNoPrmOutArray = [];
                for (var i = 0; i < operateAgreementUpdRecords.length; i++) {
                    var item = operateAgreementUpdRecords[i];
                    //当月其他收入及财务收入不做校验
//                    var assignedValueIn = item.data["assignedValue"];
//                    var occurredValueIn = item.data["occurredValue"];
//                    if (occurredValueIn > assignedValueIn) {
//                        Scdp.MsgUtil.warn("经营合同计划及其他中每月实际发生不能大于终审值！");
//                        return false;
//                    }
                    operateAgreementArray.push(item.data);
                }
                for (var i = 0; i < otherNoPrmOutUpdRecords.length; i++) {
                    var item = otherNoPrmOutUpdRecords[i];
                    var assignedValueOut = item.data["assignedValue"];
                    var occurredValueOut = item.data["occurredValue"];
                    if (occurredValueOut > assignedValueOut) {
                        Scdp.MsgUtil.warn("其他非项目支出中每月实际发生不能大于终审值！");
                        return false;
                    }
                    otherNoPrmOutArray.push(item.data);
                }
                var saveData = {
                    'operateAgreementArray': operateAgreementArray,
                    'otherNoPrmOutArray': otherNoPrmOutArray
                };
                Scdp.doAction('monitor-month-add', saveData, function (retDt) {
                    var info = retDt["info"];
                    if (info != 'undefined' && info == 'success') {
                        Scdp.MsgUtil.info("保存成功，你可以在报表中进行查看！");
                        winActual.close();
                    } else {
                        Scdp.MsgUtil.info("保存失败，请检查！");
                    }
                }, null, null, null);
                return true;
            };
            var controller = Ext.create("Monitorm.controller.MonitormController");
            var winActual = Scdp.openNewWinByController(controller, callback, 'temp_icon_16', '当月实际发生录入', "保存");
            var childView = winActual.down('IView');
            childView.getCmp("nonPrmMainDto->year").sotValue(yearParent);
            childView.getCmp("nonPrmMainDto->month").sotValue(monthParent);
            childView.getCmp("operateAgreementDto").sotValue(operateAgreementDto);
            childView.getCmp("otherNoPrmOutDto").sotValue(otherNoPrmOutDto);
        }, null, null, null);

    },
    doChgeSearchState: function (checkbox, newValue, oldValue, eOpts) {
        var me = this;
        var view = this.view;
        var year = view.getCmp("year");
        var month = view.getCmp("month");
        var now = new Date();
        if (newValue == 1) {
            year.sotValue(now.getFullYear());
            month.sotValue(null);
            year.sotEditable(false);
            month.sotEditable(false);
        } else {
            year.sotEditable(true);
            month.sotEditable(true);
            year.sotValue(now.getFullYear());
            month.sotValue(now.getMonth() + 1);
        }

    }
});