Ext.define("Notesplan.controller.NotesplanController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Notesplan.view.NotesplanView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'addLastIssue', name: 'click', fn: 'addLastIssue'},//新增月度记录
        {cid: 'markTime', name: 'click', fn: 'markTime'},//记录时间
        {cid: 'filterQueryBtn', name: 'click', fn: 'detailQueryBtn'},//查询(编辑区）
        {cid: 'detailResetBtn', name: 'click', fn: 'detailResetBtn'},//重置(编辑区)
        {cid: 'lockIssue', name: 'click', fn: 'lockIssue'}//重置(编辑区)
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.notesplan.dto.ScmNotesPlanDto',
    queryAction: 'notesplan-query',
    loadAction: 'notesplan-load',
    addAction: 'notesplan-add',
    modifyAction: 'notesplan-modify',
    deleteAction: 'notesplan-delete',
    exportXlsAction: "notesplan-exportxls",
    afterInit: function () {
        var me = this;
        me.doQuery();
    },
    beforeAdd: function () {
        return true;
    },
    afterAdd: function () {
    },
    beforeCopyAdd: function () {
        return true;
    },
    afterCopyAdd: function () {
    },
    beforeModify: function () {
        return true;
    },
    afterModify: function () {
    },
    beforeSave: function () {
        return true;
    },
    afterSave: function () {
    },
    beforeLoadItem: function () {
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        //1.加载数据之后，相关按钮的禁用和启用
        var selectedRecord = me.view.getResultPanel().getCurRecord();
        if (selectedRecord.length == 0) {
            Scdp.MsgUtil.info("未选择月度记录！");
            return false;
        }
        var islock = selectedRecord.get("islock");
        if (Scdp.ObjUtil.isNotEmpty(islock)) {
            if (islock == "1") {
                me.view.getCmp("modifyBtn").setDisabled(true);
                me.view.getCmp("markTime").setDisabled(true);
            } else {
                me.view.getCmp("markTime").setDisabled(false);
            }
        } else {
            me.view.getCmp("markTime").setDisabled(false);
        }
        this.detailResetBtn();
    },
    beforeCancel: function () {
        return true;
    },
    afterCancel: function () {
    },
    beforeDelete: function () {
        return true;
    },
    afterDelete: function () {
    },
    beforeBatchDel: function () {
        return true;
    },
    afterBatchDel: function () {
    },
    beforeExport: function () {
        return true;
    },
    afterExport: function () {
    },
    detailQueryBtn: function () {
        var me = this;
        var officeId = me.view.getCmp('queryForm->officeId').gotValue();
        var projectId = me.view.getCmp('queryForm->projectId').gotValue();
        var supplierCode = me.view.getCmp('queryForm->supplierCodeQuery').gotValue();
        var createBy = me.view.getCmp('queryForm->createBy').gotValue();
        var conclusion = me.view.getCmp("queryForm->conclusion").gotValue();
        var queryConditions = {};
        if (Scdp.ObjUtil.isNotEmpty(officeId)) {
            queryConditions.officeId = officeId;
        }
        if (Scdp.ObjUtil.isNotEmpty(projectId)) {
            queryConditions.projectId = projectId;
        }
        if (Scdp.ObjUtil.isNotEmpty(supplierCode)) {
            queryConditions.supplierCode = supplierCode;
        }
        if (Scdp.ObjUtil.isNotEmpty(createBy)) {
            queryConditions.createBy = createBy;
        }
        if (Scdp.ObjUtil.isNotEmpty(conclusion)) {
            queryConditions.conclusion = conclusion;
        }
        var modulePath = me.view.modulePath;
        var layoutFile = "notesplan-query-layout.xml";
        var uuid = me.view.getCmp("uuid").value;
        if (Scdp.ObjUtil.isEmpty(uuid)) {
            Scdp.MsgUtil.info("未加载期号记录！");
            return false;
        }
        var selectedRecords = me.view.getCmp("scmNotesPlanDetailDto").getSelectedRecords();
        var postData = {queryConditions: queryConditions, modulePath: modulePath, layoutFile: layoutFile, uuid: uuid};
        Scdp.doAction("notesplan-detail-filter-query", postData, function (result) {
            me.view.getCmp('scmNotesPlanDetailDto').sotValue(result.scmNotesPlanDetailDto);
            var scmNotesPlanDetailStore = me.view.getCmp('scmNotesPlanDetailDto').getStore();
            if (uuid && scmNotesPlanDetailStore && scmNotesPlanDetailStore.getCount() > 0) {
                var scmContractCode = "";
                if (selectedRecords.length != 0) {
                    scmContractCode = selectedRecords[0].get("scmContractCode");
                }
                for (var i = 0; i < scmNotesPlanDetailStore.data.items.length; i++) {
                    var subItem = scmNotesPlanDetailStore.data.items[i];
                    if (scmContractCode == subItem.data.scmContractCode) {
                        me.view.getCmp('scmNotesPlanDetailDto').getSelectionModel().select(i);
                    }
                }
            }
        });
        var grid = me.view.getCmp("scmNotesPlanDetailDto");
        grid.sotEditable(false);
    },
    //重置按钮
    detailResetBtn: function () {
        var me = this;
        me.view.getCmp("officeId").sotValue("");
        me.view.getCmp("projectIdName").sotValue("");
        me.view.getCmp("projectId").sotValue("");
        me.view.getCmp("supplierCodeName").sotValue("");
        me.view.getCmp("supplierCodeQuery").sotValue("");
        me.view.getCmp("createByName").sotValue("");
        me.view.getCmp("createBy").sotValue("");
        me.view.getCmp("conclusion").sotValue("");
    },
    //记录时间
    markTime: function () {
        var me = this;
        var selectedRecords = me.view.getCmp("scmNotesPlanDetailDto").getSelectedRecords();
        if (selectedRecords.length == 0) {
            Scdp.MsgUtil.info("未选择票据计划详情记录！");
            return false;
        }
        var uuid = me.view.getCmp("uuid").value;
        var postData = {
            uuid: uuid,
            selectedRecord: selectedRecords[0].data
        };
        Scdp.doAction("notesplan-marktime", postData, function () {
            me.detailQueryBtn();
        });
        me.view.getCmp("modifyBtn").setDisabled(false);
        me.view.getCmp("cancelBtn").setDisabled(true);
        Ext.getCmp("editStatus").setValue(Scdp.I18N.VIEW_STATUS);
        var grid = me.view.getCmp("scmNotesPlanDetailDto");
        grid.sotEditable(false);

    },
    //确认新增一条期号记录
    addLastIssue: function () {
        var me = this;
        Scdp.MsgUtil.confirm("确认新增下一期记录？", function (e) {
            if ("yes" == e) {
                Scdp.doAction("notesplan-add", null, function () {
                    me.doQuery();
                    me.detailQueryBtn();
                    var grid = me.view.getCmp("scmNotesPlanDetailDto");
                    grid.sotEditable(true);
                })
            }
        })
    },
    //锁定一条记录
    lockIssue: function () {
        var me = this;
        var curRecord = me.view.getResultPanel().getCurRecord();
        if (!curRecord) {
            Scdp.MsgUtil.info("未选择期号记录！");
            return false;
        }
        if (curRecord.data.islock == "1") {
            Scdp.MsgUtil.info("该期号当前状态已经为锁定状态！");
            return false;
        }
        Scdp.MsgUtil.confirm("确认锁定该记录？", function (e) {
            if ("yes" == e) {
                var uuid = curRecord.get("uuid");
                var postData = {uuid: uuid};
                Scdp.doAction("notesplan-lock", postData, function () {
                    me.doQuery();
                })
            }
        })
    },
    //重写页面修改按钮
    doModify: function () {//MODIFY_STATUS
        var me = this;
        me.view.getCmp("modifyBtn").setDisabled(true);
        me.view.getCmp("cancelBtn").setDisabled(false);
        me.view.getCmp("saveBtn").setDisabled(false);
        Ext.getCmp("editStatus").setValue(Scdp.I18N.MODIFY_STATUS);
        me.detailQueryBtn();
        var grid = me.view.getCmp("scmNotesPlanDetailDto");
        grid.sotEditable(true);
    },
    //重写保存按钮
    doSave: function () {//MODIFY_STATUS
        var me = this;
        var selecteds = me.view.getCmp("scmNotesPlanDetailDto").getSelectedRecords();
        if (selecteds.length == 0) {
            return false;
        }
        var uuid = me.view.getCmp("uuid").value;
        var selectedRecords = [];
        for (var i = 0; i < selecteds.length; i++) {
            selectedRecords.push(selecteds[i].data);
        }
        var postData = {
            type: "save",
            uuid: uuid,
            selectedRecords: selectedRecords
        };
        Scdp.doAction("notesplan-save", postData, function () {
            me.detailQueryBtn();
        });
        me.view.getCmp("modifyBtn").setDisabled(false);
        me.view.getCmp("cancelBtn").setDisabled(true);
        me.view.getCmp("saveBtn").setDisabled(true);
        Ext.getCmp("editStatus").setValue(Scdp.I18N.MODIFY_STATUS);
        me.detailQueryBtn();
        var grid = me.view.getCmp("scmNotesPlanDetailDto");
        grid.sotEditable(false);
    },
    //重写取消按钮
    doCancel: function () {
        var me = this;
        me.view.getCmp("modifyBtn").setDisabled(false);
        me.view.getCmp("cancelBtn").setDisabled(true);
        me.view.getCmp("saveBtn").setDisabled(true);
        Ext.getCmp("editStatus").setValue(Scdp.I18N.VIEW_STATUS);
        me.detailQueryBtn();
        var grid = me.view.getCmp("scmNotesPlanDetailDto");
        grid.sotEditable(false);
    }
});