Ext.define("Contract.controller.ContractController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Contract.view.ContractView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'cdmFileRelationDto->fileDownload', name: 'click', fn: 'fnFileDownload'},
        {cid: 'btnExamDate', name: 'click', fn: 'fnExamDate'},
        {cid: 'btnConfirmedDate', name: 'click', fn: 'fnConfirmedDate'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.contract.dto.PrmContractDto',
    queryAction: 'contract-query',
    loadAction: 'contract-load',
    //addAction: 'contract-add',
    //modifyAction: 'contract-modify',
    deleteAction: 'contract-delete',
    exportXlsAction: "contract-exportxls",
    afterInit: function () {
        var me = this;
        me.callParent(arguments);
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
        me.callParent(arguments);
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        me.callParent(arguments);
        var confirmedDate = me.view.getCmp("prmContractDto->confirmedDate").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(confirmedDate)) {
            me.view.getCmp("editPanel->btnConfirmedDate").setDisabled(true);
        } else {
            me.view.getCmp("editPanel->btnConfirmedDate").setDisabled(false);
        }

        var examDate = me.view.getCmp("prmContractDto->examDate").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(examDate)) {
            me.view.getCmp("editPanel->btnExamDate").setDisabled(false);
        } else {
            me.view.getCmp("editPanel->btnExamDate").setDisabled(true);
        }
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
        me.callParent(arguments);
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
    //文件下载
    fnFileDownload: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        Erp.FileUtil.erpFileDownLoad(grid);
    },
    fnConfirmedDate: function () {
        var me = this;
        var uuid = me.view.getCmp("prmContractDto->uuid").gotValue();
        if (Scdp.ObjUtil.isEmpty(uuid)) {
            Scdp.MsgUtil.warn("请选择数据!");
            return false;
        }
        var postData = {uuid: uuid};
        Scdp.MsgUtil.confirm("是否确认？", function (e) {
            if ("yes" == e) {
                Scdp.doAction("confirm-date", postData, function (result) {
                    if (Scdp.ObjUtil.isNotEmpty(result.result) && result.result == true) {
                        me.loadItem(uuid);
                        Scdp.MsgUtil.info("更新成功！");
                    }
                });
            }
        });
    },

    fnExamDate: function () {
        var me = this;
        var examDate = me.view.getCmp("prmContractDto->examDate").gotValue();
        var uuid = me.view.getCmp("prmContractDto->uuid").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(uuid)) {
            var controller = Ext.create("PrmExamdateHistory.controller.PrmExamDateWinController");
            var callback = function (subView) {
                var form = subView.getCmp('PrmExamDateHistoryDto');
                if (!form.validate()) {
                    return false;
                }
                var postData = {'viewdata': {'prmExamDateHistoryDto': subView.getCmp('PrmExamDateHistoryDto').gotValue()},
                    'dtoClass': controller.dtoClass};
                Scdp.doAction('prmexampdatahistory-add', postData, function (retData) {
                    if (retData.success == true) {
                        me.loadItem(uuid);
                        Scdp.MsgUtil.info("操作成功！");
                    }
                });
                return true;
            };
            var win = Scdp.openNewWinByController(controller, callback, 'temp_icon_16', '考核时间修正', '确定');
            var childForm = win.down('JForm')
            childForm.getCmp('tableName').sotValue('com.csnt.scdp.bizmodules.entity.prm.PrmContract');
            childForm.getCmp('columnName').sotValue('examDate');
            childForm.getCmp('dataUuid').sotValue(uuid);
            childForm.getCmp('oldDate').sotValue(examDate);
        }
    }
});