Ext.define("Monitory.controller.MonitoryController", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'Monitory.view.MonitoryView',
    queryAction: 'monitory-query',
    extraEvents: [
        {cid: 'btnEditMonitorY', name: 'click', fn: 'openMonitorYView'}
    ],
    openMonitorYView: function(){
        var me = this;
        var view = me.view;
        var yearParent = view.getCmp("year").gotValue();
        if (Scdp.ObjUtil.isEmpty(yearParent) ) {
            Scdp.MsgUtil.warn("调整年终评估时，请先填写查询条件的年！");
            return;
        }
        var postData = {"year": yearParent};
        Scdp.doAction('monitory-load', postData, function (retData) {
            var projectDto = retData["incomeLst"];
            var nonProjectDto = retData["paymentLst"];

            var callback = function (subView) {
                var projectDtoGrid = subView.getCmp("projectDto");
                var nonProjectDtoGrid = subView.getCmp("nonProjectDto");
                if (!projectDtoGrid.isDirty() && !nonProjectDtoGrid.isDirty()) {
                    Scdp.MsgUtil.warn("数据未发生变化");
                    return;
                }
                 var projectDtoUpdRecords = projectDtoGrid.store.getUpdatedRecords();
                var nonProjectDtoRecords = nonProjectDtoGrid.store.getUpdatedRecords();
                var projectDtoArray = [];
                var nonProjectDtoArray = [];
                for (var i = 0; i < projectDtoUpdRecords.length; i++) {
                    var item = projectDtoUpdRecords[i];
                    var assignedValueIn = item.data["assignedValue"];
                    var occurredValueIn = item.data["occurredValue"];
                    if (occurredValueIn > assignedValueIn) {
                        Scdp.MsgUtil.warn("实际发生不能大于终审值！");
                        return false;
                    }
                    projectDtoArray.push(item.data);
                }
                for (var i = 0; i < nonProjectDtoRecords.length; i++) {
                    var item = nonProjectDtoRecords[i];
                    var assignedValueOut = item.data["assignedValue"];
                    var occurredValueOut = item.data["occurredValue"];
                    if (occurredValueOut > assignedValueOut) {
                        Scdp.MsgUtil.warn("其他非项目支出中每月实际发生不能大于终审值！");
                        return false;
                    }
                    nonProjectDtoArray.push(item.data);
                }
                var saveData = {'projectDtoArray': projectDtoArray, 'nonProjectDtoArray': nonProjectDtoArray};
                Scdp.doAction('monitory-modify', saveData, function (retDt) {
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
            var controller = Ext.create("Monitory.controller.MonitoryPopController");
            var winActual = Scdp.openNewWinByController(controller, callback, 'temp_icon_16', '年终评估调整', "保存");
            var childView = winActual.down('IView');
            childView.getCmp("nonPrmMainDto->year").sotValue(yearParent);
            childView.getCmp("projectDto").sotValue(projectDto);
            childView.getCmp("nonProjectDto").sotValue(nonProjectDto);
        }, null, null, null);
    },
    afterInit: function () {
        var me = this;
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
    },
    beforeModify: function () {
        var me = this;
        return true;
    },
    afterModify: function () {
        var me = this;
    },
    beforeSave: function () {
        var me = this;
        return true;
    },
    afterSave: function (retData) {
        var me = this;
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
    },
    beforeDelete: function () {
        var me = this;
        return true;
    },
    afterDelete: function () {
        var me = this;
    },
    beforeBatchDel: function () {
        var me = this;
        return true;
    },
    afterBatchDel: function () {
        var me = this;
    },
    beforeExport: function () {
        var me = this;
        return true;
    },
    afterExport: function () {
        var me = this;
    }
});