Ext.define("Payreq.controller.PayreqprojectmonthController", {
    extend: 'Payreq.controller.PayreqController',
    viewClass: 'Payreq.view.PayreqprojectmonthView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'fadPayReqHDto->month', name: 'change', fn: 'doMonthChange'},
        {cid: 'fadPayReqHDto->year', name: 'change', fn: 'doYearChange'},
        {cid: 'editToolbar->commitBtn', name: 'click', fn: 'doCommit'},
        {cid: 'editToolbar->checkBtn', name: 'click', fn: 'doCheck'},
        {cid: 'editToolbar->checkAllBtn', name: 'click', fn: 'doCheckAll'},
        {cid: 'editToolbar->backBtn', name: 'click', fn: 'doBack'},
        {cid: 'editToolbar->backDeptBtn', name: 'click', fn: 'doBackDept'},
        {cid: 'editToolbar->lockBtn', name: 'click', fn: 'doLock'},
        {cid: 'editPanel->fileUpload', name: 'click', fn: 'fileUploadClick'},
        {cid: 'editPanel->fileDownload', name: 'click', fn: 'fileDownloadClick'},
        {cid: 'editPanel->fileDelete', name: 'click', fn: 'fileDeleteClick'},
        {cid: 'editPanel->filePreview', name: 'click', fn: 'filePreviewClick'},
        {cid: 'searchBtn', name: 'click', fn: 'btnSearchClick'},
        {cid: 'exportDetailBtn', name: 'click', fn: 'exportDetailBtnClick'},
        {cid: 'editToolbar->toCertificate', name: 'click', fn: 'toCertificate'},
        {cid: 'expenseRequest', name: 'click', fn: 'doPrintExpenseRequest'},
        {cid: 'btnAnalysis', name: 'click', fn: 'btnAnalysisClick'},
        {cid: 'editToolbar->payBtn', name: 'click', fn: 'doPay'},
        {cid: 'editToolbar->cashReqBtn', name: 'click', fn: 'doCashReq'},
        {cid: 'btnSendMsg', name: 'click', fn: 'btnSendMsgClick'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.fad.payreq.dto.FadPayReqHDto',
    queryAction: 'payreq-query',
    loadAction: 'payreq-month-load',
    addAction: 'payreq-add',
    modifyAction: 'payreq-modify',
    deleteAction: 'payreq-delete',
    exportXlsAction: "payreq-exportxls",
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
        var view = me.view;
        view.getCmp("fadPayReqHDto->reqType").sotValue("1");
        view.getCmp("fadPayReqHDto->state").sotValue("0");
        view.getCmp("fadPayReqHDto->year").sotValue(new Date().getFullYear());
        view.getCmp("fadPayReqHDto->year").setReadOnly(false);
        view.getCmp("fadPayReqHDto->month").setEditable(false);

        view.getCmp("fadPayReqCDto").getColumnBydataIndex("auditMoney").setVisible(false);
        view.getCmp("fadPayReqCDto").getColumnBydataIndex("auditMoneyRate").setVisible(false);
        view.resetComState();
    },

    beforeCopyAdd: function () {
        var me = this;
        return true;
    },

    afterCopyAdd: function () {
        var me = this;
        var view = me.view;
    },

    beforeModify: function () {
        var me = this;
        var view = me.view;
        var state = view.getCmp("fadPayReqHDto->state").gotValue();
        if ((state != "0" && state != "6") || (!me.isScmVp && state == "6")) {
            Scdp.MsgUtil.warn("当前申请非新增状态，无法修改！");
            return false;
        }
        if (me.actionParams.modifyCheckCreateBy == '1') {
            var stateCmp = me.view.getHeader().getCmp(me.getWfStateField());
            if (Scdp.ObjUtil.isNotEmpty(stateCmp)) {
                var state = stateCmp.gotValue();
                if (Scdp.ObjUtil.isEmpty(state) || '0' == state || '5' == state) {
                    if (Scdp.getCurrentUserId() !== me.view.createBy) {
                        Scdp.MsgUtil.info("只有创建人可以修改该数据！");
                        return false;
                    }
                }
            }
        }
        return true;
    },

    doSave: function () {
        var me = this;
        if (arguments.length == 2) {
            var succFn = function () {
                me.view.getCmp("fadPayReqCDto").startFilter();
            }
            me.superclass.doSave.call(me, arguments[0], arguments[1], succFn);
        } else {
            me.callParent(arguments);
        }
    },

    erpBeforeModify: function () {
        var me = this;
        me.callParent(arguments);
    },

    afterModify: function () {
        var me = this;
        var view = me.view;
        view.getCmp("fadPayReqHDto->year").setReadOnly(true);
        view.getCmp("fadPayReqHDto->month").setReadOnly(true);
        view.getCmp("fadPayReqHDto->state").setReadOnly(true);
        var fadPayReqCDtoGrid = view.getCmp("fadPayReqCDto");
        fadPayReqCDtoGrid.getCmp("toolbar").setDisabled(false);
        view.getCmp("editPanel-\x3esaveBtn").setDisabled(false);
    },

    afterSave: function (retData) {
        var me = this;
        var view = me.view;
        me.callParent(arguments);
        if (Scdp.ObjUtil.isNotEmpty(retData.reqno)) {
            view.getCmp("fadPayReqHDto->reqno").sotValue(retData.reqno);
        }

        var uuid = view.getCmp("fadPayReqHDto->uuid").gotValue();
        if (Scdp.ObjUtil.isEmpty(uuid) && Scdp.ObjUtil.isNotEmpty(retData.uuid)) {
            view.getCmp("fadPayReqHDto->uuid").sotValue(retData.uuid);
            uuid = retData.uuid;
        }
        if (Scdp.ObjUtil.isNotEmpty(uuid)) {
            me.loadItem(uuid);
        }
    },

    beforeLoadItem: function () {
        var me = this;
        return true;
    },

    afterLoadItem: function () {
        var me = this;
        var view = me.view;
        me.callParent(arguments);
        view.resetComState();
        var state = view.getCmp("fadPayReqHDto->state").gotValue();

        view.getCmp("fadPayReqCDto").getColumnBydataIndex("purchaseChiefMoney").setVisible(false);
        view.getCmp("fadPayReqCDto").getColumnBydataIndex("purchaseChiefMoneyRate").setVisible(false);

        view.getCmp("fadPayReqCDto").getColumnBydataIndex("auditMoney").setVisible(false);
        view.getCmp("fadPayReqCDto").getColumnBydataIndex("auditMoneyRate").setVisible(true);

        var isAuditMoneyVisible = ( state == '1' || state == '2' || state == '4') || me.isScmVp || me.isComVp;
        view.getCmp("fadPayReqCDto").getColumnBydataIndex("auditMoney").setVisible(isAuditMoneyVisible);
        view.getCmp("fadPayReqCDto").getColumnBydataIndex("auditMoneyRate").setVisible(isAuditMoneyVisible);
        view.getCmp("fadPayReqCDto").getColumnBydataIndex("purchaseChiefMoney").setVisible(true);
        view.getCmp("fadPayReqCDto").getColumnBydataIndex("purchaseChiefMoneyRate").setVisible(true);

        if (state == '0') {
            view.getCmp('editPanel->workFlowCommitBtn').setDisabled(true);
        }
        else if (state == '2' || state == '4') {
            view.getCmp("fadPayReqCDto").getColumnBydataIndex("auditMoney").setVisible(true);
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
        var view = me.view;
        var rtnFlag = true;
        var state = view.getCmp("fadPayReqHDto->state").gotValue();
        if (state != "0" && state != "5" && state != "6") {
            Scdp.MsgUtil.warn("非新增状态的支付，删除失败！")
            return false;
        } else {
            var inValidStates = ["1", "2", "4", "8", "9"];
            var store = view.getCmp("fadPayReqCDto").store;
            if (store.getCount() > 0) {
                for (var i = 0; i < store.getCount(); i++) {
                    var data = store.getAt(i).data;
                    if (Scdp.ObjUtil.isNotEmpty(data.state) && inValidStates.indexOf(data.state) >= 0) {
                        rtnFlag = false;
                        break;
                    }
                }
            }
            if (!rtnFlag) {
                Scdp.MsgUtil.warn("存在非新增状态的支付明细，删除失败！！")
            }
            return rtnFlag;
        }
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

    beforeWorkFlowCommit: function () {
        var me = this;
        var view = me.view;
        view.getCmp("fadPayReqCDto").store.clearFilter();
        if (view.getCmp("fadPayReqCDto").store.count() == 0) {
            Scdp.MsgUtil.warn("当前支付申请没有支付明细，无法提交！");
            return false;
        }
        var state = view.getCmp("fadPayReqHDto->state").gotValue();
        if ("0" == state || "2" == state) {
            Scdp.MsgUtil.warn("该支付申请未锁定，无法提交！");
            return false;
        } else if ("6" == state) {
            if (!me.isScmVp) {
                Scdp.MsgUtil.warn("锁定状态，只能有供应链部主任发起提交！");
                return false;
            }
        }
        return true;
    },

    refreshUIStatusBasedOnWorkFlow: function (returnData) {
        var me = this;
        var view = me.view;
        var state = view.getCmp("fadPayReqHDto->state").gotValue();
        if (state == "0" || state == "2") {
            view.getCmp('editPanel->workFlowCommitBtn').setDisabled(true);
        }
        if (me.actionParams.type == "project") {
            view.getCmp('editPanel->workFlowViewLogBtn').setVisible(false);
            view.getCmp('editPanel->workFlowCommitBtn').setVisible(false);
            view.getCmp('editPanel->workFlowAssignBtn').setVisible(false);
            view.getCmp('editPanel->workFlowRollBackBtn').setVisible(false);
            view.getCmp('editPanel->workFlowCancelBtn').setVisible(false);
        }
    },

    doMonthChange: function (a, b) {
        var me = this;
        var view = me.view;
        var fadPayReqCDtoGrid = view.getCmp("fadPayReqCDto");
        fadPayReqCDtoGrid.store.removeAll();

        fadPayReqCDtoGrid.getCmp("toolbar").setDisabled(true)
        view.getCmp("editPanel-\x3esaveBtn").setDisabled(true)

        if (Scdp.ObjUtil.isNotEmpty(b)) {
            var year = view.getCmp("fadPayReqHDto->year").gotValue();
            if (Scdp.ObjUtil.isEmpty(year)) {
                Scdp.MsgUtil.warn("请选择支付年度！")
                return;
            }
            var postData = {year: year, month: b};
            Scdp.doAction("payreq-fill", postData, function (result) {
                var uuid = result.uuid;
                var reqWarnMsg = result.reqWarnMsg;
                var lstObj = result.payreqc;
                if (Scdp.ObjUtil.isNotEmpty(uuid)) {
                    me.loadItem(uuid, "modify", me.afterModify);
                }
                else {
                    var fadPayReqCDtoGrid = view.getCmp("fadPayReqCDto");
                    fadPayReqCDtoGrid.getCmp("toolbar").setDisabled(false);
                    view.getCmp("editPanel-\x3esaveBtn").setDisabled(false)
                    if (Scdp.ObjUtil.isNotEmpty(reqWarnMsg)) {
                        view.getCmp("fadPayReqHDto->month").sotValue("");
                        Scdp.MsgUtil.warn(reqWarnMsg)
                    }
                }
            });
        }
    },

    doYearChange: function (a, b) {
        var me = this;
        var view = this.view;
        view.getCmp("fadPayReqHDto->month").sotValue('');
    },
    pickContract: function () {
        var me = this;
        var view = me.view;
        if (Scdp.ObjUtil.isEmpty(view.getCmp("fadPayReqHDto->year").gotValue()) ||
            Scdp.ObjUtil.isEmpty(view.getCmp("fadPayReqHDto->month").gotValue())) {
            Scdp.MsgUtil.warn("未选择支付年月，无法新增支付明细！");
            return;
        }
        var billstate = view.getCmp("fadPayReqHDto->state").gotValue();
        if (!me.isScmVp && billstate != '0') {
            Scdp.MsgUtil.warn("当前申请非新增状态，无法添加！");
            return;
        }
        me.callParent(arguments);
    },

    doLock: function () {
        var me = this;
        var view = me.view;
        if ("0" != view.getCmp("fadPayReqHDto->state").gotValue()) {
            Scdp.MsgUtil.warn("当前申请非新增状态，无法锁定！");
            return;
        }
        Scdp.MsgUtil.confirm("锁定后将无法打开，请确认是否锁定？", function (e) {
            if ("yes" == e) {
                var uuid = view.getCmp("fadPayReqHDto->uuid").gotValue();
                var param = {action: 'lock', uuid: uuid};
                Scdp.doAction("payreq-approve", param, function (result) {
                    me.loadItem(uuid);
                });
            }
        });
    },

    doCommit: function () {
        var me = this;
        var view = me.view;
        var selectedRecords = view.getCmp("fadPayReqCDto").getSelectedRecords();
        if (selectedRecords.length == 0) {
            Scdp.MsgUtil.warn("请选择需要提交的记录！");
            return;
        }
        var isProject = me.actionParams.type == "project";
        var applyMoney0 = Ext.Array.filter(selectedRecords, function (item) {
            if (isProject) {
                if (Scdp.ObjUtil.isNotEmpty(item.data.reqMoney) && item.data.reqMoney == 0) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (Scdp.ObjUtil.isNotEmpty(item.data.purchaseManagerMoney) && item.data.reqMoney == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        if (Scdp.ObjUtil.isNotEmpty(applyMoney0)) {
            var resNos = '';
            Ext.Array.each(applyMoney0, function (item) {
                var index = me.view.getCmp("fadPayReqCDto").store.indexOf(item);
                resNos = resNos + (index + 1) + ",";
            });
            resNos = resNos.substr(0, resNos.length - 1);
            Scdp.MsgUtil.confirm("序号 " + resNos + " 申请金额为0,是否确认提交？", function (e) {
                if ("yes" == e) {
                    me.checkAction('commit', '0');
                }
            });
        }
        else {
            Scdp.MsgUtil.confirm("是否确认提交？", function (e) {
                if ("yes" == e) {
                    me.checkAction('commit', '0');
                }
            });
        }
    },
    doCheckAll: function () {
        var me = this;
        var view = me.view;
        var selectedRecords = view.getCmp("fadPayReqCDto").getStore().data.items;
        if (selectedRecords.length == 0) {
            Scdp.MsgUtil.warn("当前没有待确认的支付明细！");
            return;
        }
        Scdp.MsgUtil.confirm("是否全部确认？", function (e) {
            if ("yes" == e) {
                me.checkAction('check', '7', true);
            }
        });
    },

    doCheck: function () {
        var me = this;
        var view = me.view;
        var selectedRecords = view.getCmp("fadPayReqCDto").getSelectedRecords();
        if (selectedRecords.length == 0) {
            Scdp.MsgUtil.warn("请选择需要确认的记录！");
            return;
        }
        Scdp.MsgUtil.confirm("是否选择确认？", function (e) {
            if ("yes" == e) {
                me.checkAction('check', '7');
            }
        });
    },

    doBack: function () {
        var me = this;
        var view = me.view;
        var selectedRecords = view.getCmp("fadPayReqCDto").getSelectedRecords();
        if (selectedRecords.length == 0) {
            Scdp.MsgUtil.warn("请选择需要撤回的记录！");
            return;
        }
        Scdp.MsgUtil.confirm("是否确认撤回？", function (e) {
            if ("yes" == e) {
                me.checkAction('back', '7');
            }
        });
    },

    doBackDept: function () {
        var me = this;
        var view = me.view;
        if ("0" != view.getCmp("fadPayReqHDto->state").gotValue()) {
            Scdp.MsgUtil.warn("当前申请非新增状态，无法退回部门！");
            return;
        }
        var view = me.view;
        var selectedRecords = view.getCmp("fadPayReqCDto").getSelectedRecords();
        if (selectedRecords.length == 0) {
            Scdp.MsgUtil.warn("请选择需要退回的记录！");
            return;
        }
        Scdp.MsgUtil.confirm("是否确认退回部门？", function (e) {
            if ("yes" == e) {
                me.checkAction('backDept', '1');
            }
        });
    },

    checkAction: function (action, state, range) {
        var me = this;
        var view = me.view;
        if ("0" == view.getCmp("fadPayReqHDto->state").gotValue()) {
            var uuid = view.getCmp("fadPayReqHDto->uuid").gotValue();
            var cids = [];
            var selectedRecords;
            if (range) {
                selectedRecords = view.getCmp("fadPayReqCDto").getStore().data.items;
            } else {
                selectedRecords = view.getCmp("fadPayReqCDto").getSelectedRecords();
            }
            Ext.Array.each(selectedRecords, function (a) {
                var rowData = a.data;
                if (rowData.state == state) {
                    cids.push(rowData.uuid);
                }
            });
            if (cids.length == 0) {
                var msg = "没有符合状态的支付明细";
                if (state == "0") {
                    msg = "选中记录均已提交，处于确认中或已提交，无法再提交!";
                }
                else if (state == "7") {
                    if (action == "check") {
                        msg = "只能确认待确认的记录，选中记录均已确认或处于新增状态，无法再确认!";
                    }
                    else if (action == "back") {
                        msg = "只能撤回确认中的记录，选中记录均已撤回或已提交，无法再撤回!";
                    }
                }
                else if (state == "1") {
                    msg = "当前选中记录未提交或者已全部退回，无法再退回部门!";
                }
                Scdp.MsgUtil.warn(msg);
                return;
            }
            var param = {action: action, uuid: uuid, fadUUids: cids};
            Scdp.doAction("payreq-approve", param, function (result) {
                if (result.msg) {
                    Scdp.MsgUtil.warn(result.msg);
                    return;
                } else {
                    var callFn = function () {
                        me.loadItem(uuid, '', function () {
                            view.getCmp("fadPayReqCDto").startFilter()
                        });
                    };
                    Scdp.runWithMask(null, callFn);
                }
            });
        }
        else {
            Scdp.MsgUtil.warn("支付申请未处于新增状态，无法处理！！");
            return;
        }
    },

    fileUploadClick: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        var fileClassify = "FAD_MONTH_PAYMENT";
        Erp.FileUtil.erpFileUpload(grid, fileClassify, me.afterUploadData);
    },

    afterUploadData: function (form, obj) {
        var me = form;
        var view = me.view;
        var dataId = me.ownerCt.ownerCt.getCmp("fadPayReqHDto->uuid").gotValue();
        var fileId = obj.uuid;
        var param = {dataId: dataId, fileId: fileId, actionType: 'upload'};
        Scdp.doAction("cdmfile-common", param, function (result) {
            if (result.success && Scdp.ObjUtil.isNotEmpty(result.CdmFileRelationDto)) {
                form.addRowItem(result.CdmFileRelationDto, false);
            }
        });
    },

    //文件下载
    fileDownloadClick: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        Erp.FileUtil.erpFileDownLoad(grid);
    },

    //文件预览
    filePreviewClick: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        Erp.FileUtil.erpFilePreview(grid);
    },

    //文件删除
    fileDeleteClick: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        Erp.FileUtil.erpFileDelete(grid, null, 1);
    }
});

