//项目管理-采购申请变更
Ext.define("Scmcontractchange.controller.ScmNewContractchangeController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Scmcontractchange.view.ScmNewContractchangeView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'scmContractChangeDto->scmContractId', name: 'change', fn: 'scmContractIdChangeFn'},
        {cid: 'scmContractChangeDto->contractNature', name: 'change', fn: 'natureChangeFn'},
        {cid: 'fileUpload', name: 'click', fn: 'fileUploadBtn'},
        {cid: 'fileDownload', name: 'click', fn: 'fileDownloadBtn'},
        {cid: 'filePreview', name: 'click', fn: 'filePreviewBtn'},
        {cid: 'fileDelete', name: 'click', fn: 'fileDeleteBtn'}],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.scmcontractchange.dto.ScmContractChangeDto',
    queryAction: 'scmcontractchange-query',
    loadAction: 'scmcontractchange-load',
    addAction: 'scmcontractchange-add',
    modifyAction: 'scmcontractchange-modify',
    deleteAction: 'scmcontractchange-delete',
    exportXlsAction: "scmcontractchange-exportxls",
    isFirstCommit: false,
    afterInit: function () {
        var me = this;
        me.isFirstCommit = false;
        me.callParent(arguments);
        me.view.getCmp("fileUpload").setDisabled(true);
        me.view.getCmp("fileDelete").setDisabled(true);
        me.view.getCmp("scmContractChangeDDto").afterEditGrid = me.checkAmountFn;

        me.view.getCmp("scmContractChangeDDto").on('beforeedit', function (editor, eventObj) {
            var field = eventObj.field;
            var record = eventObj.record;
            //只有新增状态可编辑
            if (record.data.changeState != "1" && field == "handleAmount")
                return false;
        })
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        me.view.getCmp("fileUpload").setDisabled(false);
        me.view.getCmp("fileDelete").setDisabled(false);
        me.view.getCmp("scmContractChangeDto->closeChange").sotValue("0");
        me.view.getCmp("scmContractChangeDto->state").sotValue("0");
        me.view.getCmp("scmContractChangeDto->createBy").putValue(Scdp.getCurrentUserId());
        me.view.getCmp("originalValueTotal").sotValue("");
        me.view.getCmp("newValueTotal").sotValue("");
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
        me.setTotalMoney();
        return true;
    },
    afterModify: function () {
        var me = this;
        me.view.getCmp("fileUpload").setDisabled(false);
        me.view.getCmp("fileDelete").setDisabled(false);
        me.natureChangeFn(null, me.view.getCmp("scmContractChangeDto->contractNature").gotValue());
        me.setTotalMoney();
    },
    beforeSave: function () {
        var me = this;
        var detailItems = me.view.getCmp('scmContractChangeDDto').getStore().data.items;
        if (detailItems.length < 1) {
            Scdp.MsgUtil.info("请选择一条记录！");
            return false;
        }
        if (me.view.getCmp('scmContractChangeDto->state').gotValue()==0) {//如果是新增保存就校验，提交后修改保存不核验
            for (var i = 0; i < detailItems.length; i++) {
                if (detailItems[i].get("handleAmount") > detailItems[i].get("planAmount") && detailItems[i].get("changeState") == "1") {
                    Scdp.MsgUtil.info("申请数量超过最大值！");
                    return false;
                }
            }
        }

        var isProject = me.view.getCmp("isProject").gotValue();
        if (isProject == 1) {
            var newValueTotal = me.view.getCmp("newValueTotal").gotValue();
            var newValue = me.view.getCmp("scmContractChangeDto->newValue").gotValue();
            if (Number(newValue) > Number(newValueTotal)) {
                Scdp.MsgUtil.info("变更后金额不能大于变更后预算总价！");
                return false;
            }
        }

        return true;
    },
    afterSave: function (retData) {
        var me = this;
        me.callParent(arguments);
        me.view.getCmp("fileUpload").setDisabled(true);
        me.view.getCmp("fileDelete").setDisabled(true);
        if (Scdp.ObjUtil.isNotEmpty(retData.runningNo)) {
            me.view.getCmp("runningNo").sotValue(retData.runningNo);
        }
        me.afterloadItemInit();
    }
    ,
    beforeLoadItem: function () {
        var me = this;
        return true;
    }
    ,
    afterLoadItem: function () {
        var me = this;
        me.callParent(arguments);
        me.afterloadItemInit();
        me.setTotalMoney();
    }
    ,
    beforeCancel: function () {
        var me = this;
        return true;
    }
    ,
    afterCancel: function () {
        var me = this;
        me.callParent(arguments);
        me.view.getCmp("fileUpload").setDisabled(true);
        me.view.getCmp("fileDelete").setDisabled(true);
        me.setTotalMoney();
    }
    ,
    beforeDelete: function () {
        var me = this;
        return true;
    }
    ,
    afterDelete: function () {
        var me = this;
        me.view.getCmp('originalValueTotal').sotValue("");
        me.view.getCmp('newValueTotal').sotValue("");
    }
    ,
    beforeBatchDel: function () {
        var me = this;
        return true;
    }
    ,
    afterBatchDel: function () {
        var me = this;
    }
    ,
    beforeExport: function () {
        var me = this;
        return true;
    }
    ,
    afterExport: function () {
        var me = this;
    }
    ,
//文件上传
    fileUploadBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        var fileClassify = "CDM_FILE_TYPE_DETAIL";
        Erp.FileUtil.erpFileUpload(grid, fileClassify, this.initFileUploadData, null, null, "SCMCONTRACTCHANGE");
    }
    ,
//文件下载
    fileDownloadBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        Erp.FileUtil.erpFileDownLoad(grid);
    }
    ,
//文件预览
    filePreviewBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        Erp.FileUtil.erpFilePreview(grid);
    }
    ,
//文件删除
    fileDeleteBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        Erp.FileUtil.erpFileDelete(grid);
    }
    ,
    afterloadItemInit: function () {
        var me = this;
        var view = me.view;
        //var reportJPanel = view.getCmp("scmContractReport");
        var scmContractId = view.getCmp("scmContractChangeDto->scmContractId").gotValue();
        var url = Scdp.getSysConfig("base_path") + Scdp.getSysConfig("report_servlet") + '?reportlet=erp/scm/ScmContractSingleQuery.cpt&scmContractId=' + scmContractId;
        var fd = Ext.get('scmContractChangeReportForm');
        if (!fd) {
            fd = Ext.DomHelper.append(
                Ext.getBody(), {
                    tag: 'form',
                    method: 'post',
                    id: 'scmContractChangeReportForm',
                    action: url,
                    target: 'scmContractChangeReportIframe',
                    cls: 'x-hidden',
                    cn: []
                }, true);

        } else {
            $(fd.dom).attr("action", url);
        }
        fd.dom.submit();
    }
    ,
    scmContractIdChangeFn: function (a, b) {
        var me = this;
        var scmContractChangeDDtoGrid = me.view.getCmp("scmContractChangeDDto");
        //清除变更后金额
        me.view.getCmp("scmContractChangeDto->newValue").sotValue("");
        scmContractChangeDDtoGrid.clearData();
        Scdp.doAction("scmcontractchange-loadpuchasereqdetail", {uuid: b}, function (res) {
            scmContractChangeDDtoGrid.sotValue(res.dto);
            me.setTotalMoney();
        }, false, false);
    }
    ,
//M3_C5_F2_页面调整
    pickChasereqPack: function () {
        var me = this;
        var projectName = me.view.getCmp("fadSubjectName").getValue();
        var reqUuids = new Array();
        var reqGridData = me.view.getCmp("scmContractChangeDDto").gotValue();
        for (var i = 0; i < reqGridData.length; i++) {
            reqUuids.push(reqGridData[i].prmPurchaseReqDetailId);
        }

        if (Scdp.ObjUtil.isNotEmpty(projectName)) {
            var prmProjectMainId = me.view.getCmp("scmContractChangeDto->projectId").gotValue();
            var purchasePackageId = me.view.getCmp("scmContractChangeDto->purchasePackageId").gotValue();
            var queryController = Ext.create("Purchaseplan.controller.PickerPurchaseplanController");
            var callBack = function (subView, isCancel) {
                if (isCancel) {
                    return;
                }
                var prmPurchasePlanDetailGrid = subView.getCmp("prmPurchasePlanDetailDto").getSelectedRecords();
                if (Scdp.ObjUtil.isEmpty(prmPurchasePlanDetailGrid)) {
                    return;
                }
                var queryConditions = {};
                var duuids = new Array();
                for (var i = 0; i < prmPurchasePlanDetailGrid.length; i++) {
                    duuids.push(prmPurchasePlanDetailGrid[i].data.uuid);
                }
                queryConditions.duuids = duuids;

                var postData = {queryConditions: queryConditions};
                Scdp.doAction("prmpurchasereq-planquery", postData, function (result) {
                    //stone
                    var hasDat = result.hasDat;
                    if (hasDat) {
                        var planDetails = result.PrmPurchasePlanDetailDto;
                        me.clickAddPlanBtn(planDetails);
                    }
                }, null, false, null, null);
                return true;
            };
            var param = {
                prmProjectMainId: prmProjectMainId,
                purchasePackageId: purchasePackageId,
                reqUuids: reqUuids
            };
            queryController.actionParams = param;
            Scdp.openNewWinByController(queryController, callBack, 'temp_icon_16', "采购明细选择");
        } else {
            Scdp.MsgUtil.info("请选择所属项目！");
        }
    }
    ,
    clickAddPlanBtn: function (planDetails) {
        //stone
        var me = this;
        var reqGrid = me.view.getCmp("scmContractChangeDDto");
        for (var i = 0; i < planDetails.length; i++) {
            planDetails[i].prmPurchasePlanDetailId = planDetails[i].uuid;
            planDetails[i].prmPurchaseReqDetailId = planDetails[i].uuid;
            planDetails[i].planAmount = planDetails[i].remainAmount;
            planDetails[i].handleAmount = planDetails[i].remainAmount;
            planDetails[i].amount = planDetails[i].remainAmount;
            planDetails[i].expectedPrice = planDetails[i].budgetPrice;
            planDetails[i].supplierId = planDetails[i].supplierId;
            planDetails[i].model = planDetails[i].model;
            planDetails[i].factory = planDetails[i].factory;
            planDetails[i].supplierProperty = planDetails[i].supplierProperty;
            planDetails[i].arriveDate = planDetails[i].arriveDate;
            planDetails[i].subTotal = Erp.MathUtil.multiNumber(planDetails[i].handleAmount, planDetails[i].budgetPrice);
            planDetails[i].changeState = "1"
        }
        reqGrid.addRowItems(planDetails, false);
        me.setTotalMoney();
    }
    ,
    afterDeleteRow: function () {
        var me = this;
        me.setTotalMoney();
    }
    ,
    doBeforeDelete: function (a) {
        var me = this;
        var b = this.view.getCmp("scmContractChangeDDto").getSelectionModel().getSelection();
        if (0 == b.length) {
            Scdp.MsgUtil.warn(Scdp.I18N.NO_RECORDS_SELECT)
        } else {
            if (b[0].data.changeState == "0") {

                var flag = false;
                var detailItems = me.view.getCmp('scmContractChangeDDto').getStore().data.items;
                Ext.Array.forEach(detailItems, function (a) {
                    if (a.data.puuid == b[0].data.prmPurchaseReqDetailId) {
                        flag = true;
                        return false;
                    }
                });
                if (flag) {
                    Scdp.MsgUtil.info("该明细已被拆分，如需修改数量请先删除已拆分记录!");
                    return false;
                }
                var amount = b[0].data.handleAmount;
                var needLessAmountId = Ext.id();
                var msgId = Ext.id();
                var window = Ext.create('Ext.window.Window', {
                    width: 280,
                    height: 120,
                    layout: 'border',
                    modal: true,
                    title: "合同变更减少",
                    items: [{
                        xtype: 'form',
                        region: 'center',
                        frame: true,
                        scrollable: true,
                        items: [
                            {
                                xtype: 'numberfield',
                                id: needLessAmountId,
                                fieldLabel: '需要减少的数量',
                                allowDecimals: false,
                                editable: true,
                                minValue: 0,
                                allowNegative: false,
                                listeners: {
                                    change: function (a, b) {
                                        if (Number(b) > Number(amount))
                                            Ext.getCmp(msgId).show();
                                        else
                                            Ext.getCmp(msgId).hide();
                                    }
                                }
                            }, {
                                xtype: 'label',
                                id: msgId,
                                hidden: true,
                                html: "<span style='color: red'>减少数量不能超过原有数量</span>"
                            }
                        ]
                    }],
                    buttons: [{
                        text: '确认',
                        handler: function () {
                            var needLessAmount = Ext.getCmp(needLessAmountId).getValue();
                            if (Scdp.ObjUtil.isEmpty(needLessAmount) || !Ext.getCmp(msgId).isHidden() || needLessAmount <= 0) {
                                Scdp.MsgUtil.info("数据校验失败!");
                                return;
                            }
                            if (Number(needLessAmount) == Number(amount)) {
                                b[0].set("changeState", "2");
                            } else if (Number(needLessAmount) < Number(amount)) {
                                b[0].set("handleAmount", amount - needLessAmount);
                                b[0].set("subTotal", b[0].data.handleAmount * b[0].data.budgetPrice);

                                var reqGrid = me.view.getCmp("scmContractChangeDDto");
                                var newData = Ext.clone(b[0].data);
                                newData.handleAmount = needLessAmount;
                                newData.uuid = "";
                                newData.subTotal = needLessAmount * newData.budgetPrice;
                                newData.changeState = "2";
                                newData.puuid = b[0].data.prmPurchaseReqDetailId;
                                reqGrid.addRowItem(newData, false);
                            }
                            me.setTotalMoney();
                            window.close();
                        }
                    }, {
                        text: '取消',
                        handler: function () {
                            window.close()
                        }
                    }]
                });
                window.show();
            } else if (b[0].data.changeState == "1") {
                return true;
            } else if (b[0].data.changeState == "2") {
                if (Scdp.ObjUtil.isNotEmpty(b[0].data.puuid)) {
                    var detailItems = me.view.getCmp('scmContractChangeDDto').getStore().data.items;
                    Ext.Array.forEach(detailItems, function (a) {
                        if (a.data.prmPurchaseReqDetailId == b[0].data.puuid && a.data.changeState == "0") {
                            a.set("handleAmount", a.data.handleAmount + b[0].data.handleAmount);
                            a.set("subTotal", (a.data.handleAmount * a.data.budgetPrice));
                            return false;
                        }
                    })
                    me.view.getCmp("scmContractChangeDDto").getStore().remove(b);
                } else {
                    b[0].set("changeState", "0");
                }
                me.setTotalMoney();
            }
        }
        return false;
    }
    ,
    natureChangeFn: function (a, b) {
        var me = this;
        if (!me.view.getCmp("editPanel->saveBtn").disabled) {
            if (b == "1") { //外协
                me.view.getCmp("scmContractChangeDto->closeChange").setReadOnly(false);
            } else {
                me.view.getCmp("scmContractChangeDto->closeChange").setReadOnly(true);
                me.view.getCmp("scmContractChangeDto->closeChange").sotValue("0");
            }
        }
    }
    ,
    beforeWorkFlowCommit: function () {
        var me = this;
        var view = me.view;
        var errorMsg = "";
        //首次提交
        //var firstSumbit = me.workFlowFormData[0].indexOf("事业部分管采购领导");
        if (!me.workFlowTaskId) {
            me.isFirstCommit = true;
            if (me.view.getCmp("scmContractChangeDto->contractNature").gotValue() == "1") {
                //外协且是否结算变更要选是
                var closeChange = view.getCmp("scmContractChangeDto->closeChange").gotValue();
                if (closeChange == "1") {
                    var files = view.getCmp("cdmFileRelationDto").getStore().data.items;
                    var flag = false;
                    Ext.Array.forEach(files, function (a) {
                        if (a.data.fileClassify == "SCM_CONTRACT_CHANGE_SETTLEMENT_MATERIAL") {
                            flag = true;
                            return false;
                        }
                    });
                    if (!flag) errorMsg += "请提交外协结算申请材料，谢谢";
                }
            }
        }

        if (Scdp.ObjUtil.isEmpty(errorMsg)) {
            return true;
        } else {
            Scdp.MsgUtil.warn(Erp.I18N.BEFORE_COMMIT_FAILURE + errorMsg);
            return false;
        }
    }
    ,
    loadWorkFlowProcessDeptCode: function () {
        var me = this;
        var processDeptCode = me.view.getCmp('scmContractChangeDto->officeId').gotValue();
        return processDeptCode;
    }
    ,
    collectMoreWorkFlowParamOnLoadAssignee: function () {
        var param = {};
        param.assignLoadUserMethod='default';
        return param;
    },
    executeTask: function () {
        var a = this, b = {};
        b.businessKey = a.gotPrimaryKey();
        b.workFlowDefinitionKey = a.view.workFlowDefinitionKey;
        b.dto = a.dtoClass;
        b.menuCode = this.loadWorkflowMenuCode();
        b.taskId = a.workFlowTaskId;
        b.processDeptCode = a.loadWorkFlowProcessDeptCode();
        var d = a.collectMoreWorkFlowParamOnComplete();
        Scdp.ObjUtil.isNotEmpty(d) && (b.variable = d);
        d = function (c, d, f, g) {
            b.assignee = c;
            b.comment = d;
            b.userFilter = f;
            b.priority = g;
            Scdp.doAction("workflow-complete-action", b, function (b) {
                refreshWorkFlowBarStatus(b, a);
                a.afterCompelteTask(b);
            }, function () {
            }, !0, !1)
        };
        -1 != a.workFlowFormData.indexOf("wf_skip_popup_dialog\x3d1") ? d(null, null, null) : a.chooseAssignee(d, !0, !1, !1, a)
    }
    ,
    afterCompelteTask: function (b) {
        var me = this;
        if (me.isFirstCommit && me.view.getCmp("scmContractChangeDto->closeChange").gotValue() == "1") {
            //M3_C17_F1_供方评价
            var params = {items: ['质量', "组织能力", "服从管理", "安全管理", "结算", "综合评价"]};
            var callback = function (result) {
                Scdp.doAction("supplierinfor-supplierevaluation", {
                    uuid: me.view.getCmp("scmContractChangeDto->supplierCode").gotValue(),
                    scmContractId: me.view.getCmp("scmContractChangeDto->scmContractId").gotValue(),
                    personQuality: result.scoreArray[0],
                    organizingCapability: result.scoreArray[1],
                    compliance: result.scoreArray[2],
                    securityManagement: result.scoreArray[3],
                    finalEstimate: result.scoreArray[4],
                    comprehensive: result.scoreArray[5],
                    evaluateFrom: '2',
                    remark: result.suggest
                }, function (res) {
                });
            };
            Scdp.FiveStarWin.show(params, callback, false, false);
        }
        else {
            b.success && Scdp.MsgUtil.info(Scdp.I18N.MSG_WORKFLOW_COMPLETE_SUCCESS);
        }
    }
    ,
//申请数量和金额检查
    checkAmountFn: function (eventObj, isChanged) {
        var me = this;
        var myView = eventObj.grid.up("IView");
        var detailItems = myView.getCmp('scmContractChangeDDto').getStore().data.items;
        var totalBudget = 0;
        if (detailItems.length < 1) {
            return false;
        }

        var field = eventObj.field;
        var record = eventObj.record;

        var position = {};
        position['row'] = eventObj.rowIdx;
        var controller = Scdp.getActiveModule().controller;
        if (field == "handleAmount") {
            var originalValue = eventObj.originalValue;
            position['column'] = eventObj.colIdx;
            if (record.get("handleAmount") > record.get("planAmount")) {
                Scdp.MsgUtil.info("申请数量超过最大值！");

                var cell = eventObj.view.getCellByPosition(position, true);
                cell.style.backgroundColor = '#EE0000';
                return;
            }
            record.set("subTotal", (record.data.handleAmount * record.data.budgetPrice))
            record.set("amount", (record.data.handleAmount))
            controller.setTotalMoney();
        }
        if (field == "expectedPrice") {
            record.set("subTotal", (record.data.handleAmount * record.data.budgetPrice))
            controller.setTotalMoney();
        }
    }
    ,
    setTotalMoney: function () {
        var me = this;
        var detailItems = me.view.getCmp('scmContractChangeDDto').getStore().data.items;
        var originalTotal = 0;
        var newTotal = 0;
        var backTotal = 0;
        if (detailItems.length < 1) {
            me.view.getCmp('originalValueTotal').sotValue(0);
            me.view.getCmp('newValueTotal').sotValue(0);
            return;
        } else {
            for (var i = 0; i < detailItems.length; i++) {
                if (detailItems[i].get("changeState") == "0") {
                    originalTotal += detailItems[i].get("handleAmount") * detailItems[i].get("budgetPrice");
                } else if (detailItems[i].get("changeState") == "1") {
                    newTotal += detailItems[i].get("handleAmount") * detailItems[i].get("budgetPrice");
                } else if (detailItems[i].get("changeState") == "2") {
                    backTotal += detailItems[i].get("handleAmount") * detailItems[i].get("budgetPrice");
                }
            }
        }
        me.view.getCmp('originalValueTotal').sotValue(originalTotal + backTotal);
        me.view.getCmp('newValueTotal').sotValue(originalTotal + newTotal);
    }


})
;
