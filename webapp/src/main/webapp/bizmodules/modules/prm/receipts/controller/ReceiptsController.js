var isCognateContract = false;
Ext.define("Receipts.controller.ReceiptsController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Receipts.view.ReceiptsView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'prmReceiptsDto->prmContractId', name: 'blur', fn: 'setInternalInfo'},
        {cid: 'editToolbar->certificate', name: 'click', fn: 'certificateClick'},
        {cid: 'editToolbar->toCertificate', name: 'click', fn: 'toCertificate'},
        {cid: 'prmReceiptsDto->moneyType', name: 'change', fn: 'setSubBtnToolState'},
        {cid: 'prmReceiptsDto->isInternal', name: 'change', fn: 'fnIsInternalChange'},
        {cid: 'fileUpload', name: 'click', fn: 'fileUploadBtn'},
        {cid: 'fileDownload', name: 'click', fn: 'fileDownloadBtn'},
        {cid: 'fileDelete', name: 'click', fn: 'fileDeleteBtn'},
        {cid: 'filePreview', name: 'click', fn: 'filePreviewBtn'},
        {cid: 'cognateContract', name: 'click', fn: 'cognateContractFn'},
        {cid: 'btnExamDate', name: 'click', fn: 'fnExamDate'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.receipts.dto.PrmReceiptsDto',
    queryAction: 'receipts-query',
    loadAction: 'receipts-load',
    addAction: 'receipts-add',
    modifyAction: 'receipts-modify',
    deleteAction: 'receipts-delete',
    exportXlsAction: "receipts-exportxls",
    afterInit: function () {
        var me = this;
        me.callParent(arguments);

        me.setPrmbillingOpen();
        me.controlFileButton();
    },
    setInternalInfo: function () {
        var me = this;
        var view = me.view;
        var prmProjectMainId = view.getCmp("prmReceiptsDto->prmProjectMainId").gotValue();
        var prmContractId = view.getCmp("prmReceiptsDto->prmContractId").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(prmProjectMainId)) {
            var postData = {prmProjectMainId: prmProjectMainId, prmContractId: prmContractId};
            var internalOfficeCmp = view.getCmp("prmReceiptsDto->internalOffice");
            var isInternalCmp = view.getCmp("prmReceiptsDto->isInternal");
            var result = Scdp.doAction("receipts-internal", postData, null, null, true, false);
            isInternalCmp.sotValue(Number(result["isInternal"]));
            internalOfficeCmp.putValue(result["internalOffice"]);
        }
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("prmReceiptsDto->state").sotValue("0");
        view.getCmp("prmReceiptsDto->receivableBalance").sotEditable(false);
        me.setSubBtnToolState();
        me.controlFileButton();
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        me.controlFileButton();
    },
    beforeModify: function () {
        var me = this;
        return true;
    },
    afterModify: function () {
        var me = this;
        var view = me.view;
        //财务只能修改对冲金额
        var moneyType = view.getCmp("editPanel->moneyType").gotValue();
        var enableNull = me.workFlowFormData.indexOf("wf_enable_actualMoney_actualDate=1");
        if (enableNull != "-1" && "HEDGE" == moneyType) {
            view.getCmp('prmReceiptsDto').sotEditable(false);
            Ext.Array.each(view.getCmp("toolbar").items.items, function (a) {
                a.setDisabled(true);
            })
        } else {
            if (view.getCmp("prmReceiptsDto->isInternal").gotValue() == 1) {
                view.getCmp("prmReceiptsDto->moneyType").sotEditable(false);
            } else {
                view.getCmp("prmReceiptsDto->moneyType").sotEditable(true);
            }
            view.getCmp("prmReceiptsDto->receivableBalance").sotEditable(false);
            me.setSubBtnToolState();
        }

        //M3_C7_F4_关联合同
        if (isCognateContract) {
            Ext.Array.each(me.view.getCmp("prmReceiptsDto").items.items, function (a) {
                if (a.cid == "prmContractId")
                    a.sotEditable(true);
                else
                    a.sotEditable(false);
            })
        }
        var role = Erp.Util.getCurrentUserRoleName();
        var state = me.view.getCmp("prmReceiptsDto->state").gotValue();
        if (role.ROLE.indexOf("会计") > -1 && state == '1') {
            me.controlEditable();
        }
        me.controlFileButton();
    },
    beforeSave: function () {
        var me = this;
        var view = me.view;
        //var estimateDate = view.getCmp("prmReceiptsDto->estimateDate").gotValue();
        //var actualDate = view.getCmp("prmReceiptsDto->actualDate").gotValue();
        //if (estimateDate) {
        //    estimateDate.setMonth(estimateDate.getMonth() + 3);
        //    if (actualDate) {
        //        if (actualDate > estimateDate) {
        //            Scdp.MsgUtil.warn("保存失败！实际到款时间与预计到款时间不能超过 3 个月");
        //            return false;
        //        }
        //    }
        //}

        return true;
    },
    afterSave: function (retData) {
        var me = this;
        me.callParent(arguments);
        me.controlFileButton();
        var uuid = me.view.getCmp("prmReceiptsDto->uuid").gotValue();
        me.loadItem(uuid);
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        var view = me.view;
        me.callParent(arguments);
        view.getCmp("editPanel->certificate").setDisabled(true);
        view.getCmp("editPanel->toCertificate").setDisabled(true);
        if (view.getCmp("prmReceiptsDto->state").gotValue() == "2" && view.getCmp("prmReceiptsDto->isInternal").gotValue() != "1") {
            view.getCmp("editPanel->certificate").setDisabled(false);
        }
        else if (view.getCmp("prmReceiptsDto->state").gotValue() == "4" || view.getCmp("prmReceiptsDto->state").gotValue() == "8") {
            view.getCmp("editPanel->toCertificate").setDisabled(false);
        }
        //M3_C7_F4_关联合同
        var state = view.getCmp("prmReceiptsDto->state").gotValue();
        var prmContractId = view.getCmp("prmReceiptsDto->prmContractId").gotValue();
        if ((state == "2" || state == "4" || state == "8") && Scdp.ObjUtil.isEmpty(prmContractId)) {
            view.getCmp("cognateContract").setDisabled(false);
        } else {
            view.getCmp("cognateContract").setDisabled(true);
        }
        me.setWflBtnState();
        me.controlFileButton();
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
        me.callParent(arguments);
        me.controlFileButton();
    },
    beforeDelete: function () {
        var me = this;
        return true;
    },
    afterDelete: function () {
        var me = this;
        me.controlFileButton();
    },
    beforeBatchDel: function () {
        var me = this;
        return true;
    },
    afterBatchDel: function () {
        var me = this;
        me.controlFileButton();
    },
    beforeExport: function () {
        var me = this;
        return true;
    },
    afterExport: function () {
        var me = this;
    },
    certificateClick: function (obj) {
        var me = this;
        var view = me.view;
        var state = view.getCmp("prmReceiptsDto->state").gotValue();
        if (state != "2") {
            Scdp.MsgUtil.warn("只有审批通过的单据才能生成凭证！");
        } else {
            var uuid = view.getCmp("prmReceiptsDto->uuid").gotValue();
            var param = {uuid: uuid};
            Scdp.doAction("receipts-certificate-create", param, function (result) {
                if (Scdp.ObjUtil.isNotEmpty(result.result) && result.result == true) {
                    me.loadItem(uuid);
                    Scdp.MsgUtil.info("操作成功！");
                    Erp.Util.gotoCertificateModule(result.fadCertificateUuid);
                }
                else {
                    Scdp.MsgUtil.warn("操作失败！" + result.msg);
                }
            });
        }
    },

    //调出凭证
    toCertificate: function () {
        var me = this;
        var view = me.view;
        var uuid = view.getCmp('prmReceiptsDto->uuid').gotValue();
        if (Scdp.ObjUtil.isEmpty(uuid)) {
            Scdp.MsgUtil.info("未选择数据");
            return;
        }
        var postData = {uuid: uuid};
        var actionResult = Scdp.doAction("certificate-getFadCertificateUuid", postData, null, null, true, false);
        Erp.Util.gotoCertificateModule(actionResult.fadCertificateUuid);
    },
    beforeWorkFlowCommit: function () {
        var me = this;
        var view = me.view;
        var enableNull = me.workFlowFormData.indexOf("wf_enable_actualMoney_actualDate=1");
        var isPreProject = view.getCmp("prmReceiptsDto->isPreProject").gotValue();
        var prmContractId = view.getCmp("prmReceiptsDto->prmContractId").gotValue();
        var customerName = view.getCmp("prmReceiptsDto->customerId").gotValue();
        var prmProjectMainId = view.getCmp("prmReceiptsDto->prmProjectMainId").gotValue();
        var errorMsg = "";
        if (enableNull != "-1") {
            var actualDate = view.getCmp("prmReceiptsDto->actualDate").gotValue();
            var actualMoney = view.getCmp("prmReceiptsDto->actualMoney").gotValue();
            if (actualDate == null || actualMoney == null) {
                errorMsg += Erp.Const.BREAK_LINE + "“实际收款金额”和“实际到款时间”" + Erp.I18N.CAN_NOT_EMPTY;
            }
            var moneyType = view.getCmp("prmReceiptsDto->moneyType").gotValue();
            var isInternal = view.getCmp("prmReceiptsDto->isInternal").gotValue();
            if (Scdp.ObjUtil.isEmpty(moneyType) && "1" != isInternal) {
                errorMsg += Erp.Const.BREAK_LINE + "“付款方式”" + Erp.I18N.CAN_NOT_EMPTY;
            }

            if ("HEDGE" == moneyType) {
                var dtoGrid = view.getCmp("prmReceiptsScmInvoiceDto");
                var count = dtoGrid.getStore().getCount();
                if (count <= 0) {
                    errorMsg += Erp.Const.BREAK_LINE + "付款方式为对冲,对冲合同信息" + Erp.I18N.CAN_NOT_EMPTY;
                }
                var selectedRecords = dtoGrid.store.data.items;
                var hedgeMoneySum = 0;
                Ext.Array.each(selectedRecords, function (a) {
                    if (Scdp.ObjUtil.isEmpty(a.data.hedgeMoney)) {
                        errorMsg += Erp.Const.BREAK_LINE + "“对冲金额”" + Erp.I18N.CAN_NOT_EMPTY;
                        return false;
                    }

                    //2017-01-19，对冲金额可以小于0,用于处理红冲对应的项目收款对冲数据
//                    if (Scdp.ObjUtil.isNotEmpty(a.data.hedgeMoney) && a.data.hedgeMoney <= 0) {
//                        errorMsg += Erp.Const.BREAK_LINE + "“对冲金额”要大于零！";
//                        return false;
//                    }
                    if (Scdp.ObjUtil.isNotEmpty(a.data.hedgeMoney) && a.data.needPayMoneyLock < 0) {
                        errorMsg += Erp.Const.BREAK_LINE + "“对冲金额”不能大于“可对冲应付账款”！";
                        return false;
                    }
                    hedgeMoneySum = Erp.MathUtil.plusNumber(hedgeMoneySum, a.data.hedgeMoney, false);

                });

                if (Scdp.ObjUtil.isNotEmpty(actualMoney) && hedgeMoneySum.toFixed(2) != actualMoney.toFixed(2)) {
                    errorMsg += Erp.Const.BREAK_LINE + "“对冲金额合计”与“实际收款金额”不一致！";
                }

            }

        }

        if (!me.workFlowTaskId) {
            if (isPreProject != "1") {
                if (Scdp.ObjUtil.isEmpty(prmContractId)) {
                    errorMsg += Erp.Const.BREAK_LINE + "非预立项项目“合同名称”" + Erp.I18N.CAN_NOT_EMPTY;
                }
                if (Scdp.ObjUtil.isEmpty(customerName)) {
                    errorMsg += Erp.Const.BREAK_LINE + "非预立项项目“业主单位”" + Erp.I18N.CAN_NOT_EMPTY;
                }
                //M3_C7_F4_关联合同
                Scdp.doAction("receipts-billingisnull", {prmProjectMainId: prmProjectMainId}, function (res) {
                    if (Scdp.ObjUtil.isNotEmpty(res) && res.result > 0) {
                        errorMsg += Erp.Const.BREAK_LINE + "该项目存在未关联合同的开票申请和项目收款数据,请先处理！";
                    }
                }, false, false, false, null);
            }

            var moneyType = view.getCmp("prmReceiptsDto->moneyType").gotValue();
            var isInternal = view.getCmp("prmReceiptsDto->isInternal").gotValue();
            //内委的可以为空，不是内委的不能为空
            if (isInternal != '1') {
                if (Scdp.ObjUtil.isEmpty(moneyType)) {
                    errorMsg += Erp.Const.BREAK_LINE + "“付款方式”" + Erp.I18N.CAN_NOT_EMPTY;
                }

                if ("HEDGE" == moneyType) {
                    var dtoGrid = view.getCmp("prmReceiptsScmInvoiceDto");
                    var count = dtoGrid.getStore().getCount();
                    if (count <= 0) {
                        errorMsg += Erp.Const.BREAK_LINE + "付款方式为对冲,对冲合同信息" + Erp.I18N.CAN_NOT_EMPTY;
                    }

                    var selectedRecords = dtoGrid.store.data.items;
                    var hedgeMoneySum = 0;
                    Ext.Array.each(selectedRecords, function (a) {
                        if (Scdp.ObjUtil.isEmpty(a.data.hedgeMoney)) {
                            errorMsg += Erp.Const.BREAK_LINE + "“对冲金额”" + Erp.I18N.CAN_NOT_EMPTY;
                            return false;
                        }
//                        if (Scdp.ObjUtil.isNotEmpty(a.data.hedgeMoney) && a.data.hedgeMoney <= 0) {
//                            errorMsg += Erp.Const.BREAK_LINE + "“对冲金额”要大于零！";
//                            return false;
//                        }
                        //首次提交，金额还没被锁定
                        if (Scdp.ObjUtil.isNotEmpty(a.data.hedgeMoney) && a.data.hedgeMoney > a.data.needPayMoneyLock) {
                            errorMsg += Erp.Const.BREAK_LINE + "“对冲金额”不能大于“可对冲应付账款”！";
                            return false;
                        }
                        hedgeMoneySum = Erp.MathUtil.plusNumber(hedgeMoneySum, a.data.hedgeMoney, false);

                    });

                }

            }
        }

        if (Scdp.ObjUtil.isEmpty(errorMsg)) {
            return true;
        } else {
            Erp.Util.showLogView(Erp.I18N.BEFORE_COMMIT_FAILURE + errorMsg);
            return false;
        }
    },
    loadWorkFlowProcessDeptCode: function () {
        var me = this;
        var tranferDeptCode = me.workFlowFormData.indexOf("wf_transfer_deptCode=1");
        var processDeptCode = "";
        if (tranferDeptCode != "-1") {
            processDeptCode = me.view.getCmp('prmReceiptsDto->internalOffice').gotValue();
        } else {
            processDeptCode = me.view.getCmp('prmReceiptsDto->departmentCode').gotValue();
        }
        return processDeptCode;
    },
    fnIsInternalChange: function () {
        var me = this;
        var view = me.view;
        if (!isCognateContract) {  //M3_C7_F4_关联合同
            if (view.getCmp("prmReceiptsDto->isInternal").gotValue() == 1) {
                view.getCmp("prmReceiptsDto->moneyType").sotValue("");
                view.getCmp("prmReceiptsDto->moneyType").sotEditable(false);
            } else {
                view.getCmp("prmReceiptsDto->moneyType").sotEditable(true);
            }
        }

    },
    pickContract: function (a, b) {
        var me = this;
        var view = me.view;
        var queryController = Ext.create("Receipts.controller.PrmContractQueryController");
        var selectId = "";
        Ext.Array.each(view.getCmp("prmReceiptsScmInvoiceDto").store.data.items, function (a) {
            selectId += "'" + a.data.scmContractUuid + "',"
        })
        queryController.actionParams = {
            selectId: selectId.substr(0, selectId.length - 1),
            payer: view.getCmp("prmReceiptsDto->payerDesc").gotValue()
        }

        var callBack = function (subView, isCancel) {
            var grid = subView.getQueryPanel().ownerCt.getCmp('resultPanel');
            var selectedRecords = grid.getSelectedRecords();
            var map = new Map()
            Ext.Array.each(view.getCmp("prmReceiptsScmInvoiceDto").store.data.items, function (a) {
                map.put(a.data.scmContractUuid.toUpperCase(), "");
            })
            if (selectedRecords.length > 0) {
                var dtoGrid = view.getCmp("prmReceiptsScmInvoiceDto");
                Ext.Array.each(selectedRecords, function (a) {
                    var rowData = a.data;
                    var c = dtoGrid.getStore(), d = Ext.ModelManager.create({}, dtoGrid.store.model.modelName);
                    if (Scdp.ObjUtil.isNotEmpty(rowData)) {
                        if (!map.containsKey(rowData.uuid.toUpperCase())) {
                            d.set("scmContractUuid", rowData.uuid);
                            d.set("officeId", rowData.officeId);
                            d.set("officeIdDesc", rowData.officeName);
                            d.set("projectCode", rowData.projectCode);
                            d.set("projectName", rowData.projectName);
                            d.set("needPayMoney", rowData.scmNeedPayMoney);
                            d.set("scmContractUuidDesc", rowData.scmContractCode);
                            d.set("needPayMoneyLock", rowData.scmNeedPayMoneyLock);
                            d.set("hedgeMoney", 0);
                            d.set("seqNo", Scdp.getMaxSnoInStore(c) + 1);
                            dtoGrid.store.insert(c.getCount(), d);
                        }
                    }
                });
            }
            subView.up('window').close();
        }
        Scdp.openNewWinByController(queryController, callBack, 'temp_icon_16', '对冲合同选择');
    },
    setSubBtnToolState: function () {
        var me = this;
        var view = me.view;
        var moneyType = view.getCmp("editPanel->moneyType").gotValue();
        if (!view.getCmp("editPanel->saveBtn").disabled && "HEDGE" == moneyType) {
            //view.getCmp("toolbar").setDisabled(false);
            Ext.Array.each(view.getCmp("toolbar").items.items, function (a) {
                a.setDisabled(false);
            })
        } else {
            //view.getCmp("toolbar").setDisabled(true);
            Ext.Array.each(view.getCmp("toolbar").items.items, function (a) {
                a.setDisabled(true);
            })
            view.getCmp("prmReceiptsScmInvoiceDto").clearData();
        }
    },
    //文件上传
    fileUploadBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        var fileClassify = "";
        var fileObjConfig = {};
        fileObjConfig.regex = /(.)+((\.pdf)|(\.doc)|(\.docx)|(\.xls)|(\.xlsx)|(\.ppt)|(\.pptx)|(\.txt)(\w)?)$/i;
        fileObjConfig.regexText = '支持的文件格式：pdf,doc,docx,xls,xlsx,ppt,pptx,txt';
        Erp.FileUtil.erpFileUpload(grid, fileClassify, this.initFileUploadData, null, fileObjConfig);
    },
    //文件下载
    fileDownloadBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        Erp.FileUtil.erpFileDownLoad(grid);
    },
    //文件预览
    filePreviewBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        Erp.FileUtil.erpFilePreview(grid);
    },
    //文件删除
    fileDeleteBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        Erp.FileUtil.erpFileDelete(grid);
    },
    //文件按钮的禁用启用
    controlFileButton: function () {
        var me = this;
        var status = Ext.getCmp("editStatus").getValue();
        if (Scdp.ObjUtil.isEmpty(status)) {
            me.view.getCmp("fileUpload").setDisabled(true);
            me.view.getCmp("fileDownload").setDisabled(true);
            me.view.getCmp("fileDelete").setDisabled(true);
            return false;
        }
        if (status == Scdp.I18N.VIEW_STATUS) {
            me.view.getCmp("fileUpload").setDisabled(true);
            me.view.getCmp("fileDelete").setDisabled(true);
            me.view.getCmp("fileDownload").setDisabled(false);
        } else if (status == Scdp.I18N.MODIFY_STATUS || status == Scdp.I18N.ADD_STATUS) {
            me.view.getCmp("fileUpload").setDisabled(false);
            me.view.getCmp("fileDownload").setDisabled(false);
            me.view.getCmp("fileDelete").setDisabled(false);
        }
    },
    setWflBtnState: function () {
        var me = this;
        var view = me.view;
        var enableNull = me.workFlowFormData.indexOf("wf_enable_actualMoney_actualDate=1");
        if (enableNull != "-1" && !view.getCmp('editToolbar->workFlowCommitBtn').disabled) {
            view.getCmp('editToolbar->modifyBtn').setDisabled(false);
        }
    },
    setPrmbillingOpen: function () {
        var me = this;
        if (Scdp.ObjUtil.isNotEmpty(me.actionParams) && me.actionParams.flag == "1") {
            new Ext.util.DelayedTask(function () {
                me.view.getCmp("queryPanel->addNew1Btn").focus();
                me.view.getCmp("queryPanel->addNew1Btn").fireEvent("click");
                //赋值
                me.view.getCmp("prmReceiptsDto->projectCode").sotValue(me.actionParams.projectCode);
                me.view.getCmp("prmReceiptsDto->prmProjectMainId").sotValue(me.actionParams.prmProjectMainId);
                me.view.getCmp("prmReceiptsDto->projectName").sotValue(me.actionParams.projectName);
                me.view.getCmp("prmReceiptsDto->customerIdDesc").sotValue(me.actionParams.customerName);
                me.view.getCmp("prmReceiptsDto->customerId").sotValue(me.actionParams.customerId);
                me.view.getCmp("prmReceiptsDto->estimateMoney").sotValue(me.actionParams.invoiceMoney);
                me.view.getCmp("prmReceiptsDto->payerDesc").sotValue(me.actionParams.customerName);
                me.view.getCmp("prmReceiptsDto->payer").sotValue(me.actionParams.customerId);
                me.view.getCmp("prmReceiptsDto->estimateDate").sotValue(me.actionParams.estimateDate);
                me.actionParams = {};
            }).delay(2000)
        }
    },
    //M3_C7_F4_关联合同
    cognateContractFn: function () {
        var me = this;
        me.view.getCmp("editToolbar->modifyBtn").focus();
        me.view.getCmp("editToolbar->modifyBtn").fireEvent("click");

        isCognateContract = true;
    },
    controlEditable: function () {
        var me = this;
        var view = me.view;
        view.getCmp("prmReceiptsDto").sotEditable(false);
        view.getCmp("prmReceiptsScmInvoiceDto").sotEditable(true);
        view.getCmp("cdmFileRelationDto").sotEditable(false);
        view.getCmp("prmReceiptsDto->actualMoney").sotEditable(true);
        view.getCmp("prmReceiptsDto->actualDate").sotEditable(true);
        view.getCmp("fileUpload").setDisabled(true);
        view.getCmp("fileDelete").setDisabled(true);
    },
    fnExamDate: function () {
        var me = this;
        var examDate = me.view.getCmp("prmReceiptsDto->examDate").gotValue();
        var uuid = me.view.getCmp("prmReceiptsDto->uuid").gotValue();
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
            childForm.getCmp('tableName').sotValue('com.csnt.scdp.bizmodules.entity.prm.PrmReceipts');
            childForm.getCmp('columnName').sotValue('examDate');
            childForm.getCmp('dataUuid').sotValue(uuid);
            childForm.getCmp('oldDate').sotValue(examDate);
        }
    }
});