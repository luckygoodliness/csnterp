Ext.define("Finalestimate.controller.FinalestimateController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Finalestimate.view.FinalestimateView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'editPanel->squareProjectMoney', name: 'blur', fn: 'changeSquareProjectMoney'},
        //{cid: 'editPanel->squareCost', name: 'blur', fn: 'changeSquareCost'},
        {cid: 'editPanel->squareGrossProfit', name: 'blur', fn: 'changeSquareGrossProfit'},
        {cid: 'editPanel->btnProjectReport', name: 'click', fn: 'doBtnProjectReport'},
        //{cid: 'editPanel->btnShowContractInOut', name: 'click', fn: 'showContractInOut'},
        {cid: 'editPanel->btnModifyTax', name: 'click', fn: 'doBtnModifyTaxCorrection'},
        {cid: 'editPanel->btnModifyCost', name: 'click', fn: 'doBtnModifyCost'},
        {cid: 'btnExamDate', name: 'click', fn: 'fnExamDate'},
        {cid: 'btnrExamDate', name: 'click', fn: 'fnExamDate'}

    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.finalestimate.dto.PrmFinalEstimateDto',
    queryAction: 'finalestimate-query',
    loadAction: 'finalestimate-load',
    addAction: 'finalestimate-add',
    modifyAction: 'finalestimate-modify',
    deleteAction: 'finalestimate-delete',
    exportXlsAction: "finalestimate-exportxls",
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
        view.getCmp("prmFinalEstimateDto->state").sotValue("0");
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("prmFinalEstimateDto->state").sotValue("0");
        me.view.getCmp("prmFinalEstimateDto->taxCorrection").sotValue(null);
    },
    beforeModify: function () {
        var me = this;
        return true;
    },
    afterModify: function () {
        var me = this;
        var view = me.view;
        var squareType = view.getCmp("prmFinalEstimateDto->squareType").gotValue();
        if ("1" == squareType || "3" == squareType) {
            view.getCmp("prmFinalEstimateDto->squareProjectMoney").sotEditable(false);
            //view.getCmp("prmFinalEstimateDto->squareCost").sotEditable(false);
        }
    },
    beforeSave: function () {
        var me = this;
        var view = me.view;
        var prmProjectMainId = view.getCmp("prmFinalEstimateDto->prmProjectMainId").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(prmProjectMainId)) {
            //validate
            var squareProjectMoney = view.getCmp("prmFinalEstimateDto->squareProjectMoney").gotValue();
            var registeredMoney = view.getCmp("prmFinalProjectInfoDto->registeredMoney").gotValue();
            var registeredReceiveMoney = view.getCmp("prmFinalProjectInfoDto->registeredReceiveMoney").gotValue();
            var squareCost = view.getCmp("prmFinalEstimateDto->squareCost").gotValue();
            var registeredCost = view.getCmp("prmFinalProjectInfoDto->registeredCost").gotValue();
            var squareGrossProfit = view.getCmp("prmFinalEstimateDto->squareGrossProfit").gotValue();
            var registeredProfit = view.getCmp("prmFinalProjectInfoDto->registeredProfit").gotValue();
            var squareType = view.getCmp("prmFinalEstimateDto->squareType").gotValue();
            var isArchiving = view.getCmp("prmFinalEstimateDto->isArchiving").gotValue();
            var errors = [];
            if (Erp.MathUtil.compare(squareProjectMoney, registeredMoney) > 0) {
                errors.push("项目结算金额不能大于在册运行额！");
            }
            if (Erp.MathUtil.compare(squareProjectMoney, registeredReceiveMoney) > 0) {
                errors.push("项目结算金额不能大于在册收款额！");
            }
            if (Erp.MathUtil.compare(squareCost, registeredCost) > 0) {
                errors.push("项目结算成本不能大于在册成本！");
            }
            if (squareType != "1" && Erp.MathUtil.compare(squareGrossProfit, registeredProfit) > 0) {
                errors.push("项目阶段结算利润不能大于在册利润！");
            }
            if (errors.length > 0) {
                var errorMsg = "";
                Ext.Array.forEach(errors, function (error) {
                    errorMsg += Erp.Const.BREAK_LINE + error;
                });
                Erp.Util.showLogView(Erp.I18N.BEFORE_SAVE_VALIDATE_FAILURE + errorMsg);
                return false;
            }

            var result = Scdp.doAction("finalestimate-check", me.view.gotValue(), null, null, true, false);
            if (result.errorList) {
                var errorMsg = "";
                Ext.Array.forEach(result.errorList, function (error) {
                    errorMsg += Erp.Const.BREAK_LINE + error;
                });
                Erp.Util.showLogView(Erp.I18N.BEFORE_SAVE_VALIDATE_FAILURE + errorMsg);
                return false;
            }
            if (result.infoList) {
                var infoMsg = "";
                Ext.Array.forEach(result.infoList, function (info) {
                    infoMsg += Erp.Const.BREAK_LINE + info;
                });
                Erp.Util.showLogView(Erp.I18N.BEFORE_SAVE_VALIDATE_FAILURE + infoMsg);
            }
        }
        return true;
    },
    afterSave: function (retData) {
        var me = this;
        me.callParent(arguments);
        //me.doQuery();
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        me.callParent(arguments);
        me.view.afterChangeUIStatus();
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

    doAdd: function () {
        var me = this;
        var view = me.view;

        var callback = function (form) {
            if (!form.validate()) {
                Scdp.MsgUtil.warn(Erp.I18N.INFO_NOT_ENOUGH);
                return false;
            }
            var squareType = form.getCmp("squareType").gotValue();
            var prmProjectMainId = form.getCmp("prmProjectMainId").gotValue();
            var projectName = form.getCmp("prmProjectMainIdDesc").gotValue();
            //this.calInvoiceMoney(prmProjectMainId);

            var postdata = {};
            postdata.prmProjectMainId = prmProjectMainId;
            postdata.squareType = squareType;
            var result = Scdp.doAction("finalestimate-assignment", postdata, null, null, true, false);
            if (result.errors) {
                var errorMsg = "";
                Ext.Array.forEach(result.errors, function (error) {
                    errorMsg += Erp.Const.BREAK_LINE + error;
                });
                Erp.Util.showLogView("项目信息不符：" + errorMsg);
                return false;
            } else if (result.infoMsg) {
                win.close();
                Scdp.MsgUtil.confirm(result.infoMsg + "<br/>" + "继续进行该项目的完工结算吗？", function (opt) {
                    if (opt == "yes") {
                        view.setUIStatus(Scdp.Const.UI_INFO_STATUS_NEW);
                        if (squareType == '1') {
                            view.getCmp("prmFinalEstimateDto->squareProjectMoney").sotEditable(false);
                            //view.getCmp("prmFinalEstimateDto->squareCost").sotEditable(false);
                        }
                        view.sotValue(result, true);
                        return true;
                    }
                });

            } else {
                view.setUIStatus(Scdp.Const.UI_INFO_STATUS_NEW);
                if (squareType == '1' || squareType == '3') {
                    view.getCmp("prmFinalEstimateDto->squareProjectMoney").sotEditable(false);
                    //view.getCmp("prmFinalEstimateDto->squareCost").sotEditable(false);
                }
                view.sotValue(result, true);
                return true;
            }
        };
        var form = Ext.create("Scdp.container.JForm", {
            cid: 'finalEstimateProjectForm',
            height: 150,
            width: 400,
            layout: 'form',
            bodyPadding: '10 10 15 10',
            items: [
                {
                    xtype: 'JHidden',
                    cid: 'prmProjectMainIdDesc'
                },
                {
                    xtype: 'JNoneFinishedProject',
                    cid: "prmProjectMainId",
                    fieldLabel: "项目名称",
                    allowBlank: false,
                    descField: "projectName",
                    valueField: "uuid"
                },
                {
                    xtype: 'JCombo',
                    cid: 'squareType',
                    fieldLabel: "结算类型",
                    allowBlank: false,
                    comboType: "scdp_fmcode",
                    codeType: "PRM_SQUARE_TYPE",
                    multiSelect: false,
                    displayDesc: true,
                    fullInfo: false
                }
            ]
        });
        var win = Scdp.openNewWinByView(form, callback, null, "项目结算");
    },

    changeSquareProjectMoney: function (obj) {
        var me = this;
        var view = me.view;
        if (obj.isEditable() && obj.isChanged()) {
            //validate square money
            var squareProjectMoney = obj.gotValue();

            var amount1 = me.getMaxSquareMoney();
            var amount2 = me.getMinSquareMoney();
            if (squareProjectMoney > amount1 || squareProjectMoney < amount2) {
                Scdp.MsgUtil.warn("结算运行额范围为（"
                    + Ext.util.Format.number(amount2, '0.00')
                    + ", "
                    + Ext.util.Format.number(amount1, '0.00')
                    + "）元！");
                obj.sotValue(obj.originalValue);
                return;
            }

            me.calSquarePercent();
            me.calSquareCost();
            me.calProfit();
            me.calTax();
        }
    },
    calSquarePercent: function () {
        var me = this;
        var view = me.view;
        var prmProjectMainId = view.getCmp("prmFinalEstimateDto->prmProjectMainId").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(prmProjectMainId)) {
            var squareProjectMoney = view.getCmp("prmFinalEstimateDto->squareProjectMoney").gotValue();
            var registeredMoney = view.getCmp("prmFinalProjectInfoDto->registeredMoney").gotValue();
            if (Erp.MathUtil.compare(squareProjectMoney, registeredMoney) > 0) {
                Scdp.MsgUtil.info("项目结算金额不能大于在册运行额！");
                return;
            }
            var totalSquareMoney = view.getCmp("prmFinalProjectInfoDto->totalSquareMoney").gotValue();
            var squareMoneySum = view.getCmp("prmFinalProjectInfoDto->squareMoneySum").gotValue();
            var percent = 0;
            if (totalSquareMoney > 0) {
                percent = Erp.MathUtil.divideNumber(Erp.MathUtil.plusNumber(squareMoneySum, squareProjectMoney) * 100, totalSquareMoney);
            }
            var squareProportionCmp = view.getCmp("prmFinalEstimateDto->squareProportion");
            squareProportionCmp.sotValue(percent);
        }
    },
    calProfit: function () {
        var me = this;
        var view = me.view;
        var squareProjectMoney = view.getCmp("prmFinalEstimateDto->squareProjectMoney").gotValue();
        var squareCost = view.getCmp("prmFinalEstimateDto->squareCost").gotValue();
        var squareProfit = Erp.MathUtil.minusNumber(squareProjectMoney, squareCost);
        me.view.getCmp("prmFinalEstimateDto->squareGrossProfit").sotValue(squareProfit);
    },
    calTax: function () {
        var me = this;
        var squareProjectMoney = me.view.getCmp("prmFinalEstimateDto->squareProjectMoney").gotValue();
        var totalSquareMoney = me.view.getCmp("prmFinalProjectInfoDto->totalSquareMoney").gotValue();
        var squareMoneySum = me.view.getCmp("prmFinalProjectInfoDto->squareMoneySum").gotValue();
        var plannedTax = me.view.getCmp("prmFinalProjectInfoDto->plannedTax").gotValue();
        var raxSum = me.view.getCmp("prmFinalProjectInfoDto->raxSum").gotValue();
        var taxAmount = 0;
        if (Scdp.ObjUtil.isNotEmpty(totalSquareMoney) && totalSquareMoney != 0) {
            taxAmount = Erp.MathUtil.minusNumber(
                Erp.MathUtil.divideNumber(
                    Erp.MathUtil.multiNumber(Erp.MathUtil.plusNumber(squareProjectMoney, squareMoneySum), plannedTax, 4),
                    totalSquareMoney),
                raxSum);
        }
        me.view.getCmp("prmFinalEstimateDto->rax").sotValue(taxAmount);
    },
    changeSquareCost: function (obj) {
        var me = this;
        var view = me.view;
        if (obj.isEditable() && obj.isChanged()) {
            me.calSquareCost();
            me.calProfit();
        }
    },
    getSquareCost: function () {
        var me = this;
        var view = me.view;
        var squareMoneySum = view.getCmp("prmFinalProjectInfoDto->squareMoneySum").gotValue();
        var tmpProjectSquareSum = Erp.MathUtil.plusNumber(view.getCmp("prmFinalEstimateDto->squareProjectMoney").gotValue(), squareMoneySum);
        var plannedCost = view.getCmp("prmFinalProjectInfoDto->plannedCost").gotValue();
        var totalSquareMoney = view.getCmp("prmFinalProjectInfoDto->totalSquareMoney").gotValue();
        var squareCostSum = view.getCmp("prmFinalProjectInfoDto->squareCostSum").gotValue();
        var lockedBudgetSum = view.getCmp("prmFinalProjectInfoDto->lockedBudgetSum").gotValue();
        var billedCostSum = view.getCmp("prmFinalProjectInfoDto->billedCostSum").gotValue();
        if (Erp.MathUtil.multiNumber(tmpProjectSquareSum, plannedCost) >= Erp.MathUtil.multiNumber(lockedBudgetSum, totalSquareMoney)) {
            return Erp.MathUtil.minusNumber(lockedBudgetSum, squareCostSum);
        } else if (Erp.MathUtil.multiNumber(tmpProjectSquareSum, plannedCost) > Erp.MathUtil.multiNumber(billedCostSum, totalSquareMoney)) {
            if (totalSquareMoney <= 0) {
                return 0;
            } else {
                return Erp.MathUtil.minusNumber(Erp.MathUtil.divideNumber(Erp.MathUtil.multiNumber(tmpProjectSquareSum, plannedCost, 4), totalSquareMoney), squareCostSum);
            }
        } else {
            return Erp.MathUtil.minusNumber(billedCostSum, squareCostSum);
        }
    },
    calSquareCost: function () {
        var me = this;
        var view = me.view;
        view.getCmp("prmFinalEstimateDto->squareCost").sotValue(me.getSquareCost());
    },
    getMaxSquareMoney: function () {
        var me = this;
        var view = me.view;
        //get the max square money by 成本比例
        var projectActualMoneySum = view.getCmp("prmFinalProjectInfoDto->projectActualMoneySum").gotValue();//总收款额(元)
        var squareMoneySum = view.getCmp("prmFinalProjectInfoDto->squareMoneySum").gotValue();//已结算运行额(元)
        var plannedCost = view.getCmp("prmFinalProjectInfoDto->plannedCost").gotValue();//计划成本(元)
        var totalSquareMoney = view.getCmp("prmFinalProjectInfoDto->totalSquareMoney").gotValue();//计划运行额(元)
        var squareCostSum = view.getCmp("prmFinalProjectInfoDto->squareCostSum").gotValue();//已结算成本(元)
        var lockedBudgetSum = view.getCmp("prmFinalProjectInfoDto->lockedBudgetSum").gotValue();//总发生成本(合同)
        var billedCostSum = view.getCmp("prmFinalProjectInfoDto->billedCostSum").gotValue();//总发生成本(收票)
        if (Erp.MathUtil.multiNumber(projectActualMoneySum, plannedCost) > Erp.MathUtil.multiNumber(lockedBudgetSum, totalSquareMoney)) {
            return Erp.MathUtil.minusNumber(Erp.MathUtil.divideNumber(Erp.MathUtil.multiNumber(lockedBudgetSum, totalSquareMoney, 4), plannedCost), squareMoneySum);
        } else {
            return Erp.MathUtil.minusNumber(projectActualMoneySum, squareMoneySum);
        }
    },
    getMinSquareMoney: function () {
        var me = this;
        var view = me.view;
        var projectActualMoneySum = view.getCmp("prmFinalProjectInfoDto->projectActualMoneySum").gotValue();//总收款额(元)
        var squareMoneySum = view.getCmp("prmFinalProjectInfoDto->squareMoneySum").gotValue();//已结算运行额(元)
        var plannedCost = view.getCmp("prmFinalProjectInfoDto->plannedCost").gotValue();//计划成本(元)
        var totalSquareMoney = view.getCmp("prmFinalProjectInfoDto->totalSquareMoney").gotValue();//计划运行额(元)
        var squareCostSum = view.getCmp("prmFinalProjectInfoDto->squareCostSum").gotValue();//已结算成本(元)
        var lockedBudgetSum = view.getCmp("prmFinalProjectInfoDto->lockedBudgetSum").gotValue();//总发生成本(合同)
        var billedCostSum = view.getCmp("prmFinalProjectInfoDto->billedCostSum").gotValue();//总发生成本(收票)
        if (Erp.MathUtil.compare(projectActualMoneySum, lockedBudgetSum) > 0) {
            return Erp.MathUtil.max(0, Erp.MathUtil.minusNumber(lockedBudgetSum, squareCostSum));
        } else {
            return Erp.MathUtil.minusNumber(projectActualMoneySum, squareMoneySum);
        }
    },
    doBtnProjectReport: function () {
        var me = this;
        var view = me.view;
        var actionParams = me.actionParams;
        var uuid = view.getCmp("prmFinalEstimateDto->prmProjectMainId").gotValue();
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
    doBtnModifyTaxCorrection: function () {
        var me = this;
        var taxCorrectionCmp = me.view.getCmp("prmFinalEstimateDto->taxCorrection");
        Scdp.MsgUtil.prompt('修正税金', function (buttonId, text) {
            if (buttonId != 'ok') return;
            if (isNaN(text)) {
                Scdp.MsgUtil.warn("请输入有效的数字!");
                return;
            }
            var amount = Erp.MathUtil.multiNumber(Number(text), 1);
            var postData = {};
            var uuid = me.view.getCmp("prmFinalEstimateDto->uuid").gotValue();
            postData.uuid = uuid;
            postData.taxCorrection = amount;
            Scdp.doAction("final-estimate-update-tax-correction", postData, function (result) {
                me.loadItem(uuid);
            });
        }, null, null, taxCorrectionCmp.gotValue());
    },
    doBtnModifyCost: function () {
        var me = this;
        var costCorrectionCmp = me.view.getCmp("prmFinalProjectInfoDto->costCorrection");
        var squareGrossProfitCmp = me.view.getCmp("prmFinalEstimateDto->squareGrossProfit");
        var costCorrectionOrig = costCorrectionCmp.gotValue() == null ? 0 : costCorrectionCmp.gotValue();
        var squareGrossProfit = squareGrossProfitCmp.gotValue() == null ? 0 : squareGrossProfitCmp.gotValue();
        Scdp.MsgUtil.prompt('修正成本', function (buttonId, text) {
            if (buttonId != 'ok') return;
            if (isNaN(text)) {
                Scdp.MsgUtil.warn("请输入有效的数字!");
                return;
            }
            var amount = Erp.MathUtil.multiNumber(Number(text), 1);
            var postData = {};
            var uuid = me.view.getCmp("prmFinalEstimateDto->uuid").gotValue();
            postData.uuid = uuid;
            postData.costCorrection = amount;
            postData.costCorrectionOrig = costCorrectionOrig;
            postData.squareGrossProfit = squareGrossProfit;
            Scdp.doAction("final-estimate-update-cost-correction", postData, function (result) {
                me.loadItem(uuid);
            });
        }, null, null, costCorrectionCmp.gotValue());
    },
    changeSquareGrossProfit: function (obj) {
        var me = this;
        var view = me.view;
        if (obj.isEditable() && obj.isChanged()) {
            var oldValue = obj.oldValue;
            var newValue = obj.gotValue();
            if (Scdp.ObjUtil.isEmpty(newValue)) {
                obj.sotValue(oldValue);
                Scdp.MsgUtil.warn("请输入合法的利润数值！");
                return;
            }
            var squareProjectMoney = view.getCmp("prmFinalEstimateDto->squareProjectMoney").gotValue();
            var maxProfitSum = Erp.MathUtil.minusNumber(squareProjectMoney, me.getSquareCost());
            if (maxProfitSum < newValue) {
                Scdp.MsgUtil.warn("可结算利润不能超过" + maxProfitSum + "元！");
                obj.sotValue(oldValue);
                return;
            }
            view.getCmp("prmFinalEstimateDto->squareCost").sotValue(Erp.MathUtil.minusNumber(squareProjectMoney, newValue));
        }
    },
    loadWorkFlowProcessDeptCode: function () {
        var me = this;
        var processDeptCode = me.view.getCmp('prmFinalEstimateDto->departmentCode').gotValue();
        return processDeptCode;
    },
    fnExamDate: function () {
        var me = this;
        var uuid = me.view.getCmp("prmFinalEstimateDto->uuid").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(uuid)) {
            var controller = Ext.create("PrmExamdateHistory.controller.PrmExamDateWinController");
            var callback = function (subView) {
                var form = subView.getCmp('PrmExamDateHistoryDto');
                if (!form.validate()) {
                    return false;
                }
                var postData = {
                    'viewdata': {'prmExamDateHistoryDto': subView.getCmp('PrmExamDateHistoryDto').gotValue()},
                    'dtoClass': controller.dtoClass
                };
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
            childForm.getCmp('tableName').sotValue('com.csnt.scdp.bizmodules.entity.prm.PrmFinalEstimate');
            childForm.getCmp('dataUuid').sotValue(uuid)
            if (arguments && arguments[0].cid == "btnExamDate") {
                childForm.getCmp('columnName').sotValue('examDate');
                var examDate = me.view.getCmp("prmFinalEstimateDto->examDate").gotValue();
                childForm.getCmp('oldDate').sotValue(examDate);
            } else {
                childForm.getCmp('columnName').sotValue('examRTaxDate');
                var examRTaxDate = me.view.getCmp("prmFinalEstimateDto->examRTaxDate").gotValue();
                childForm.getCmp('oldDate').sotValue(examRTaxDate);
            }
        }
    }
});