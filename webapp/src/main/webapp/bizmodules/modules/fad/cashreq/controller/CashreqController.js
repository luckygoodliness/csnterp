Ext.define("Cashreq.controller.CashreqController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Cashreq.view.CashreqView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'editToolbar->trash', name: 'click', fn: 'trashClick'},
        {cid: 'fadCashReqDto->subjectCode', name: 'blur', fn: 'subjectCodeBlur'},
        {cid: 'editToolbar->certificate', name: 'click', fn: 'certificateClick'},
        {cid: 'editToolbar->toCertificate', name: 'click', fn: 'toCertificate'},
        {cid: 'fadCashReqDto->payStyle', name: 'change', fn: 'payStyleChange'},
        {cid: 'fadCashReqDto->electricCommercialStore', name: 'blur', fn: 'electricCommercialStoreBlur'},
        {cid: 'fadCashReqDto->budgetDesc', name: 'change', fn: 'budgetDescChange'},
        {cid: 'fadCashReqDto->payeeAccount', name: 'change', fn: 'payeeAccountChange'},
        {cid: 'expenseRequest', name: 'click', fn: 'doPrintExpenseRequest'},
        {cid: 'expenseElectric', name: 'click', fn: 'doPrintExpenseRequest'},
        //M4_C6_F3_批量打印
        {cid:'batchExpenseRequest',name:'click',fn:'doBatchExpenseRequest'},
        {cid: 'meetingPrint', name: 'click', fn: 'doPrintMeeting'},
        {cid: 'fadCashReqDto->fileUpload', name: 'click', fn: 'doFileUpload'},
        {cid: 'fadCashReqDto->fileDownload', name: 'click', fn: 'doFileDownload'},
        {cid: 'fileDownloadQ', name: 'click', fn: 'doFileDownload'},
        {cid: 'fadCashReqDto->fileDelete', name: 'click', fn: 'dofileDelete'},
        {cid: 'meetingStartTime', name: 'change', fn: 'setMeetingDaysValue'},
        {cid: 'meetingEndTime', name: 'change', fn: 'setMeetingDaysValue'},
        {cid: 'electricCommercialStore', name: 'blur', fn: 'setUserAndPwd'},
        {cid: 'jdName', name: 'change', fn: 'changeOrderState'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.fad.cashreq.dto.FadCashReqDto',
    queryAction: 'cashreq-query',
    loadAction: 'cashreq-load',
    addAction: 'cashreq-add',
    modifyAction: 'cashreq-modify',
    deleteAction: 'cashreq-delete',
    exportXlsAction: "cashreq-exportxls",
    setUserAndPwd : function () {
        //M4_C6_F2_商城信息优化
        var me = this;
        var data = {};
        var password = me.view.getCmp("jdPassword").gotValue();
        var passwordDesc = me.view.getCmp("jdPasswordDesc");
        var deptCode = Erp.Util.getCurrentDeptCode();
        if (deptCode == "CSNT_JCB"){
            passwordDesc.sotValue(password);
        }else{
            passwordDesc.sotValue("******");
        }
    },
    //检查电商是否选择，有选中则给订单号等加上必填标识
    changeOrderState : function() {
        var me = this;
        var view = me.view;
        var electricName = view.getCmp("electricCommercialStore").gotValue();
        var jdOrderNo = view.getCmp("jdOrderNo");
        var jdOrderDate = view.getCmp("jdOrderDate");
        var jdLastDate = view.getCmp("jdLastDate");
        jdOrderNo.labelEl.dom.innerHTML = jdOrderNo.labelEl.dom.innerHTML.replace('*', '');
        jdOrderDate.labelEl.dom.innerHTML = jdOrderDate.labelEl.dom.innerHTML.replace('*', '');
        jdLastDate.labelEl.dom.innerHTML = jdLastDate.labelEl.dom.innerHTML.replace('*', '');
        if (Scdp.ObjUtil.isNotEmpty(electricName)) {
            //加上标识
            jdOrderNo.labelEl.dom.innerHTML = jdOrderNo.labelEl.dom.innerHTML + '<div style="color:red;font-size: 22px;display:inline;margin-left: 0px">*</div>';
            jdOrderDate.labelEl.dom.innerHTML = jdOrderDate.labelEl.dom.innerHTML + '<div style="color:red;font-size: 22px;display:inline;margin-left: 0px">*</div>';
            jdLastDate.labelEl.dom.innerHTML = jdLastDate.labelEl.dom.innerHTML + '<div style="color:red;font-size: 22px;display:inline;margin-left: 0px">*</div>';
        }
    },
    afterInit: function () {
        var me = this;
        var view = me.view;
        me.callParent(arguments);
        view.getCmp("fadCashReqDto->fileUpload").disable();
        view.getCmp("fadCashReqDto->fileDelete").disable();
        view.getCmp("meetingInfo").setVisible(false);
        view.getCmp("travelInfo").setVisible(true);
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
        view.getCmp("fadCashReqDto->isProject").sotValue(0);
        view.getCmp("travelInfo").setVisible(true);
        view.getCmp("meetingInfo").setVisible(false);
        view.getCmp("cashReqTab").tabBar.items.items[2].hide();

        view.getCmp("fadCashReqDto->applyDate").sotValue(new Date());
        var preclearDate = Ext.Date.add(new Date(), Ext.Date.MONTH, 6)
        if (preclearDate > Scdp.DateUtil.newDate(new Date().getFullYear(), '12', '31')) {
            preclearDate = Scdp.DateUtil.newDate(new Date().getFullYear(), '12', '31');
        }
        view.getCmp("fadCashReqDto->preclearDate").sotValue(preclearDate);
        view.getCmp("travelInfo").setDisabled(true)

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

        view.getCmp("fadCashReqDto->applyDate").sotValue(new Date());
        var preclearDate = Ext.Date.add(new Date(), Ext.Date.MONTH, 6)
        if (preclearDate > Scdp.DateUtil.newDate(new Date().getFullYear(), '12', '31')) {
            preclearDate = Scdp.DateUtil.newDate(new Date().getFullYear(), '12', '31');
        }
        view.getCmp("fadCashReqDto->preclearDate").sotValue(preclearDate);
        view.getCmp('fadCashReqDto').down('JDisplay').setValue("");
        this.fillApplier();
        var subjectName = view.getCmp("fadCashReqDto->subjectName").gotValue();
        view.getCmp("travelInfo").setVisible(true);
        view.getCmp("meetingInfo").setVisible(false);
        view.getCmp("cashReqTab").tabBar.items.items[2].hide();
        if (subjectName == "会务费") {
            view.getCmp("meetingInfo").setVisible(true);
            view.getCmp("cashReqTab").tabBar.items.items[2].show();
        }
    },
    beforeModify: function () {
        var me = this;
        var state = me.view.getCmp("fadCashReqDto->state").gotValue();
        return true;
    },
    afterModify: function () {
        var me = this;
        var view = me.view;
        view.getCmp("fadCashReqDto->officeId").setReadOnly(true);
        view.getCmp("fadCashReqDto->staffId").setReadOnly(true);
        view.getCmp("fadCashReqDto->travelInfo").setDisabled(true)

        var subjectName = me.view.getCmp("fadCashReqDto->subjectName").gotValue();
        if (subjectName == "差旅费") {
            me.view.getCmp("travelInfo").setDisabled(false)
        }

        //密码只有财务可看
        var password = view.getCmp("fadCashReqDto->jdPassword").gotValue();
        var passwordDesc = me.view.getCmp("jdPasswordDesc");
        var deptCode = Erp.Util.getCurrentDeptCode();
        if (Scdp.ObjUtil.isNotEmpty(password)){
            if (deptCode == "CSNT_JCB"){
                passwordDesc.sotValue(password);
            }else{
                passwordDesc.sotValue("******");
            }
        }
        me.changeOrderState();

    },
    payeeAccountChange: function (obj) {
        var me = this;
        if (Scdp.ObjUtil.isNotEmpty(obj.displayTplData) && obj.displayTplData[0].codedesc) {
            var bank = me.view.getCmp("fadCashReqDto->payeeBankName");
            bank.sotValue(obj.displayTplData[0].codedesc);
        }
    },

    beforeSave: function () {
        var me = this;
        var view = me.view;
        //校验请求金额不能小于分摊金额总和
        var applyMoney = me.view.getCmp("fadCashReqDto->money").gotValue();
        var sharedMoney = 0;
        var dtoGrid = me.view.getCmp("fadCashReqShareDto");
        for (var i = 0; i < dtoGrid.getStore().getCount(); i++) {
            if (Scdp.ObjUtil.isNotEmpty(dtoGrid.getStore().getAt(i).data.money))
                sharedMoney = sharedMoney + dtoGrid.getStore().getAt(i).data.money;
        }
        if (applyMoney < sharedMoney) {
            Scdp.MsgUtil.warn("保存失败！<br\>分摊金额不能大于请款金额！");
            return false;
        }
        var applyDate = me.view.getCmp("fadCashReqDto->applyDate").gotValue();
        var squareDate = me.view.getCmp("fadCashReqDto->squareDate").gotValue();
        var preclearDate = me.view.getCmp("fadCashReqDto->preclearDate").gotValue();

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

        if (!me.validateMeetingBudget()) {
            return false;
        }

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
        var subjectName = me.view.getCmp("fadCashReqDto->subjectName").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(subjectName)) {
            if (subjectName.indexOf("差旅费") >= 0) {
                var tripDays = view.getCmp("fadCashReqDto->tripDays").gotValue();
                var tripMemberNum = view.getCmp("fadCashReqDto->tripMemberNum").gotValue();
                var tripReason = view.getCmp("fadCashReqDto->tripReason").gotValue();
                if (Scdp.ObjUtil.isEmpty(tripDays) || Scdp.ObjUtil.isEmpty(tripMemberNum) || Scdp.ObjUtil.isEmpty(tripReason)) {
                    Scdp.MsgUtil.warn("出差费用申请，请填写“出差天数、出差人数、出差事由”！");
                    return false;
                }
            }
        }

        var electricCommercialStore = me.view.getCmp("fadCashReqDto->electricCommercialStore").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(electricCommercialStore)) {
            var jdName = view.getCmp("fadCashReqDto->jdName").gotValue();
            if (Scdp.ObjUtil.isEmpty(jdName)) {
                Scdp.MsgUtil.warn("请输完整商城信息！用户名不能为空！");
                return false;
            }
            var jdPassword = view.getCmp("fadCashReqDto->jdPassword").gotValue();
            if (Scdp.ObjUtil.isEmpty(jdPassword)) {
                Scdp.MsgUtil.warn("请输完整商城信息！密码不能为空！");
                return false;
            }

            var jdOrderNo = view.getCmp("fadCashReqDto->jdOrderNo").gotValue();
            if (Scdp.ObjUtil.isEmpty(jdOrderNo)) {
                Scdp.MsgUtil.warn("请输完整商城信息！订单号不能为空！");
                return false;
            }

            var jdOrderDate = view.getCmp("fadCashReqDto->jdOrderDate").gotValue();
            if (Scdp.ObjUtil.isEmpty(jdOrderDate)) {
                Scdp.MsgUtil.warn("请输完整商城信息！下单时间不能为空！");
                return false;
            }

            var jdLastDate = view.getCmp("fadCashReqDto->jdLastDate").gotValue();
            if (Scdp.ObjUtil.isEmpty(jdLastDate)) {
                Scdp.MsgUtil.warn("请输完整商城信息！最后支付时间不能为空！");
                return false;
            }
            if (jdOrderDate > jdLastDate) {
                Scdp.MsgUtil.warn("最后支付时间不能早于下单时间！");
                return false;
            }
        }

        //保存前判断密码框是否被修改
        var passwordCid = me.view.getCmp("jdPassword");
        var passwordDesc = me.view.getCmp("jdPasswordDesc").gotValue();
        if (passwordDesc != "******"){
            passwordCid.sotValue(passwordDesc);
        }
        return true;

        return true;
    },
    validateMeetingBudget: function () {
        var me = this;
        var view = me.view;
        var subjectName = view.getCmp("fadCashReqDto->subjectName").gotValue();
        if (subjectName == "会务费") {
            var cashReqBudget = view.getCmp("fadCashReqBudgetDto").store.data.items;
            if (cashReqBudget.length == 0) {
                Scdp.MsgUtil.warn("会务费，必须维护预算信息！");
                return false;
            }
        }
        return true;
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
    afterSave: function (result) {
        var me = this;
        var view = me.view;
        me.callParent(arguments);
        if (Scdp.ObjUtil.isNotEmpty(result)) {
            if(Scdp.ObjUtil.isNotEmpty(result.uuid))
                me.loadItem(result.uuid);
            else
                me.loadItem(view.getCmp("uuid").gotValue());
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
        view.getCmp("editPanel->trash").setDisabled(true);
        view.getCmp("editPanel->certificate").setDisabled(true);
        view.getCmp("editPanel->toCertificate").setDisabled(true);

        view.getCmp("fadCashReqDto->fileUpload").disable();
        view.getCmp("fadCashReqDto->fileDelete").disable();
        var state = view.getCmp("fadCashReqDto->state").gotValue()
        var operator = view.getCmp("fadCashReqDto->operatorId").gotValue();

        if (state == "0") {
            if (Scdp.getCurrentUserId() == operator) {
                view.getCmp("fadCashReqDto->fileUpload").setDisabled(false);
                view.getCmp("fadCashReqDto->fileDelete").setDisabled(false);
            }
        }
        else if (state == "2") {
            view.getCmp("editPanel->trash").setDisabled(false);
            view.getCmp("editPanel->certificate").setDisabled(false);
        }
        else if (state == "4" || state == "8") {
            view.getCmp("editPanel->toCertificate").setDisabled(false);
        }
        var display = view.getCmp("fadCashReqDto->budgetDesc").gotValue()
        view.getCmp('fadCashReqDto').down('JDisplay').setValue(display);
        var subjectName = view.getCmp("fadCashReqDto->subjectName").gotValue();
        view.getCmp("travelInfo").setVisible(true);
        view.getCmp("meetingInfo").setVisible(false);
        view.getCmp("cashReqTab").tabBar.items.items[2].hide();
        view.getCmp("editPanel->editToolbar->meetingPrint").setVisible(false);
        if (subjectName == "会务费") {
            view.getCmp("meetingInfo").setVisible(true);
            view.getCmp("cashReqTab").tabBar.items.items[2].show();
            view.getCmp("editPanel->editToolbar->meetingPrint").setVisible(true);
            view.getCmp("travelInfo").setVisible(false);
        }

        //密码只有财务可看
        var password = view.getCmp("fadCashReqDto->jdPassword").gotValue();
        var passwordDesc = me.view.getCmp("jdPasswordDesc");
        var deptCode = Erp.Util.getCurrentDeptCode();
        if (Scdp.ObjUtil.isNotEmpty(password)){
            if (deptCode == "CSNT_JCB"){
                passwordDesc.sotValue(password);
            }else{
                passwordDesc.sotValue("******");
            }
        }
        me.changeOrderState();
    },
    budgetDescChange: function (a, b) {
        var me = this;
        var view = me.view;
        view.getCmp('fadCashReqDto').down('JDisplay').setValue("当前该科目可用预算为：" + b + "元");
    },

    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
        me.callParent(arguments);
        var view = me.view;
    },
    beforeDelete: function () {
        var me = this;
        return true;
    },
    afterDelete: function () {
        var me = this;
        var view = me.view;
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

    subjectCodeBlur: function (obj) {
        var me = this;
        var view = me.view;
        view.getCmp("meetingInfo").setVisible(false);
        view.getCmp("travelInfo").setVisible(true);
        view.getCmp("cashReqTab").tabBar.items.items[2].hide();
        view.getCmp("travelInfo").setDisabled(true);

        view.getCmp("editPanel->editToolbar->meetingPrint").setVisible(false);
        var subjectName = view.getCmp("fadCashReqDto->subjectName").gotValue();
        view.getCmp("fadCashReqDto->reqType").sotValue("1");
        if (subjectName == "差旅费") {
            view.getCmp("travelInfo").setDisabled(false)
            view.getCmp("fadCashReqDto->reqType").sotValue("2");
        }
        if (subjectName == "会务费") {
            view.getCmp("cashReqTab").tabBar.items.items[2].show();
            view.getCmp("meetingInfo").setVisible(true);
            view.getCmp("travelInfo").setVisible(false);
            view.getCmp("editPanel->editToolbar->meetingPrint").setVisible(true);
        }
    },

    payStyleChange: function (a, b) {
        var me = this;
        var view = me.view;
        if (b == "0" || b == "2") {
            view.getCmp("fadCashReqDto->payeeName").sotValue('');
            view.getCmp("fadCashReqDto->payeeBankName").sotValue('');
            view.getCmp("fadCashReqDto->payeeAccount").sotValue('');
        }
    },
    electricCommercialStoreBlur: function (a, b) {
        var me = this;
        var view = me.view;
        if (a.lastValue != a.oldValue) {
            view.getCmp("fadCashReqDto->payeeName").putValue(a.lastValue);
            view.getCmp("fadCashReqDto->payStyle").sotValue('1');
        }
    },
    //弹出部门选择窗体
    pickContract: function () {
        var me = this;
        var view = me.view;
        var notInRow = "";
        var dtoGrid = view.getCmp("fadCashReqShareDto");
        for (var i = 0; i < dtoGrid.getStore().getCount(); i++) {
            if (Scdp.ObjUtil.isNotEmpty(dtoGrid.getStore().getAt(i).data.officeId))
                notInRow = notInRow + "'" + dtoGrid.getStore().getAt(i).data.officeId + "',"
        }
        notInRow = notInRow + "'.'";
        var param = {notInRow: notInRow};
        var queryController = Ext.create("Erpoffice.controller.OfficePickController");
        queryController.actionParams = param;
        var callBack = function (subView, isCancel) {
            if (isCancel) {
                return;
            }
            var grid = subView.getQueryPanel().ownerCt.getCmp('resultPanel');
            var selectedRecords = grid.getSelectedRecords();
            if (selectedRecords.length > 0) {
                var dtoGrid = view.getCmp("fadCashReqShareDto");
                Ext.Array.each(selectedRecords, function (a) {
                    var rowData = a.data;
                    var c = dtoGrid.getStore(), d = Ext.ModelManager.create({}, dtoGrid.store.model.modelName);
                    if (Scdp.ObjUtil.isNotEmpty(rowData)) {
                        d.set("officeId", rowData.orgCode);
                        d.set("officeName", rowData.orgName);
                    }
                    dtoGrid.store.insert(c.getCount(), d);
                });
                return true;
            } else {
                return true;
            }
        }
        Scdp.openNewWinByController(queryController, callBack, 'temp_icon_16');
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
    //填充申请人
    fillApplier: function () {
        var me = this;
        var view = me.view;
        view.getCmp("fadCashReqDto->staffId").putValue(Scdp.getCurrentUserId());
        view.getCmp("fadCashReqDto->officeId").putValue(Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE));

        view.getCmp("fadCashReqDto->operatorId").sotValue(Scdp.getCurrentUserId());
        view.getCmp("fadCashReqDto->operatorName").sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_NAME));
        view.getCmp("fadCashReqDto->operateTime").sotValue(Ext.Date.format(new Date(), 'Y-m-d'));
    },
    //M4_C6_F3_批量打印
    doPrintExpenseRequest: function (a, b) {
        var me = this;
        var view = me.view;
        var runningNo = view.getCmp("fadCashReqDto->runningNo").gotValue();
        var uuid = view.getCmp("fadCashReqDto->uuid").gotValue();
        var reqType = view.getCmp("fadCashReqDto->reqType").gotValue();
        if (a.cid == "expenseRequest") {

            var param = {uuid: uuid};
            if (reqType == 2) {//差旅
                Scdp.printReport("erp/prm/AccountStatementTrip.cpt", [param], false, "pdf");
            } else {
                Scdp.printReport("erp/prm/AccountStatementOthers.cpt", [param], false, "pdf");
            }
        }
        else if (a.cid == "expenseElectric") {
            var param = {uuid: uuid};
            Scdp.printReport("erp/prm/ElectricCommercialStore.cpt", [param], false, "pdf");
        }
    },
    //M4_C6_F3_批量打印
    doBatchExpenseRequest:function(){
        var me=this;
        var view=me.view;
        var uuids="";
        var reqType="";
        var grid=view.getQueryPanel().getCmp("resultPanel");
        var selectedRecords=grid.getSelectionModel().getSelection();
        if(selectedRecords.length>0){
        reqType=selectedRecords[0].data.reqType;
        for(var i=0;i<selectedRecords.length;i++){
            uuids+=selectedRecords[i].data.uuid+",";
            var flag=false;
            if(reqType=="2"){
                if(reqType!=selectedRecords[i].data.reqType){
                    flag=true;
                }
            }else{
                if("2"==selectedRecords[i].data.reqType){
                    flag=true;
                }
            }
        }
            if(flag){
                Scdp.MsgUtil.warn("选中的打印数据中的类型不相同");
                return;
            }
            var param={
                uuid:uuids.substr(0, uuids.length - 1)
            }
            if(reqType==2){
                Scdp.printReport("erp/prm/AccountStatementTrip.cpt",[param],false,"pdf");
            }else{
                Scdp.printReport("erp/prm/AccountStatementOthers.cpt",[param],false,"pdf");
            }
        }
    },
    loadWorkFlowProcessDeptCode: function () {
        var me = this;
        var processDeptCode = me.view.getCmp('fadCashReqDto->officeId').gotValue();
        return processDeptCode;
    },

    doFileUpload: function () {
        var me = this;
        var form = me;
        var fileClassify = "";
        Erp.FileUtil.erpFileUpload(form, fileClassify, me.initSubFileUploadData);
    },
    initSubFileUploadData: function (form, obj) {
        var me = form;
        var view = me.view;
        var fileName = obj.fileName

        view.getCmp("fadCashReqDto->fileId").sotValue(obj.uuid);
        var dataId = view.getCmp("fadCashReqDto->uuid").gotValue();
        var fileId = view.getCmp("fadCashReqDto->fileId").gotValue();
        var param = {dataId: dataId, fileId: fileId, actionType: 'upload'};
        Scdp.doAction("cashreq-attachment", param, function (result) {
            if (result.result == true) {
                view.getCmp("fadCashReqDto->attachmentFileName").sotValue(fileName);
            }
        });
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
    dofileDelete: function (fileGrid) {
        var me = this;
        var view = me.view;
        var fileName = view.getCmp("fadCashReqDto->attachmentFileName").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(fileName)) {
            var dataId = view.getCmp("fadCashReqDto->uuid").gotValue();
            var fileId = view.getCmp("fadCashReqDto->fileId").gotValue();
            var param = {dataId: dataId, fileId: fileId, actionType: 'delete'};
            Scdp.doAction("cashreq-attachment", param, function (result) {
                if (result.result == true) {
                    view.getCmp("fadCashReqDto->attachmentFileName").sotValue("");
                }
            });
        } else {
            Scdp.MsgUtil.warn("当前申请没有附件关联信息！");
        }
    },
    doPrintMeeting: function () {
        var me = this;
        var view = me.view;
        var runningNo = view.getCmp("fadCashReqDto->runningNo").gotValue();
        var subjectCode = view.getCmp("fadCashReqDto->subjectName").gotValue();

        var param = {runningNo: runningNo};
        if (subjectCode == "会务费") {
            Scdp.printReport("erp/prm/FadMeetingBudget.cpt", [param], false, "pdf");
        }
    },
    doFileDownload: function (fileGrid) {
        var me = this;
        var view = me.view;

        var fileName = view.getCmp("fadCashReqDto->attachmentFileName").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(fileName)) {
            var uuids = [];
            var fileId = view.getCmp("fadCashReqDto->fileId").gotValue();
            var postdata = {};
            uuids.push(fileId);
            postdata.fileList = uuids;
            var ret = Scdp.doAction("erp-common-file-download", postdata, null, null, false, false);
            var urlList = ret.URL_LIST;
            Ext.each(urlList, function (item) {
                window.open(item);
            })
        } else {
            Scdp.MsgUtil.warn("当前申请没有附件关联信息！");
        }
    }
});