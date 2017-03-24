Ext.define("Cashreq.controller.CashreqProjectController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Cashreq.view.CashreqProjectView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'editToolbar->trash', name: 'click', fn: 'trashClick'},
        {cid: 'editToolbar->certificate', name: 'click', fn: 'certificateClick'},
        {cid: 'editToolbar->toCertificate', name: 'click', fn: 'toCertificate'},
        {cid: 'subjectName', name: 'blur', fn: 'doBlur'},
        {cid: 'fadCashReqDto->payStyle', name: 'change', fn: 'payStyleChange'},
        {cid: 'fadCashReqDto->budgetDesc', name: 'change', fn: 'budgetDescChange'},
        {cid: 'fadCashReqDto->projectId', name: 'change', fn: 'projectIdChange'},
        {cid: 'fileUpload', name: 'click', fn: 'fileUploadBtn'},
        {cid: 'fileDownload', name: 'click', fn: 'fileDownloadBtn'},
        {cid: 'fileDelete', name: 'click', fn: 'fileDeleteBtn'},
        {cid: 'expenseRequest', name: 'click', fn: 'doPrintExpenseRequest'},
        {cid: 'meetingPrint', name: 'click', fn: 'doPrintMeeting'},
        {cid: 'meetingStartTime', name: 'change', fn: 'setMeetingDaysValue'},
        {cid: 'meetingEndTime', name: 'change', fn: 'setMeetingDaysValue'},
        //M3_C6_F2_批量打印
        {cid: 'batchExpenseRequest', name: 'click', fn: 'doPrintBatchExpenseRequest'},
        {cid: 'meetingEndTime', name: 'change', fn: 'setMeetingDaysValue'},
        {cid: 'electricCommercialStore', name: 'blur', fn: 'setUserAndPwd'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.fad.cashreq.dto.FadCashReqDto',
    queryAction: 'cashreq-query',
    loadAction: 'cashreq-load',
    addAction: 'cashreq-add',
    modifyAction: 'cashreq-modify',
    deleteAction: 'cashreq-delete',
    exportXlsAction: "cashreq-exportxls",
    setUserAndPwd: function () {
        //M3_C6_F2_商城信息优化
        var me = this;
        var data = {};
        var password = me.view.getCmp("jdPassword").gotValue();
        var passwordDesc = me.view.getCmp("jdPasswordDesc");
        var deptCode = Erp.Util.getCurrentDeptCode();
        if (Scdp.ObjUtil.isNotEmpty(password)) {
            if (deptCode == "CSNT_JCB") {
                passwordDesc.sotValue(password);
            } else {
                passwordDesc.sotValue("******");
            }
        }
    },
    afterInit: function () {
        var me = this;
        me.callParent(arguments);
        me.view.getCmp("fileUpload").setDisabled(true);
        me.view.getCmp("fileDelete").setDisabled(true);
        me.view.getCmp("meetingInfo").setVisible(false);
        me.view.getCmp("travelInfo").setVisible(false);
        //M3_C6_F1_会议费审批
        var meetingStartTime = me.view.getCmp("meetingStartTime");
        var meetingEndTime = me.view.getCmp("meetingEndTime");
        var meetingDays = me.view.getCmp("meetingDays");
        var meetingLocation = me.view.getCmp("meetingLocation");
        var meetingInPersonNum = me.view.getCmp("meetingInPersonNum");
        var meetingOutPersonNum = me.view.getCmp("meetingOutPersonNum");
        var meetingSubject = me.view.getCmp("meetingSubject");
        meetingStartTime.labelEl.dom.innerHTML = meetingStartTime.labelEl.dom.innerHTML + '<div style="color:red;font-size: 22px;display:inline;margin-left: 0px">*</div>';
        meetingEndTime.labelEl.dom.innerHTML = meetingEndTime.labelEl.dom.innerHTML + '<div style="color:red;font-size: 22px;display:inline;margin-left: 0px">*</div>';
        meetingDays.labelEl.dom.innerHTML = meetingDays.labelEl.dom.innerHTML + '<div style="color:red;font-size: 22px;display:inline;margin-left: 0px">*</div>';
        meetingLocation.labelEl.dom.innerHTML = meetingLocation.labelEl.dom.innerHTML + '<div style="color:red;font-size: 22px;display:inline;margin-left: 0px">*</div>';
        meetingInPersonNum.labelEl.dom.innerHTML = meetingInPersonNum.labelEl.dom.innerHTML + '<div style="color:red;font-size: 22px;display:inline;margin-left: 0px">*</div>';
        meetingOutPersonNum.labelEl.dom.innerHTML = meetingOutPersonNum.labelEl.dom.innerHTML + '<div style="color:red;font-size: 22px;display:inline;margin-left: 0px">*</div>';
        meetingSubject.labelEl.dom.innerHTML = meetingSubject.labelEl.dom.innerHTML + '<div style="color:red;font-size: 22px;display:inline;margin-left: 0px">*</div>';
        //M3_C6_F3_逾期标识 - 1
        var tripDays = me.view.getCmp("tripDays");
        var tripMemberNum = me.view.getCmp("tripMemberNum");
        var tripReason = me.view.getCmp("tripReason");
        tripDays.labelEl.dom.innerHTML = tripDays.labelEl.dom.innerHTML + '<div style="color:red;font-size: 22px;display:inline;margin-left: 0px">*</div>';
        tripMemberNum.labelEl.dom.innerHTML = tripMemberNum.labelEl.dom.innerHTML + '<div style="color:red;font-size: 22px;display:inline;margin-left: 0px">*</div>';
        tripReason.labelEl.dom.innerHTML = tripReason.labelEl.dom.innerHTML + '<div style="color:red;font-size: 22px;display:inline;margin-left: 0px">*</div>';

    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("fadCashReqDto->state").sotValue("0");
        view.getCmp("fadCashReqDto->reqType").sotValue("1");
        view.getCmp("fadCashReqDto->isProject").sotValue(1);
        view.getCmp("travelInfo").setVisible(false);
        view.getCmp("meetingInfo").setVisible(false);
        view.getCmp("fadCashReqDto->applyDate").sotValue(Ext.Date.format(new Date(), 'Y-m-d'));
        var preclearDate = Ext.Date.add(new Date(), Ext.Date.MONTH, 6)
        if (preclearDate > Scdp.DateUtil.newDate(new Date().getFullYear(), '12', '31')) {
            preclearDate = Scdp.DateUtil.newDate(new Date().getFullYear(), '12', '31');
        }
        view.getCmp("fadCashReqDto->preclearDate").sotValue(preclearDate);

        view.getCmp('fadCashReqDto').down('JDisplay').setValue("");

        this.fillApplier();
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("fadCashReqDto->runningNo").sotValue("");
        view.getCmp("fadCashReqDto->state").sotValue("0");
        view.getCmp("fadCashReqDto->reqType").sotValue("1");
        view.getCmp("fadCashReqDto->isProject").sotValue(1);
        view.getCmp("fadCashReqDto->applyDate").sotValue(new Date());
        var preclearDate = Ext.Date.add(new Date(), Ext.Date.MONTH, 6)
        if (preclearDate > Scdp.DateUtil.newDate(new Date().getFullYear(), '12', '31')) {
            preclearDate = Scdp.DateUtil.newDate(new Date().getFullYear(), '12', '31');
        }
        view.getCmp("fadCashReqDto->preclearDate").sotValue(preclearDate);
        me.dealWithTravelInfo();
        view.getCmp('fadCashReqDto').down('JDisplay').setValue("");
        this.fillApplier();
    },
    beforeModify: function () {
        var me = this;
        return true;
    },
    afterModify: function () {
        var me = this;
        var view = me.view;
        view.getCmp("fadCashReqDto->staffId").setReadOnly(true);
        //密码只有财务可看
        var password = view.getCmp("fadCashReqDto->jdPassword").gotValue();
        var passwordDesc = me.view.getCmp("jdPasswordDesc");
        var deptCode = Erp.Util.getCurrentDeptCode();
        if (Scdp.ObjUtil.isNotEmpty(password)) {
            if (deptCode == "CSNT_JCB") {
                passwordDesc.sotValue(password);
            } else {
                passwordDesc.sotValue("******");
            }
        }
    },
    afterCancel: function () {
        var me = this;
        me.callParent(arguments);
    },
    beforeWorkFlowCommit: function () {
        var me = this;
        var view = me.view;
        var subjectName = view.getCmp("fadCashReqDto->subjectName").gotValue();
        if (!me.validateMeetingBudget()) {
            return false;
        }
        return true;
    },
    beforeSave: function () {
        var me = this;
        var view = me.view;
        var orderDate = view.getCmp("fadCashReqDto->jdOrderDate").gotValue();
        var lastDate = view.getCmp("fadCashReqDto->jdLastDate").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(orderDate) && Scdp.ObjUtil.isNotEmpty(lastDate)) {
            if (orderDate > lastDate) {
                Scdp.MsgUtil.warn("保存失败，最后下单时间不能早于最后支付时间！");
                return false
            }
        }
        //M3_C6_F1_会议费审批
        var mettingInfo = view.getCmp("meetingInfo");
        if (mettingInfo.isVisible()) {
            var meetingStartTime = view.getCmp("meetingStartTime").gotValue();
            var meetingEndTime = view.getCmp("meetingEndTime").gotValue();
            var meetingDays = view.getCmp("meetingDays").gotValue();
            var meetingLocation = view.getCmp("meetingLocation").gotValue().toString().replace(/(^\s*)|(\s*$)/g, "");
            var meetingInPersonNum = view.getCmp("meetingInPersonNum").gotValue();
            var meetingOutPersonNum = view.getCmp("meetingOutPersonNum").gotValue();
            var meetingSubject = view.getCmp("meetingSubject").gotValue().toString().replace(/(^\s*)|(\s*$)/g, "");
            if (Scdp.ObjUtil.isEmpty(meetingStartTime) || Scdp.ObjUtil.isEmpty(meetingEndTime) || Scdp.ObjUtil.isEmpty(meetingDays)
                || Scdp.ObjUtil.isEmpty(meetingLocation) || Scdp.ObjUtil.isEmpty(meetingInPersonNum) || Scdp.ObjUtil.isEmpty(meetingOutPersonNum)
                || Scdp.ObjUtil.isEmpty(meetingSubject)) {
                Scdp.MsgUtil.warn("保存失败，会务信息全部为必填项！");
                return false
            }
        }

        var applyDate = me.view.getCmp("fadCashReqDto->applyDate").gotValue();
        var squareDate = me.view.getCmp("fadCashReqDto->squareDate").gotValue();
        var preclearDate = me.view.getCmp("fadCashReqDto->preclearDate").gotValue();

        ///M3_C6_F3_逾期标识
        var subjectName = view.getCmp("fadCashReqDto->subjectName").gotValue();
        if (subjectName == "项目设计验收会务、设备厂验费") {
            var d1 = new Date(applyDate);
            var d2 = new Date(preclearDate);
            d1.setMonth(d1.getMonth() + 3);
            if(d2) {
                if(d2 > d1) {
                    Scdp.MsgUtil.warn("费用内容为‘项目设计验收会务、设备厂验费’，预计核销时间与请款时间不能超过 3 个月！");
                    return false;
                }
            }
        }

        if (Scdp.ObjUtil.isNotEmpty(applyDate) && Scdp.ObjUtil.isNotEmpty(squareDate)) {
            if (applyDate > squareDate) {
                Scdp.MsgUtil.warn("保存失败！<br\>预计结算时间不能早于请款时间!");
                return false;
            }
        }
        if (Scdp.ObjUtil.isNotEmpty(squareDate) && Scdp.ObjUtil.isNotEmpty(applyDate)) {
            if (squareDate > preclearDate) {
                Scdp.MsgUtil.warn("保存失败！<br\>预计核销时间不能早于预计结算时间!");
                return false;
            }
        }
        if (Scdp.ObjUtil.isNotEmpty(preclearDate)) {
            if (preclearDate > Scdp.DateUtil.newDate(new Date().getFullYear(), '12', '31')) {
                Scdp.MsgUtil.warn("保存失败！<br\>预计核销时间不能晚于今年!");
                return false;
            }
        }
        if (Scdp.ObjUtil.isNotEmpty(applyDate) && Scdp.ObjUtil.isNotEmpty(preclearDate)) {
            if (preclearDate > Ext.Date.add(applyDate, Ext.Date.MONTH, 6)) {
                Scdp.MsgUtil.warn("保存失败！<br\>预计核销时间不能晚于请款后半年!");
                return false;
            }
        }
        // M7_C2_F2_新增优化
        var payStyle = me.view.getCmp("fadCashReqDto->payStyle").gotValue();
        if (payStyle != "0" && payStyle != "2") {
            var payeeName = me.view.getCmp("fadCashReqDto->payeeName").gotValue();
            var payeeBankName = me.view.getCmp("fadCashReqDto->payeeBankName").gotValue();
            var payeeAccount = me.view.getCmp("fadCashReqDto->payeeAccount").gotValue();

            if (Scdp.ObjUtil.isEmpty(payeeName) || Scdp.ObjUtil.isEmpty(payeeBankName) || Scdp.ObjUtil.isEmpty(payeeAccount)) {
                Scdp.MsgUtil.warn("当前申请非个人收款，请输入收款单位名称、收款开户银行、收款单位账号!");
                return false;
            }
        }

        var meetingStartTime = view.getCmp("fadCashReqDto->meetingStartTime").gotValue();
        var meetingEndTime = view.getCmp("fadCashReqDto->meetingEndTime").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(meetingStartTime) && Scdp.ObjUtil.isNotEmpty(meetingEndTime)) {
            if (meetingEndTime < meetingStartTime) {
                Scdp.MsgUtil.warn("会务开始时间不能晚于会务结束时间！");
                return false;
            }
        }
        if (!me.validateMeetingBudget()) {
            return false;
        }

        var subjectName = view.getCmp("fadCashReqDto->subjectName").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(subjectName)) {
            if (subjectName.indexOf("差旅费") >= 0) {
                var tripDays = view.getCmp("fadCashReqDto->tripDays").gotValue();
                var tripMemberNum = view.getCmp("fadCashReqDto->tripMemberNum").gotValue();
                var tripReason = view.getCmp("fadCashReqDto->tripReason").gotValue().toString().replace(/(^\s*)|(\s*$)/g, "");
                if (Scdp.ObjUtil.isEmpty(tripDays) || Scdp.ObjUtil.isEmpty(tripMemberNum) || Scdp.ObjUtil.isEmpty(tripReason)) {
                    Scdp.MsgUtil.warn("出差费用申请，请填写“出差天数、出差人数、出差事由”！");
                    return false;
                }
            }
        }

        //保存前判断密码框是否被修改
        var passwordCid = me.view.getCmp("jdPassword");
        var passwordDesc = me.view.getCmp("jdPasswordDesc").gotValue();
        if (passwordDesc != "******") {
            passwordCid.sotValue(passwordDesc);
        }
        return true;
    },

    validateMeetingBudget: function () {
        var me = this;
        var view = me.view;
        var subjectName = view.getCmp("fadCashReqDto->subjectName").gotValue();
        if (subjectName == "项目设计验收会务、设备厂验费") {
            var cashReqBudget = view.getCmp("fadCashReqBudgetDto").store.data.items;
            if (cashReqBudget.length == 0) {
                Scdp.MsgUtil.warn("费用内容为‘项目设计验收会务、设备厂验费’，必须维护预算信息！");
                return false;
            }
        }
        return true;
    },
    afterSave: function (result) {
        var me = this;
        me.callParent(arguments);
        var view = me.view;
        if (Scdp.ObjUtil.isNotEmpty(result.runningNo)) {
            view.getCmp("fadCashReqDto->runningNo").sotValue(result.runningNo);
        }
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        me.callParent(arguments);
        var view = me.view;
        view.getCmp("editPanel->trash").setDisabled(true);
        view.getCmp("editPanel->certificate").setDisabled(true);
        view.getCmp("editPanel->toCertificate").setDisabled(true);
        if (view.getCmp("fadCashReqDto->state").gotValue() == "2") {
            view.getCmp("editPanel->trash").setDisabled(false);
            view.getCmp("editPanel->certificate").setDisabled(false);

            view.getCmp('editPanel->workFlowCommitBtn').setVisible(false);
            view.getCmp('editPanel->workFlowAssignBtn').setVisible(false);
            view.getCmp('editPanel->workFlowRollBackBtn').setVisible(false);
        }
        else if (view.getCmp("fadCashReqDto->state").gotValue() == "4" || view.getCmp("fadCashReqDto->state").gotValue() == "8") {
            view.getCmp("editPanel->toCertificate").setDisabled(false);
        }
        me.dealWithTravelInfo();
        view.getCmp("cashReqTab").setActiveTab(0);

        var display = view.getCmp("fadCashReqDto->budgetDesc").gotValue()

        view.getCmp('fadCashReqDto').down('JDisplay').setValue(display);
        //密码只有财务可看
        var password = view.getCmp("fadCashReqDto->jdPassword").gotValue();
        var passwordDesc = me.view.getCmp("jdPasswordDesc");
        var deptCode = Erp.Util.getCurrentDeptCode();
        if (Scdp.ObjUtil.isNotEmpty(password)) {
            if (deptCode == "CSNT_JCB") {
                passwordDesc.sotValue(password);
            } else {
                passwordDesc.sotValue("******");
            }
        }
    },

    budgetDescChange: function (a, b) {
        var me = this;
        var view = me.view;
        view.getCmp('fadCashReqDto').down('JDisplay').setValue("当前该科目可用预算为：" + b + "元");
    },

    projectIdChange: function (a, b) {
        var me = this;
        var view = me.view;
        if (Scdp.ObjUtil.isNotEmpty(view.getCmp('fadCashReqDto->subjectName').gotValue())) {
            view.getCmp('fadCashReqDto->subjectName').setValue(null);
            view.getCmp('fadCashReqDto->budgetCUuid').setValue(null);
            view.getCmp('fadCashReqDto->subjectCode').setValue(null);
            view.getCmp('fadCashReqDto->budgetDesc').setValue(null);
        }
    },
    beforeDelete: function () {
        var me = this;
        var state = me.view.getCmp("fadCashReqDto->state").gotValue();
        if (state != '0' && state != '5') {
            Scdp.MsgUtil.warn("当前申请非新增状态，无法删除！");
            return false;
        }
        return true;
    },
    afterDelete: function () {
        var me = this;
        var view = me.view;
    },
    beforeBatchDel: function () {
        var me = this;
        var view = me.view;
        var resNos = "";
        var selectedSubject = me.view.getResultPanel().getSelectionModel().getSelection();
        Ext.Array.each(selectedSubject, function (a) {
                if (a.data.state != '0' && a.data.state != '5') {
                    resNos = resNos + a.data.runningNo + ",";
                }
            }
        );
        if (resNos != "") {
            Scdp.MsgUtil.warn("账单 " + resNos + " 未处于新增状态，无法删除！<br/>请取消后执行删除！");
            return false;
        }
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
    doBlur: function (a, b) {
        var me = this;
        me.dealWithTravelInfo();
    },
    dealWithTravelInfo: function () {
        var me = this;
        var view = me.view;
        view.getCmp("meetingInfo").setVisible(false);
        view.getCmp("travelInfo").setVisible(false);
        var subjectName = view.getCmp("fadCashReqDto->subjectName").gotValue();
        var subjectCode = view.getCmp("fadCashReqDto->subjectCode").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(subjectName)) {
            if (subjectName.indexOf("差旅费") >= 0) {
                view.getCmp("travelInfo").setVisible(true);
                view.getCmp("fadCashReqDto->reqType").sotValue("2");
            } else {
                view.getCmp("travelInfo").setVisible(false);
                Ext.Array.each(view.getCmp("fadCashReqDto->travelInfo").items.items, function (a) {
                    a.sotValue("");
                });
                view.getCmp("fadCashReqDto->reqType").sotValue("1");
            }
            var budgetMeetingTab = view.getCmp("cashReqTab");
            if (subjectCode == 'MEETING_AFFAIRS') {
                view.getCmp("meetingInfo").setVisible(true);
                view.getCmp("editPanel->editToolbar->meetingPrint").setVisible(true);
                budgetMeetingTab.tabBar.items.items[2].show();
            } else {
                view.getCmp("meetingInfo").setVisible(false);
                Ext.Array.each(view.getCmp("fadCashReqDto->meetingInfo").items.items, function (a) {
                    a.sotValue("");
                });
                view.getCmp("editPanel->editToolbar->meetingPrint").setVisible(false);
                budgetMeetingTab.tabBar.items.items[2].hide();
            }
        }
    },
    // M7_C2_F2_新增优化
    payStyleChange: function (a, b) {
        var view = this.view;
        if (b == "0" || b == "2") {
            view.getCmp("fadCashReqDto->payeeName").sotValue('');
            view.getCmp("fadCashReqDto->payeeBankName").sotValue('');
            view.getCmp("fadCashReqDto->payeeAccount").sotValue('');
        }
        var payeeNameC = view.getCmp("fadCashReqDto->payeeName");
        var payeeBankNameC = view.getCmp("fadCashReqDto->payeeBankName");
        var payeeAccountC = view.getCmp("fadCashReqDto->payeeAccount");
        if (b == "0" || b == "2") {
            payeeNameC.allowBlank = true;
            payeeBankNameC.allowBlank = true;
            payeeAccountC.allowBlank = true;
            payeeNameC.labelEl.dom.innerHTML = payeeNameC.labelEl.dom.innerHTML.replace('*', '');
            payeeBankNameC.labelEl.dom.innerHTML = payeeBankNameC.labelEl.dom.innerHTML.replace('*', '');
            payeeAccountC.labelEl.dom.innerHTML = payeeAccountC.labelEl.dom.innerHTML.replace('*', '');
        } else {
            payeeNameC.allowBlank = false;
            payeeBankNameC.allowBlank = false;
            payeeAccountC.allowBlank = false;
            payeeNameC.labelEl.dom.innerHTML = payeeNameC.labelEl.dom.innerHTML.replace('*', '');
            payeeBankNameC.labelEl.dom.innerHTML = payeeBankNameC.labelEl.dom.innerHTML.replace('*', '');
            payeeAccountC.labelEl.dom.innerHTML = payeeAccountC.labelEl.dom.innerHTML.replace('*', '');
            payeeNameC.labelEl.dom.innerHTML = payeeNameC.labelEl.dom.innerHTML + '<div style="color:red;font-size: 15px;display:inline;margin-left: 0px">*</div>';
            payeeBankNameC.labelEl.dom.innerHTML = payeeBankNameC.labelEl.dom.innerHTML + '<div style="color:red;font-size: 15px;display:inline;margin-left: 0px">*</div>';
            payeeAccountC.labelEl.dom.innerHTML = payeeAccountC.labelEl.dom.innerHTML + '<div style="color:red;font-size: 15px;display:inline;margin-left: 0px">*</div>';
        }
    },

    doSubmit: function (obj) {
        var me = this;
        var view = this.view;
        var state = view.getCmp("fadCashReqDto->state").gotValue();
        if (state != '0') {
            Scdp.MsgUtil.warn("当前申请非新增状态，无法提交！");
        } else {
            var uuid = view.getCmp("fadCashReqDto->uuid").gotValue();
            var actionType = "submit";
            var param = {actionType: actionType, uuid: uuid};
            Scdp.doAction("cashreq-work", param, function (result) {
                if (Scdp.ObjUtil.isNotEmpty(result.result) && result.result == true) {
                    me.loadItem(uuid);
                    Scdp.MsgUtil.info("提交成功");
                }
                else {
                    Scdp.MsgUtil.warn("费用预算不足，请确认！");
                }
            });
        }
    },
    trashClick: function (obj) {
        var me = this;
        var view = this.view;
        var state = view.getCmp("fadCashReqDto->state").gotValue();
        if (state != '2') {
            Scdp.MsgUtil.warn("只有审批通过的单据才能作废！");
        } else {
            Scdp.MsgUtil.confirm("是否确认作废？", function (e) {
                if (e == "yes") {
                    var uuid = view.getCmp("fadCashReqDto->uuid").gotValue();
                    var actionType = "trash";
                    var param = {actionType: actionType, uuid: uuid};
                    Scdp.doAction("cashreq-work", param, function (result) {
                        if (Scdp.ObjUtil.isNotEmpty(result.result) && result.result == true) {
                            me.loadItem(uuid);
                            Scdp.MsgUtil.info("操作成功！");
                        }
                        else {
                            Scdp.MsgUtil.warn("操作失败！");
                        }
                    });
                }
            });
        }
    },
    certificateClick: function (obj) {
        var me = this;
        var view = me.view;
        var state = view.getCmp("fadCashReqDto->state").gotValue();
        if (state != "2") {
            Scdp.MsgUtil.warn("只有审批通过的单据才能生成凭证！");
        } else {
            var uuid = view.getCmp("fadCashReqDto->uuid").gotValue();
            var param = {uuid: uuid};
            Scdp.doAction("certificate-create", param, function (result) {
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
        var uuid = view.getCmp('fadCashReqDto->uuid').gotValue();
        if (Scdp.ObjUtil.isEmpty(uuid)) {
            Scdp.MsgUtil.info("未选择数据");
            return;
        }
        var postData = {uuid: uuid};
        var actionResult = Scdp.doAction("certificate-getFadCertificateUuid", postData, null, null, true, false);
        Erp.Util.gotoCertificateModule(actionResult.fadCertificateUuid);
    },
    //填充申请人
    fillApplier: function () {
        this.view.getCmp("fadCashReqDto->staffId").putValue(Scdp.getCurrentUserId());
        this.view.getCmp("fadCashReqDto->officeId").putValue(Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE));
        this.view.getCmp("fadCashReqDto->operatorId").sotValue(Scdp.getCurrentUserId());
        this.view.getCmp("fadCashReqDto->operatorName").sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_NAME));
        this.view.getCmp("fadCashReqDto->operateTime").sotValue(Ext.Date.format(new Date(), 'Y-m-d'));
    },

    //文件上传
    fileUploadBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        var fileClassify = "";
        Erp.FileUtil.erpFileUpload(grid, fileClassify);
    },
    //文件下载
    fileDownloadBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        Erp.FileUtil.erpFileDownLoad(grid);
    },
    //文件删除
    fileDeleteBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        Erp.FileUtil.erpFileDelete(grid);
    },
    //根据回传的数据，在页面上显示相关信息
//    initFileUploadData: function (fileGrid, obj) {
//        if (obj != null) {
//            obj.fileId = obj.uuid;
//            obj.module = Scdp.getActiveModule().controller.menuCode;
//            obj.cdmFileType = "";
//            delete obj["uuid"];
//            fileGrid.addRowItem(obj, false);
//        }
//    },
    //M3_C6_F2_批量打印
    doPrintExpenseRequest: function () {
        var me = this;
        var view = me.view;
        var uuid = view.getCmp("fadCashReqDto->uuid").gotValue();
        var reqType = view.getCmp("fadCashReqDto->reqType").gotValue();
        var param = {
            uuid: uuid
        }
        if (reqType == 2) {
            Scdp.printReport("erp/prm/AccountStatementTrip.cpt", [param], false, "pdf");
        } else {
            Scdp.printReport("erp/prm/AccountStatementOthers.cpt", [param], false, "pdf");
        }
    },
    //M3_C6_F2_批量打印
    doPrintBatchExpenseRequest: function () {
        var me = this;
        var view = me.view;
        var uuids = "";
        var reqType = "";
        var grid = view.getQueryPanel().getCmp("resultPanel");
        var selectedRecords = grid.getSelectionModel().getSelection();
        if (selectedRecords.length > 0) {
            reqType = selectedRecords[0].data.reqType;
            var flag = false;
            for (var i = 0; i < selectedRecords.length; i++) {
                uuids += selectedRecords[i].data.uuid + ",";
                if (reqType == "2") {
                    if (reqType != selectedRecords[i].data.reqType) {
                        flag = true;
                    }
                } else {
                    if ("2" == selectedRecords[i].data.reqType) {
                        flag = true;
                    }
                }
            }
            if (flag) {
                Scdp.MsgUtil.warn("选中的打印数据中的类型不相同");
                return;
            }
            var param = {
                uuid : uuids.substr(0, uuids.length - 1)
            }
            if (reqType == 2) {
                Scdp.printReport("erp/prm/AccountStatementTrip.cpt", [param], false, "pdf");
            } else {
                Scdp.printReport("erp/prm/AccountStatementOthers.cpt", [param], false, "pdf");
            }
        }
    },
    setMeetingDaysValue: function (obj) {
        var me = this;
        var view = me.view;
        var meetingStartTime = view.getCmp("fadCashReqDto->meetingStartTime").gotValue();
        var meetingEndTime = view.getCmp("fadCashReqDto->meetingEndTime").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(meetingStartTime) && Scdp.ObjUtil.isNotEmpty(meetingEndTime)) {
            if (meetingEndTime < meetingStartTime) {
                Scdp.MsgUtil.warn("会务开始时间不能晚于会务结束时间！");
                return;
            }
            var days = (meetingEndTime - meetingStartTime) / 86400000 + 1
            view.getCmp("fadCashReqDto->meetingDays").sotValue(days);
        }
    },

    doPrintMeeting: function () {
        var me = this;
        var view = me.view;
        var runningNo = view.getCmp("fadCashReqDto->runningNo").gotValue();
        var subjectCode = view.getCmp("fadCashReqDto->subjectCode").gotValue();

        var param = {runningNo: runningNo};
        if (subjectCode == 'MEETING_AFFAIRS') {
            Scdp.printReport("erp/prm/FadMeetingBudget.cpt", [param], false, "pdf");
        }
    },
    loadWorkFlowProcessDeptCode: function () {
        var me = this;
        var processDeptCode = me.view.getCmp('fadCashReqDto->prmContractorOffice').gotValue();
        return processDeptCode;
    }
});