Ext.define("Projectmain.controller.ProjectmainController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Projectmain.view.ProjectmainView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'projectBudgetHisTab->showBudgetHistoryBtn', name: 'click', fn: 'showBudgetHistory'},
        {cid: 'projectPlanHisTab->showPlanHistoryBtn', name: 'click', fn: 'showPlanHistory'},
        //{cid: 'editPanel->updateSijiNo', name: 'click', fn: 'updateSijiNo'},
        {cid: 'editPanel->btnShowReport2', name: 'click', fn: 'showReportFn2'},
        {cid: 'queryPanel->btnShowReport1', name: 'click', fn: 'showReportFn1'},
        {cid: 'btnExport', name: 'click', fn: 'fnExcelExport'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.projectmain.dto.PrmProjectMainDto',
    queryAction: 'projectmain-query',
    loadAction: 'projectmain-load',
    //addAction: 'projectmain-add',
    //modifyAction: 'projectmain-modify',
    //deleteAction: 'projectmain-delete',
    exportXlsAction: "projectmain-exportxls",
    afterInit: function () {
        var me = this;
        this.callParent(arguments);
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
    },
    showBudgetHistory: function () {
        var me = this;
        var view = me.view;
        var uuid = view.getCmp("prmProjectMainDto->uuid").gotValue();
        if (Scdp.ObjUtil.isEmpty(uuid)) {
            return;
        }

        var callBack = function (result) {
            for (var item in result) {
                if (!result.hasOwnProperty(item)) continue;//防止获得原型链中的属性
                var historyGrid = view.getCmp("projectBudgetHisTab->" + item);
                if (historyGrid) {
                    historyGrid.sotValue(result[item], true);
                }

            }
        };
        var postData = {};

        postData.uuid = uuid;
        Scdp.doAction("projectmain-show-budget-history", postData, callBack);
    },
    showPlanHistory: function () {
        var me = this;
        var view = me.view;
        var uuid = view.getCmp("prmProjectMainDto->uuid").gotValue();
        if (Scdp.ObjUtil.isEmpty(uuid)) {
            return;
        }

        var callBack = function (result) {
            for (var item in result) {
                if (!result.hasOwnProperty(item)) continue;//防止获得原型链中的属性
                var historyGrid = view.getCmp("projectPlanHisTab->" + item);
                if (historyGrid) {
                    historyGrid.sotValue(result[item], true);
                }

            }
        };
        var postData = {};
        postData.uuid = uuid;
        Scdp.doAction("projectmain-show-plan-history", postData, callBack);

    },
    updateSijiNo: function () {
        var me = this;
        var view = me.view;
        var uuid = view.getCmp("prmProjectMainDto->uuid").gotValue();
        var sijiNo = view.getCmp("prmProjectMainDto->sijiNo").gotValue();
        Scdp.MsgUtil.prompt('四技编号:', function (buttonId, text) {
            if (buttonId != 'ok' || Scdp.ObjUtil.isEmpty(text)) return;
            var postData = {};
            postData.uuid = uuid;
            postData.sijiNo = text;
            Scdp.doAction("projectmain-update-sijino", postData, function () {
                Scdp.MsgUtil.info("更新成功！");
                view.getCmp("prmProjectMainDto->sijiNo").sotValue(text);
            });
        }, null, null, sijiNo);
    },
    showReportFn1: function () {
        var me = this;
        var view = me.view;
        var actionParams = me.actionParams;
        var resultGrid = view.getCmp("resultPanel");
        var curRecord = resultGrid.getCurRecord();
        if (Scdp.ObjUtil.isNotEmpty(curRecord)) {
            var uuid = curRecord.get("uuid");
            me.showProjectReport(actionParams.reportFileInOut, uuid);
        }
    },
    showReportFn2: function () {
        var me = this;
        var view = me.view;
        var actionParams = me.actionParams;
        var uuid = view.getCmp("prmProjectMainDto->uuid").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(uuid)) {
            me.showProjectReport(actionParams.reportFileInOut, uuid);
        }
    },
    showProjectReport: function (reportPath, uuid) {
        var me = this;
        var actionParams = me.actionParams;
        if (Scdp.ObjUtil.isEmpty(reportPath)) {
            return;
        }
        var actionUrl = Erp.Util.getReportBasePath(reportPath);
        var fd = Ext.get('prmReportFormDummy');
        if (!fd) {
            fd = Ext.DomHelper.append(
                Ext.getBody(), {
                    tag: 'form',
                    method: 'post',
                    id: 'prmReportFormDummy',
                    action: actionUrl,
                    target: '_blank',
                    name: 'prmReportFormDummy',
                    cls: 'x-hidden',
                    cn: [
                        {
                            tag: 'input',
                            name: '__parameters__',
                            id: '__parameters__',
                            type: 'hidden'
                        }
                    ]
                }, true);

        } else {
            $(fd.dom).attr("action", actionUrl);
        }
        fd.child('#__parameters__').set({
            value: '{"project_id":"' + uuid + '"}'
        });
        fd.dom.submit();
    },
    //M3_C3_F1_界面功能增加
    fnExcelExport:function(){
        var me = this;
        var postData = {
            fileName:"已立项项目明细.xlsx",
            dtoClass:me.dtoClass,
            userId:Scdp.CacheUtil.get(Erp.Const.USER_ROLE_NAME).USERID ,
            menuCode:'PROJECT_MAIN_PLAN',
            uuid:me.view.getCmp("prmProjectMainDto->uuid").gotValue()
        };
        Scdp.doPost("projectmain-exportall",postData);
    }
});