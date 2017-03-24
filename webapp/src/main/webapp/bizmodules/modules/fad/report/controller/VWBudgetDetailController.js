Ext.define("FadReport.controller.VWBudgetDetailController", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'FadReport.view.VWBudgetDetailView',
    queryAction: 'vw_budget_detail-query',
    extraEvents: [
        /*{cid: 'stateCheck', name: 'change', fn: 'stateCheck'},*/
        {cid: 'btnRtn', name: 'click', fn: 'doReturn'},
        {
            cid: 'btnMonitorLaborCostAndOtherShareExpense',
            name: 'click',
            fn: 'openMonitorLaborCostAndOtherShareExpenseView'
        }
    ],
    beforeQuery: function () {
        var me = this;
        var view = me.view;
        return true;
    },
    afterInit: function () {
        var me = this;
        var view = me.view;
        var actionParams = me.actionParams;

        /*view.getCmp("stateCheck").sotValue(true);
         view.getCmp("state").sotValue("4");*/

        if (Scdp.ObjUtil.isNotEmpty(view.getCmp("conditionToolbar->btnRtn"))) {
            view.getCmp("conditionToolbar->btnRtn").setVisible(false);
        }
        if (Scdp.ObjUtil.isNotEmpty(actionParams)) {
            if (Scdp.ObjUtil.isNotEmpty(view.getCmp("conditionToolbar->btnRtn"))) {
                view.getCmp("conditionToolbar->btnRtn").setVisible(true);
            }
            var officeId = actionParams.officeId;
            if (Scdp.ObjUtil.isNotEmpty(officeId)) {
                view.getCmp("officeId").putValue(officeId);
                view.getCmp("officeId").resetOriginalValue();
            }
            var year = actionParams.year;
            if (Scdp.ObjUtil.isNotEmpty(year)) {
                view.getCmp("year").sotValue(year);
                view.getCmp("year").resetOriginalValue();
            }
            var month = actionParams.month;
            if (Scdp.ObjUtil.isNotEmpty(month)) {
                view.getCmp("month").sotValue(month);
                //view.getCmp("month").resetOriginalValue();
            }
            /*var dateStart = actionParams.dateStart;
             if (Scdp.ObjUtil.isNotEmpty(dateStart)) {
             view.getCmp("dateStart").sotValue(dateStart);
             }
             var dateOver = actionParams.dateOver;
             if (Scdp.ObjUtil.isNotEmpty(dateOver)) {
             view.getCmp("dateOver").sotValue(dateOver);
             }*/
            me.doQuery();
        }
    },
    /*stateCheck: function () {
     var me = this;
     var view = me.view;
     if (view.getCmp("stateCheck").gotValue()) {
     view.getCmp("state").sotValue("4");
     }
     else {
     view.getCmp("state").sotValue(null);
     }
     },*/
    doReturn: function () {
        var menuCode = "ERP_VW_BUDGET_DETAIL_QUERYACTION";
        Scdp.openNewModuleByMenuCode(menuCode, null, "MENU_ITEM_CTL", true);
    },
    openMonitorLaborCostAndOtherShareExpenseView: function () {
        var me = this;
        var view = me.view;
        var officeId = Erp.Util.isNullReturnEmpty(view.getCmp("officeId").gotValue());
        var year = Erp.Util.isNullReturnEmpty(view.getCmp("year").gotValue());
        var month = Erp.Util.isNullReturnEmpty(view.getCmp("month").gotValue());
        if (Scdp.ObjUtil.isEmpty(year) || Scdp.ObjUtil.isEmpty(month) || Scdp.ObjUtil.isEmpty(officeId)) {
            Scdp.MsgUtil.warn("月度实际发生录入时，请先填写查询条件的【部门】、【年】、【月】！");
            return;
        }
        var postData = {"year": year, "month": month, "officeId": officeId};
        Scdp.doAction('monitorlaborcostandothershareexpense-load-actual', postData, function (retData) {
            var tblVersion = retData["tblVersion"];
            var monitorLaborCostDto = retData["monitorLaborCostDto"];
            var monitorOtherShareDto = retData["monitorOtherShareDto"];

            var callback = function (subView) {
                var monitorLaborCostGrid = subView.getCmp("monitorLaborCostDto");
                var monitorOtherShareGrid = subView.getCmp("monitorOtherShareDto");
                if (!monitorLaborCostGrid.isDirty() && !monitorOtherShareGrid.isDirty()) {
                    Scdp.MsgUtil.warn("数据未发生变化");
                    return;
                }
                var monitorLaborCostArray = [];
                var monitorOtherShareArray = [];
                for (var i = 0; i < monitorLaborCostGrid.getStore().getCount(); i++) {
                    var monitorLaborCostGridRowData = monitorLaborCostGrid.getStore().data.items[i];
                    monitorLaborCostArray.push(monitorLaborCostGridRowData.data);
                }
                for (var i = 0; i < monitorOtherShareGrid.getStore().getCount(); i++) {
                    var monitorOtherShareGridRowData = monitorOtherShareGrid.getStore().data.items[i];
                    monitorOtherShareArray.push(monitorOtherShareGridRowData.data);
                }
                var saveData = {
                    'tblVersion': tblVersion,
                    'officeId': officeId,
                    'year': year,
                    'month': month,
                    'monitorLaborCostArray': monitorLaborCostArray,
                    'monitorOtherShareArray': monitorOtherShareArray
                };
                Scdp.doAction('monitorlaborcostandothershareexpense-save-actual', saveData, function (retDt) {
                    var info = retDt["info"];
                    if (info != 'undefined' && info == 'success') {
                        Scdp.MsgUtil.info("保存成功，你可以在报表中进行查看！");
                        winActual.close();
                    } else {
                        Scdp.MsgUtil.info("保存失败，请检查！");
                    }
                });
                return true;
            };
            var controller = Ext.create("Monitorlaborcostandothershareexpense.controller.MonitorlaborcostandothershareexpenseController");
            var winActual = Scdp.openNewWinByController(controller, callback, 'temp_icon_16', '月度实际发生录入', "保存");
            var childView = winActual.down('IView');
            childView.getCmp("monitorBaseDto->officeId").putValue(officeId);
            childView.getCmp("monitorBaseDto->year").sotValue(year);
            childView.getCmp("monitorBaseDto->month").sotValue(month);
            childView.getCmp("monitorLaborCostDto").sotValue(monitorLaborCostDto);
            childView.getCmp("monitorOtherShareDto").sotValue(monitorOtherShareDto);
        });
    }
});