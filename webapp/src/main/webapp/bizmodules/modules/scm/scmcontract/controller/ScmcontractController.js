Ext.define("Scmcontract.controller.ScmcontractController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Scmcontract.view.ScmcontractView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'scmContractDto->bankId', name: 'change', fn: 'bankIdChange'},
        {cid: 'scmContractDto->contractPayType', name: 'change', fn: 'contractPayTypeChange'},
        {cid: 'scmContractDto->contractNature', name: 'change', fn: 'contractNatureChange'},
        {cid: 'scmContractDto->totalValue', name: 'change', fn: 'totalValueChange'},
        {cid: 'scmContractDto->amount', name: 'blur', fn: 'amountChange'},
        //{cid: 'scmContractDto->payType', name: 'change', fn: 'payTypeChange'},
        {cid: 'scmContractPaytypeDto', name: 'beforeedit', fn: 'doBeforeEdit'},
        //{cid: 'submitToPerson', name: 'click', fn: 'clickSubmitToPerson'},
        {cid: 'contractRevocation', name: 'click', fn: 'clickContractRevocation'},
        //{cid: 'directEffectEdit', name: 'click', fn: 'clickDirectEffectEdit'},
        //{cid: 'directEffectQuery', name: 'click', fn: 'clickDirectEffectQuery'},
        //{cid: 'submitToPersonQuery', name: 'click', fn: 'clickSubmitToPersonQuery'},
        //{cid: 'contractRevocationQuery', name: 'click', fn: 'clickContractRevocationQuery'},
        {cid: 'balance', name: 'click', fn: 'clickBalanceEdit'},
        {cid: 'balanceQuery', name: 'click', fn: 'clickBalanceQuery'},
        {cid: 'standardModelDownloadBtn', name: 'click', fn: 'clickStandardModelDownloadBtn'},
        {cid: 'importExcelBtn', name: 'click', fn: 'clickImportExcelBtn'},
        {cid: 'importPurchaseReq', name: 'click', fn: 'clickImportPurchaseReq'},
        {cid: 'fileUpload', name: 'click', fn: 'fileUploadBtn'},
        {cid: 'fileDownload', name: 'click', fn: 'fileDownloadBtn'},
        {cid: 'filePreview', name: 'click', fn: 'filePreviewBtn'},
        {cid: 'fileDelete', name: 'click', fn: 'fileDeleteBtn'},
        {cid: 'clearingBalance', name: 'click', fn: 'clickClearingBalanceBtn'},
        {cid: 'purchaseTypes', name: 'change', fn: 'purchaseTypesChange'},
        {cid: 'scmContractDto.append->electricCommercialStore', name: 'change', fn: 'electricCommercialStoreChange'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.scmcontract.dto.ScmContractDto',
    queryAction: 'scmcontract-query',
    loadAction: 'scmcontract-load',
    addAction: 'scmcontract-add',
    modifyAction: 'scmcontract-modify',
    deleteAction: 'scmcontract-delete',
    exportXlsAction: "scmcontract-exportxls",
    afterInit: function () {
        var me = this;
        me.callParent(arguments);
        me.view.getCmp("fileUpload").setDisabled(true);
        me.view.getCmp("fileDelete").setDisabled(true);
        me.view.getCmp("importExcelBtn").setDisabled(true);
        //合同状态
        //var contractState = me.view.getCmp("contractState").gotValue();
        //if (contractState == "5") {
        //    me.view.getCmp("modifyBtn").setDisabled(false);
        //}
        var actionParam = me.actionParams;
        var uuid = actionParam.uuid;
        if (Scdp.ObjUtil.isNotEmpty(uuid)) {
            me.loadItem(uuid);
        } else {
            me.initValue();
        }
        actionParam.uuid = null;
    },
    beforeAdd: function () {
        return true;
    },
    afterAdd: function () {
        var me = this;
        this.btnShowControl();
        this.contractEditInit();
    },
    beforeCopyAdd: function () {
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        me.contractEditInit();
        me.btnShowControl();
    },
    beforeModify: function () {
        return true;
    },
    afterModify: function () {
        var me = this;
        me.btnShowControl();
        me.controlBasicInfoEditable();
        //me.contractEditInit();
        me.setbudgetAndExpectedAmount();
        me.initAmount();
        me.initIsClosed();
        me.fireLoadBankIdCombo();
        me.view.getCmp("deleteBtn").setDisabled(true);
        me.view.getCmp("modifyBtn").setDisabled(true);
        //me.view.getCmp("fileUpload").setDisabled(false);
        //me.view.getCmp("fileDelete").setDisabled(false);
        me.view.getCmp("scmContractDto->contractNature").focus();
    },
    beforeSave: function () {
        var me = this;
        var amount = me.view.getCmp("scmContractDto->amount").gotValue();
        var items = me.view.getCmp("scmContractPaytypeDto").store.data.items;
        if (items.length != 0) {
            //2.总份额不能超过100%
            var p = 0;
            var actuallyPaid = 0;
            for (var i = 0; i < items.length; i++) {
                p = p + Number(items[i].data.value);
                actuallyPaid = actuallyPaid + Number(items[i].data.actuallyPaid);
            }
            if (Math.abs(Erp.MathUtil.minusNumber(p, 100)) > 0.01) {
                Scdp.MsgUtil.info("支付明细中额度分配错误！");
                return false;
            }
            if (Math.abs(Erp.MathUtil.minusNumber(actuallyPaid, amount)) > 0.01) {
                Scdp.MsgUtil.info("支付明细中金额分配错误！");
                return false;
            }
        }
        ////1.判断合同总额是否等于实价总计合计
        //
        //var totalTrueInDetailGrid = me.view.getCmp("totalTrueInDetailGrid").gotValue();
        //if (Scdp.ObjUtil.isNotEmpty(totalTrueInDetailGrid)) {
        //    if (totalTrueInDetailGrid != 0) {
        //        if (amount != totalTrueInDetailGrid) {
        //            Scdp.MsgUtil.info("合同金额与合同明细中实价总计合计不一致，请调整！");
        //            return false;
        //        }
        //    }
        //}
        return true;
    },
    afterSave: function () {
        var me = this;
        me.callParent(arguments);
        me.btnShowControl();
        me.view.getCmp("scmContractDto->contractNature").focus();
    },
    beforeLoadItem: function () {
        return true;
    },
    initFileBtnAfterLoad: function () {
        var me = this;
        var view = me.view;
        var conetractState = view.getCmp("scmContractDto->contractState").gotValue();
        var enableStates = ['4', '5'];
        if (Ext.Array.contains(enableStates, conetractState)) {
            view.getCmp("fileUpload").setDisabled(false);
            view.getCmp("fileDelete").setDisabled(false);
        } else {
            view.getCmp("fileUpload").setDisabled(true);
            view.getCmp("fileDelete").setDisabled(true);
        }
    },
    afterLoadItem: function () {

        var me = this;
        var view = me.view;
        me.callParent(arguments);
        //me.fireLoadBankIdCombo();
        me.btnShowControl();
        me.calculateTotalTruePrice();
        me.setbudgetAndExpectedAmount();
        me.initFileBtnAfterLoad();
        view.getCmp("scmContractDto->contractNature").focus();
        var fadSubjectCodeCmp = view.getCmp("scmContractDto->fadSubjectCode");
        if (view.getCmp("scmContractDto->isProject").getValue() == "1") {
            fadSubjectCodeCmp.setFieldStyle("color: blue;line-height:1;height:21px;text-decoration: underline;cursor:pointer");
        } else {
            fadSubjectCodeCmp.setFieldStyle("color: black;line-height:0;height:21px;text-decoration: none");
        }
        var supplierNameCmp = view.getCmp("scmContractDto->supplierName");
        if (Scdp.ObjUtil.isNotEmpty(supplierNameCmp.gotValue())) {
            supplierNameCmp.setFieldStyle("color: blue;line-height:1;height:21px;text-decoration: underline;cursor:pointer");
        } else {
            supplierNameCmp.setFieldStyle("color: black;line-height:0;height:21px;text-decoration: none");
        }

        var supplierCodeValue = view.getCmp("scmContractDto->supplierCode").gotValue();
        var electricCommercialStoreCmp = view.getCmp("scmContractDto.append->electricCommercialStore");
        var officeCode = view.getCmp("scmContractDto->officeId").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(supplierCodeValue)) {
            electricCommercialStoreCmp.filterConditions = {selfconditions: " office_id = '" + officeCode + "' and  t1.scm_supplier_id = '" + supplierCodeValue + "'"};
        } else {
            electricCommercialStoreCmp.filterConditions = {selfconditions: " office_id = '" + officeCode + "'"};
        }
        me.initReportIframe();
        me.initReportForm();
    },
    beforeCancel: function () {
        return true;
    },
    afterCancel: function () {
        var me = this;
        me.btnShowControl();
        me.setbudgetAndExpectedAmount();
        me.calculateTotalTruePrice();
        me.view.getCmp("scmContractDto->contractNature").focus();
    },
    //不能删除存在询价单的合同，请在询价单页面删除对应明细之后再进行操作！
    beforeDelete: function () {
        var me = this;
        var prmPurchaseReqDetailGrid = me.view.getCmp("prmPurchaseReqDetailDto");
        if (prmPurchaseReqDetailGrid.getStore().data.items.length > 0) {
            Scdp.MsgUtil.info("不能删除存在询价单的合同，请在询价单页面删除对应明细之后再进行操作！");
            return false;
        }
        return true;
    },
    afterDelete: function () {
        var me = this;
        me.btnShowControl();
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
    //银行账号改变，自动带出银行名称
    bankIdChange: function (obj) {
        var me = this;
        if (Scdp.ObjUtil.isNotEmpty(obj.displayTplData)) {
            me.view.getCmp("scmContractDto->bank").sotValue(obj.displayTplData[0].codedesc);
            var bank = me.view.getCmp("scmContractDto->bank");
            bank.sotValue(obj.displayTplData[0].codedesc);
        }
    },
    //合同付款方式的change事件写在这里
    contractPayTypeChange: function () {
        var me = this;
        me.contractItemInit();
        me.contractEditInit();
    },
    //合同性质的change事件写在这里
    contractNatureChange: function () {
        var me = this;
        me.contractItemInit();
        me.contractEditInit();
    },
    //合同编制，条款初始化规则
    contractItemInit: function () {
        var me = this;
        var view = me.view;
        var amount = view.getCmp("scmContractDto->amount").value;
        var purchaseTypes = me.view.getCmp("scmContractDto->purchaseTypes");//合同支付方式
        var purchaseTypesValue = purchaseTypes.gotValue();//合同支付方式
        var contractPayType = view.getCmp("scmContractDto->contractPayType");//合同付款方式
        var contractPayTypeValue = contractPayType.gotValue();//合同付款方式
        var contractNature = view.getCmp("scmContractDto->contractNature");//合同性质
        var contractNatureValue = contractNature.gotValue();//合同性质
        var scmContractPaytypeGrid = view.getCmp("scmContractPaytypeDto");//获取grid
        var payTypeNoWay = ["1", "2", "5"];
        if (purchaseTypesValue == "00" && Ext.Array.contains(payTypeNoWay, contractPayTypeValue)) {
            Scdp.MsgUtil.info("零星采购只能选用“个人报销”或“其他”！");
            contractPayType.sotValue("");
            return false;
        }
        scmContractPaytypeGrid.clearData();
        //1.如果付款方式为合同
        var temp = {};
        if (contractPayTypeValue == "1") {
            //1.1合同性质为采购
            //合同性质为采购时条款记录的初始化,1）预 付 款： 合同额（ 20 ）%；   2）第二期款：到货款（ 40 ）%；		3）第三期款：安装调试（ 20 ）%；	4）第四期款：项目验收款 （ 15 ）% 5）尾款：质保金 （ 5 ）%。

            if (contractNatureValue == "0") {
                temp.title = "预付款";
                temp.value = "20";
                scmContractPaytypeGrid.addRowItem(temp, false);
                temp.title = "到货款";
                temp.value = "40";
                scmContractPaytypeGrid.addRowItem(temp, false);
                temp.title = "安装调试";
                temp.value = "20";
                scmContractPaytypeGrid.addRowItem(temp, false);
                temp.title = "项目验收款";
                temp.value = "15";
                scmContractPaytypeGrid.addRowItem(temp, false);
                temp.title = "质保金";
                temp.value = "5";
                scmContractPaytypeGrid.addRowItem(temp, false);
            } else if (contractNatureValue == "1") {
                //1.2合同性质为外协
                //合同性质为外协时条款记录的初始化，1）预 付 款： 预付款（ 15 ）%；   2）第二期款：业主验收会前的计量款（ 55 ）%；		3）第三期款：业主交工验收后的计量款，（ 25 ）%；4）尾款：缺陷责任期满后的尾款 （ 5 ）%。
                temp.title = "预付款";
                temp.value = "15";
                scmContractPaytypeGrid.addRowItem(temp, false);
                temp.title = "业主验收会前的计量款";
                temp.value = "55";
                scmContractPaytypeGrid.addRowItem(temp, false);
                temp.title = "业主交工验收后的计量款";
                temp.value = "25";
                scmContractPaytypeGrid.addRowItem(temp, false);
                temp.title = "缺陷责任期满后的尾款";
                temp.value = "5";
                scmContractPaytypeGrid.addRowItem(temp, false);
                me.view.getCmp("totalValue").sotValue("");
            }
        }
        if (contractPayTypeValue == "5") {
            me.batchControlJdInfo("editable", true);
            me.batchControlJdInfo("allowBlank", false);
            temp.title = "预付款";
            temp.value = "100";
            scmContractPaytypeGrid.addRowItem(temp, false);
        } else {
            me.batchControlJdInfo("editable", false);
            me.batchControlJdInfo("allowBlank", true);
            me.batchControlJdInfo("valueNull");
            contractNature.sotEditable(true);
            contractPayType.sotEditable(true);
        }
        me.calculateTotalValue();
    },
    //页面元素是否可编辑的状态初始化
    contractEditInit: function () {
        var me = this;
        var view = me.view;
        //初始化可用状态的标签
        //请款申请
        //收款人
        view.getCmp("scmContractDto->debterDepartment").sotEditable(true);
        view.getCmp("scmContractDto->debterDepartment").allowBlank = true;
        view.getCmp("scmContractDto->debterName").sotEditable(true);
        view.getCmp("scmContractDto->debterName").allowBlank = true;

        //收款单位
        view.getCmp("scmContractDto->bankId").sotEditable(true);
        view.getCmp("scmContractDto->bankId").allowBlank = true;

        view.getCmp("scmContractDto->payeeName").sotEditable(true);
        view.getCmp("scmContractDto->payeeName").allowBlank = true;

        //view.getCmp("otherDes").sotEditable(true);
        view.getCmp("scmContractDto->bank").sotEditable(true);
        view.getCmp("scmContractDto->payType").sotEditable(true);
        view.getCmp("scmContractDto->otherDes").sotEditable(true);
        view.getCmp("scmContractDto->isUrgent").sotEditable(true);
        view.getCmp("scmContractDto->totalValue").sotEditable(true);
        view.getCmp("scmContractDto->debtDate").sotEditable(true);

        view.getCmp("scmContractDto->contractState").sotEditable(false);
        view.getCmp("scmContractDto->scmContractCode").sotEditable(false);
        view.getCmp("scmContractDto->projectName").sotEditable(false);

        //工具栏初始化为不可用
        var contractPayTypeValue = view.getCmp("scmContractDto->contractPayType").gotValue();//合同付款方式
        var contractStateValue = view.getCmp("scmContractDto->contractState").gotValue();//合同状态

        //合同状态为0时，显示的合同状态设置为1
        if (contractStateValue == "0") {
            view.getCmp("contractState").sotValue("1");
        } else if (contractStateValue == "4" || contractStateValue == "5") {
            view.getCmp("scmContractDto").sotEditable(false);
            view.getCmp("scmContractDetailDto").sotEditable(false);
            view.getCmp("scmContractPaytypeDto").sotEditable(false);
            view.getCmp("importExcelBtn").setDisabled(true);
            view.getCmp("importPurchaseReq").setDisabled(true);
        } else if (contractPayTypeValue == "1" || contractPayTypeValue == "5") {//3.1如果付款方式为合同支付,或者商城支付
            view.getCmp("scmContractDto->debterDepartment").sotEditable(false);
            view.getCmp("scmContractDto->debterDepartment").allowBlank = true;
            view.getCmp("scmContractDto->debterDepartment").sotValue("");
            view.getCmp("scmContractDto->debterName").sotEditable(false);
            view.getCmp("scmContractDto->debterName").allowBlank = true;
            view.getCmp("scmContractDto->debterName").sotValue("");
            view.getCmp("scmContractDto->bankId").allowBlank = false;
            view.getCmp("scmContractDto->payeeName").allowBlank = false;
            view.getCmp("scmContractDto->debter").sotValue("");
            //view.getCmp("scmContractDto->otherDes").sotEditable(false);
            //view.getCmp("scmContractDto->otherDes").sotValue("");
            view.getCmp("scmContractDto->totalValue").sotEditable(false);
            view.getCmp("scmContractDto->debtDate").sotValue(new Date());
        } else if (contractPayTypeValue == "2") {//3.2如果合同付款方式为"个人请款"，“收款单位”区不能编辑，“其它备注”不能编辑
            view.getCmp("scmContractDto->bank").sotEditable(false);
            view.getCmp("scmContractDto->bank").allowBlank = true;
            view.getCmp("scmContractDto->bank").sotValue("");
            view.getCmp("scmContractDto->bankId").sotEditable(false);
            view.getCmp("scmContractDto->bankId").allowBlank = true;
            view.getCmp("scmContractDto->bankId").sotValue("");
            //view.getCmp("scmContractDto->otherDes").sotEditable(false);
            //view.getCmp("scmContractDto->otherDes").sotValue("");
            view.getCmp("scmContractDto->payeeName").sotEditable(false);
            view.getCmp("scmContractDto->debterName").allowBlank = false;
            view.getCmp("scmContractDto->debterDepartment").allowBlank = false;
            view.getCmp("scmContractDto->payeeName").sotValue("");
            view.getCmp("debtDate").sotValue(new Date());
        } else if (contractPayTypeValue == "3" || contractPayTypeValue == "4") {//3.3如果合同付款方式为“个人报销”或其它时“请款区”不能编辑
            view.getCmp("scmContractDto->debterDepartment").sotEditable(false);
            view.getCmp("scmContractDto->debterDepartment").allowBlank = true;
            view.getCmp("scmContractDto->debterDepartment").sotValue("");
            view.getCmp("scmContractDto->debterName").sotEditable(false);
            view.getCmp("scmContractDto->debterName").allowBlank = true;
            view.getCmp("scmContractDto->debterName").sotValue("");
            view.getCmp("scmContractDto->debter").sotEditable(false);
            view.getCmp("scmContractDto->debter").allowBlank = true;
            view.getCmp("scmContractDto->debter").sotValue("");
            view.getCmp("scmContractDto->bank").sotEditable(false);
            view.getCmp("scmContractDto->bank").allowBlank = true;
            view.getCmp("scmContractDto->bank").sotValue("");
            view.getCmp("scmContractDto->bankId").sotEditable(false);
            view.getCmp("scmContractDto->bankId").allowBlank = true;
            view.getCmp("scmContractDto->bankId").sotValue("");
            view.getCmp("scmContractDto->payType").sotEditable(false);
            view.getCmp("scmContractDto->payType").sotValue("");
            view.getCmp("scmContractDto->otherDes").sotEditable(false);
            view.getCmp("scmContractDto->otherDes").allowBlank = true;
            view.getCmp("scmContractDto->otherDes").sotValue("");
            view.getCmp("scmContractDto->isUrgent").sotEditable(false);
            view.getCmp("scmContractDto->isUrgent").allowBlank = true;
            view.getCmp("scmContractDto->isUrgent").sotValue("");
            view.getCmp("scmContractDto->totalValue").sotEditable(false);
            view.getCmp("scmContractDto->totalValue").allowBlank = true;
            view.getCmp("scmContractDto->totalValue").sotValue("");
            view.getCmp("scmContractDto->debtDate").sotEditable(false);
            view.getCmp("scmContractDto->debtDate").allowBlank = true;
            view.getCmp("scmContractDto->debtDate").sotValue("");
            view.getCmp("scmContractDto->payeeName").sotEditable(false);
            view.getCmp("scmContractDto->payeeName").allowBlank = true;
            view.getCmp("scmContractDto->payeeName").sotValue("");
            //if (contractPayTypeValue == "4") {
            //    view.getCmp("scmContractDto->otherDes").sotEditable(false);
            //    view.getCmp("scmContractDto->otherDes").sotEditable(false);
            //}
        }
        //商城信息控制
        if (Scdp.ObjUtil.isNotEmpty(contractPayTypeValue) && "5" == contractPayTypeValue) {
            me.batchControlJdInfo("editable", true);
            me.batchControlJdInfo("allowBlank", false);
        } else {
            me.batchControlJdInfo("editable", false);
        }
        return true;
    },
    batchControlJdInfo: function (state, value) {
        var me = this;
        var view = me.view;
        var electricCommercialStore = view.getCmp("scmContractDto.append->electricCommercialStore");
        var jdName = view.getCmp("scmContractDto.append->jdName");
        var jdPassword = view.getCmp("scmContractDto.append->jdPassword");
        var jdOrderNo = view.getCmp("scmContractDto.append->jdOrderNo");
        var jdOrderDate = view.getCmp("scmContractDto.append->jdOrderDate");
        var jdLastDate = view.getCmp("scmContractDto.append->jdLastDate");
        if (state == "editable") {
            electricCommercialStore.sotEditable(value);
            jdName.sotEditable(value);
            jdPassword.sotEditable(value);
            jdOrderNo.sotEditable(value);
            jdOrderDate.sotEditable(value);
            jdLastDate.sotEditable(value);
        } else if (state == "allowBlank") {
            electricCommercialStore.allowBlank = value;
            jdName.allowBlank = value;
            jdPassword.allowBlank = value;
            jdOrderNo.allowBlank = value;
            jdOrderDate.allowBlank = value;
            jdLastDate.allowBlank = value;
        } else if (state == "valueNull") {
            electricCommercialStore.sotValue("");
            jdName.sotValue("");
            jdPassword.sotValue("");
            jdOrderNo.sotValue("");
            jdOrderDate.sotValue("");
            jdLastDate.sotValue("");
        }
    },

    //点击直接生效按钮(查询面板)
    //clickDirectEffectQuery: function () {
    //    var me = this;
    //    var selectedSubject = me.view.getResultPanel().getCurRecord();
    //    if (selectedSubject == null) {
    //        Scdp.MsgUtil.info("未选择数据！");
    //        return false;
    //    }
    //    var uuid = selectedSubject.data.uuid;
    //    var contractState = selectedSubject.data.contractState;
    //    if (!me.checkIfDirectEffect(uuid, contractState)) {
    //        return false;
    //    }
    //    Scdp.MsgUtil.confirm("是否直接生效该合同/虚拟合同？", function (e) {
    //        if ("yes" == e) {
    //            var postData = {uuid: uuid};
    //            Scdp.doAction("scmcontract-directeffect", postData, function () {
    //                me.doQuery();
    //                Scdp.MsgUtil.info("合同生效成功！");
    //            });
    //        }
    //    })
    //},
    //点击直接生效按钮(编辑面板)
    //clickDirectEffectEdit: function () {
    //    var me = this;
    //    var uuid = me.view.getCmp('scmContractDto->uuid').gotValue();
    //    var contractState = me.view.getCmp("scmContractDto->contractState").gotValue();
    //    if (!me.checkIfDirectEffect(uuid, contractState)) {
    //        return false;
    //    }
    //    Scdp.MsgUtil.confirm("是否直接生效该合同/虚拟合同？", function (e) {
    //        if ("yes" == e) {
    //            var postData = {uuid: uuid};
    //            Scdp.doAction("scmcontract-directeffect", postData, function () {
    //                me.view.getCmp("scmContractDto->contractState").sotValue("5");
    //                Scdp.MsgUtil.info("合同生效成功！");
    //            });
    //        }
    //    })
    //},
    //点击结算按钮
    clickBalanceQuery: function () {
        var me = this;
        var selectedSubject = me.view.getResultPanel().getCurRecord();
        if (selectedSubject == null) {
            Scdp.MsgUtil.info("未选择数据！");
            return false;
        }
        var uuid = selectedSubject.data.uuid;
        var state = selectedSubject.data.state;
        var isClosed = selectedSubject.data.isClosed;
        if (!me.checkIfBalance(uuid, state, isClosed)) {
            return false;
        }
        Scdp.MsgUtil.confirm("是否结算该合同？", function (e) {
            if ("yes" == e) {
                var postData = {uuid: uuid};
                Scdp.doAction("scmcontract-balance", postData, function () {
                    me.doQuery();
                    Scdp.MsgUtil.info("结算合同成功!");
                })
            }
        })
    },
    clickBalanceEdit: function () {
        var me = this;
        var uuid = me.view.getCmp('scmContractDto->uuid').gotValue();
        var state = me.view.getCmp('scmContractDto->state').gotValue();
        var isClosed = me.view.getCmp('scmContractDto->isClosed').gotValue();
        if (!me.checkIfBalance(uuid, state, isClosed)) {
            return false;
        }
        Scdp.MsgUtil.confirm("是否结算该合同？", function (e) {
            if ("yes" == e) {
                var postData = {uuid: uuid};
                Scdp.doAction("scmcontract-balance", postData, function () {
                    me.view.getCmp("scmContractDto->contractState").sotValue("5");
                    Scdp.MsgUtil.info("结算合同成功！");
                    me.btnShowControl();
                })
            }
        })
    },
    //点击合同废止按钮
    clickContractRevocationQuery: function () {
        var me = this;
        var selectedSubject = me.view.getResultPanel().getCurRecord();
        if (selectedSubject == null) {
            Scdp.MsgUtil.info("未选择数据");
            return false;
        }
        var uuid = selectedSubject.data.uuid;
        if (uuid == "") {
            Scdp.MsgUtil.info("未选择数据");
            return false;
        }
        var contractState = selectedSubject.data.contractState;
        if (Scdp.ObjUtil.isNotEmpty(contractState)) {
            if (contractState == "3") {
                Scdp.MsgUtil.info("已提交审核的合同不能被废止！");
                return false;
            } else if (contractState == "7") {
                Scdp.MsgUtil.info("该合同当前状态已经是合同废止状态！");
                return false;
            }
        }
        Scdp.MsgUtil.confirm("是否废止该合同/虚拟合同？", function (e) {
            if ("yes" == e) {
                var callBack = function (subView) {
                    var fallbackReason = subView.getCmp("fallbackReason").gotValue();
                    var postData = {uuid: uuid, fallbackReason: fallbackReason};
                    Scdp.doAction("scmcontract-contractrevocation", postData, function () {
                        me.doQuery();
                        Scdp.MsgUtil.info("合同已被废止");
                    })
                    win.close();
                };
                var form = Ext.widget("JForm", {
                    height: 80,
                    width: 600,
                    layout: 'form',
                    bodyPadding: '10 10 10 10',
                    items: [
                        {
                            xtype: 'JCombo',
                            comboType: 'scdp_fmcode',
                            codeType: 'RETURN_PURCHASE_REQ',
                            allowBlank: false,
                            fieldLabel: '废止原因',
                            cid: 'fallbackReason',
                            valueField: 'codedesc',
                            forceSelection: false,
                            displayDesc: true,
                            fullInfo: true,
                            width: 150,
                            height: 21
                        }
                    ]
                });
                var win = Scdp.openNewWinByView(form, callBack, 'temp_icon_16', '退回', "处理");

            }
        })
    },
    clickContractRevocation: function () {
        var me = this;
        var uuid = me.view.getCmp('scmContractDto->uuid').gotValue();
        if (uuid == "") {
            Scdp.MsgUtil.info("未选择数据");
            return false;
        }
        Scdp.MsgUtil.confirm("是否废止该合同/虚拟合同？", function (e) {
            if ("yes" == e) {
                var callBack = function (subView) {
                    var fallbackReason = subView.getCmp("fallbackReason").gotValue();
                    var postData = {uuid: uuid, fallbackReason: fallbackReason};
                    Scdp.doAction("scmcontract-contractrevocation", postData, function (result) {
                        if (Scdp.ObjUtil.isNotEmpty(result.result) && result.result == true) {
                            me.loadItem(uuid);
                            me.btnShowControl();
                            Scdp.MsgUtil.info("合同已被废止！");
                        }
                    });
                    win.close();
                };
                var form = Ext.widget("JForm", {
                    height: 80,
                    width: 600,
                    layout: 'form',
                    bodyPadding: '10 10 10 10',
                    items: [
                        {
                            xtype: 'JCombo',
                            comboType: 'scdp_fmcode',
                            codeType: 'RETURN_PURCHASE_REQ',
                            allowBlank: false,
                            fieldLabel: '废止原因',
                            cid: 'fallbackReason',
                            valueField: 'codedesc',
                            forceSelection: false,
                            displayDesc: true,
                            fullInfo: true,
                            width: 150,
                            height: 21
                        }
                    ]
                });
                var win = Scdp.openNewWinByView(form, callBack, 'temp_icon_16', '退回', "处理");

            }
        })
    },
    //点击导入Excel按钮
    clickImportExcelBtn: function () {
        var me = this;
        var view = me.view;
        var scmContractDetailDtoCount = view.getCmp("scmContractDetailDto").getStore().getCount();
        if (scmContractDetailDtoCount > 0) {
            Scdp.MsgUtil.confirm("是否上传？(将覆盖原合同明细数据)", function (e) {
                if (e == "yes") {
                    me.openImportExcelWindow(view);
                }
            });
        } else {
            me.openImportExcelWindow(view);
        }

    },
    openImportExcelWindow: function (view) {
        var me = this;
        var formView = Ext.widget("JForm", {
            height: 50,
            width: 500,
            layout: 'form',
            bodyPadding: '10 10 10 10',
            cid: 'uploadForm',
            items: [
                {
                    xtype: 'JFile',
                    fieldLabel: '上传文件名',
                    cid: 'uploadFile'
                }
            ]
        });
        var callBack = function (subView) {
            var uploadFile = subView.getCmp("uploadFile");
            var uploadData = [];
            if (Scdp.ObjUtil.isEmpty(uploadFile.gotValue())) {
                Scdp.MsgUtil.info("Please select file!");
                return;
            }
            var scmContractUuid = view.getCmp("scmContractDto->uuid").value;
            uploadData.push(scmContractUuid);
            uploadData.push("new");
            var postData = {uploadMeta: uploadData};
            Scdp.doAction("scmcontract-importexcel", postData, function (result) {
                if (result.saveFlag) {
                    Scdp.MsgUtil.info(Scdp.I18N.DATA_NOT_CHANGE);
                    me.loadItem(result.scmContractUuid);
                } else {
                    Erp.Util.showLogView(Erp.I18N.EXCEL_UPLOAD_FAILURE + Erp.Const.BREAK_LINE + result.errorMsgLog);
                }
                win.close();
            }, null, null, null, subView.getForm());
        };
        var win = Scdp.openNewWinByView(formView, callBack, 'temp_icon_16', '文件上传', "上传文件");
    },
    clickImportPurchaseReq: function () {
        var me = this;
        var uuid = me.view.getCmp("uuid").gotValue();
        if (Scdp.ObjUtil.isEmpty(uuid)) {
            Scdp.MsgUtil.info("未选择记录");
            return false;
        }
        var prmPurchaseReqDetailItem = me.view.getCmp("prmPurchaseReqDetailDto").store.data.items;
        if (Scdp.ObjUtil.isNotEmpty(prmPurchaseReqDetailItem)) {
            var scmContractDetailGrid = me.view.getCmp("scmContractDetailDto");
            scmContractDetailGrid.clearData();
            for (var i = 0; i < prmPurchaseReqDetailItem.length; i++) {
                var temp = {};
                temp.materialNumber = prmPurchaseReqDetailItem[i].data.serialNumber;
                temp.materialName = prmPurchaseReqDetailItem[i].data.name;
                temp.model = prmPurchaseReqDetailItem[i].data.model;
                var expectedPrice = prmPurchaseReqDetailItem[i].data.expectedPrice;
                if (Scdp.ObjUtil.isEmpty(expectedPrice)) {
                    expectedPrice = 0;
                }
                var handleAmount = prmPurchaseReqDetailItem[i].data.handleAmount;
                if (Scdp.ObjUtil.isEmpty(handleAmount)) {
                    handleAmount = 0;
                }
                temp.amount = handleAmount;
                temp.units = prmPurchaseReqDetailItem[i].data.unit;
                temp.unitPriceTalk = expectedPrice;
                temp.unitPriceTrue = expectedPrice;
                temp.totalPriceTalk = expectedPrice * handleAmount;
                temp.totalPriceTrue = expectedPrice * handleAmount;
                temp.factory = prmPurchaseReqDetailItem[i].data.factory;
                temp.remark = prmPurchaseReqDetailItem[i].data.remark;
                temp.seqNo = i;
                scmContractDetailGrid.addRowItem(temp, false);
            }
            me.calculateTotalTruePrice();
            me.view.getCmp("scmContractDto->contractState").sotValue("2");
        } else {
            Scdp.MsgUtil.info("该合同无询价单！");
        }
    },
    //标准模板下载
    clickStandardModelDownloadBtn: function () {
        var fileClassifyType = "SCM_CONTRACT_DETAIL_TEMPLATE";
        var cdmFileType = "SCM_CONTRACT";
        var postdata = {fileClassifyType: fileClassifyType, cdmFileType: cdmFileType};
        Scdp.doAction("scmcontract_getfileid", postdata, function (result) {
            var fileId = result.fileId;
            var fileList = [];
            fileList.push(fileId);
            var params = {fileList: fileList};
            var ret = Scdp.doAction("erp-common-file-download", params, null, null, false, false);
            var urlList = ret.URL_LIST;
            Ext.each(urlList, function (item) {
                window.open(item);
            })
        });
    },
    //重写新增按钮
    doAdd: function () {
        var me = this;
        me.callParent(arguments);
        me.view.getCmp("scmContractDto->contractNature").sotValue("0");
        me.view.getCmp("scmContractDto->contractPayType").sotValue("1");
    },
    //合同总额变更事件写在这里
    amountChange: function () {
        var me = this;
        var amount = me.view.getCmp("scmContractDto->amount").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(amount)) {
            if (!me.view.getCmp("scmContractDto->amount").isEditable()) {
                return false;
            }
            //判断合同总额不能大于（预算单价*数量）
            //var prmPurchaseReqDetailGridItems = me.view.getCmp("prmPurchaseReqDetailDto").store.data.items;
            //if (prmPurchaseReqDetailGridItems.length > 0) {
            //    for (var i = 0; i < prmPurchaseReqDetailGridItems.length; i++) {
            //        totalExpectedPrice = totalExpectedPrice + prmPurchaseReqDetailGridItems[i].get("expectedPrice") * prmPurchaseReqDetailGridItems[i].get("handleAmount");
            //    }
            //}
            var totalExpectedPrice = me.view.getCmp("scmContractDto->expectedAmount").gotValue();
            if (amount > totalExpectedPrice) {
                me.view.getCmp("scmContractDto->amount").sotValue(totalExpectedPrice);
                Scdp.MsgUtil.info("合同金额不能超过意向合计！");
                return false;
            }
            if (me.view.getCmp("scmContractDto->amount").gotValue() >= 0) {
                me.calculateTotalValue();
            }
        }
    },
    //payTypeChange:function(){
    //    var me=this;
    //    var view=me.view;
    //    var payType = view.getCmp("scmContractDto->payType");
    //    if(payType=="0"||payType=="2"){
    //        view.getCmp("scmContractDto->payeeName").allowBlank = true;
    //    }else{
    //        view.getCmp("scmContractDto->payeeName").allowBlank = false;
    //        view.getCmp("scmContractDto->bank").allowBlank = false;
    //        view.getCmp("scmContractDto->bankId").allowBlank = true;
    //    }
    //    view.getCmp("scmContractDto->bankId").allowBlank = true;
    //},
    //预付款额度变更事件写在这里
    doBeforeEdit: function (editor, context) {
        var me = this;
        if (context.field == "title") {
            if (context.value == "预付款") {
                return false;
            }
        }
        var scmContractPayTypeGrid = me.view.getCmp("scmContractPaytypeDto");
        //scmContractPayTypeGrid.store.on('update', function () {
        //    me.calculateTotalValue();
        //});
    },
    //计算请款金额
    calculateTotalValue: function () {
        var me = this;
        var amount = me.view.getCmp("scmContractDto->amount").value;
        if (Scdp.ObjUtil.isEmpty(amount) || amount < 0) {
            return false;
        }
        var scmContractPaytypeDto = me.view.getCmp("scmContractPaytypeDto");
        var items = scmContractPaytypeDto.store.data.items;
        //1.如果没选付款方式，则请款金额为0
        if (items.length == 0) {
            me.view.getCmp("scmContractDto->totalValue").sotValue("0");
            return false;
        }
        //3.如果出现了预付款，则以此条记录计算请款金额

        for (var i = 0; i < items.length; i++) {
            items[i].set("actuallyPaid", amount * items[i].data.value / 100);
            if ("预付款" == items[i].data.title) {
                if (items[i].data.actuallyPaid >= 0) {
                    me.view.getCmp("scmContractDto->totalValue").sotValue(items[i].data.actuallyPaid);
                } else {
                    me.view.getCmp("scmContractDto->totalValue").sotValue("0");
                }
            }
        }
        return false;
    },
    //请款金额change事件，判断金额是否合法
    totalValueChange: function () {
        var me = this;
        if (me.view.getCmp("scmContractDto->totalValue").value > me.view.getCmp("scmContractDto->amount").value) {
            me.view.getCmp("scmContractDto->totalValue").sotValue(me.view.getCmp("scmContractDto->totalValue").oldValue);
            Scdp.MsgUtil.warn("请款金额不能大于合同总额");
        }
    },
    //文件上传
    fileUploadBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        var fileClassify = "CDM_FILE_TYPE_DETAIL";
        this.scmFileUpload(grid, fileClassify, null, null, null, "CONTRACTESTABLISHMENT");
    },
    scmFileUpload: function (fileGrid, fileClassify) {
        var me = this;
        var uuid = me.view.getCmp("uuid").gotValue();
        var contractState = me.view.getCmp("contractState").gotValue();
        var itemsArray = [];
        var height = 180;
        var regexText = '支持的文件格式：pdf,doc,docx,xls,xlsx,ppt,pptx,txt,bmp,jpg,jpeg,gif,png';
        itemsArray.push({
            xtype: "JFile",
            fieldLabel: "文件名",
            cid: "uploadFile",
            allowBlank: !1,
            regex: /(.)+((\.pdf)|(\.doc)|(\.docx)|(\.xls)|(\.xlsx)|(\.ppt)|(\.pptx)|(\.txt)|(\.bmp)|(\.jpg)|(\.jpeg)|(\.gif)|(\.png)(\w)?)$/i,
            regexText: regexText,
            listeners: {
                change: function (a) {
                    a = a.gotValue();
                    a = Scdp.StrUtil.getLastSplit(a, "\\");
                    a = Scdp.StrUtil.replaceNull(Scdp.StrUtil.getLastSplit(a,
                        ".")).toUpperCase();
                    this.up("JForm").getCmp("fileType").sotValue(a);
                }
            }
        });
        itemsArray.push({xtype: "JText", fieldLabel: "文件类型", cid: "fileType", hidden: true});
        itemsArray.push({
            xtype: "JCombo",
            fieldLabel: "业务文件类型",
            id: "fileClassify",
            cid: "fileClassify",
            menuCode: "CONTRACTESTABLISHMENT",
            allowBlank: !1,
            comboType: "scdp_fmcode",
            codeType: fileClassify,
            fullInfo: !0,
            displayDesc: !0,
            labelAlign: "right"
        });
        itemsArray.push({xtype: "JTextArea", fieldLabel: "备注", cid: "fileRemark"});
        itemsArray.push({xtype: 'JDisplay', value: regexText, fieldStyle: {color: 'red'}});
        Ext.define("JFileUploadView0", {
            extend: "Scdp.container.JFileUploadView",
            height: height,
            alias: ["widget.JFileUploadView0"],
            items: itemsArray,
            okFunction: function (a) {
                if (!a.validate())return !1;
                var postData = {};
                postData.fileType = a.getCmp("fileType").gotValue();
                postData.fileRemark = a.getCmp("fileRemark").gotValue();
                postData.cdmFileType = "SCM_CONTRACT";
                if (Scdp.ObjUtil.isNotEmpty(a.getCmp("fileClassify"))) {
                    postData.fileClassify = a.getCmp("fileClassify").gotValue();
                    postData.dataId = uuid;
                }
                if (Scdp.ObjUtil.isNotEmpty(contractState)) {
                    if (contractState == "4") {
                        var fileClassify = postData.fileClassify;
                        if (fileClassify == "SCM_CONTRACT_SCAN") {
                            postData.needPersistence = "1";
                            Scdp.doAction("scmcontract-upload-and-save", postData, function (result) {
                                if (result != "" || result != undefined) {
                                    Erp.FileUtil.initFileUploadData(fileGrid, result.fileData);
                                    //var view = fileGrid.up("IView");
                                    //view.controller.doSave();
                                    //view.controller.loadItem(view.getCmp("scmContractDto->uuid").value);
                                }
                                a.up("window").close();
                                Scdp.MsgUtil.info("上传成功");
                            }, null, null, null, a.getForm());
                        } else {
                            Scdp.MsgUtil.info("已审核合同只可以上转合同扫描附件！");
                            return;
                        }

                    } else if (contractState == "5") {

                        var fileClassify = postData.fileClassify;
                        if (fileClassify == "SCM_CONTRACT_SCAN") {
                            postData.needPersistence = "1";
                            Scdp.doAction("erp-common-file-upload", postData, function (result) {
                                if (result != "" || result != undefined) {
                                    Erp.FileUtil.initFileUploadData(fileGrid, result.fileData);
                                    //var view = fileGrid.up("IView");
                                    //view.controller.doSave();
                                    //view.controller.loadItem(view.getCmp("scmContractDto->uuid").value);
                                }
                                a.up("window").close();
                                Scdp.MsgUtil.info("上传成功");
                            }, null, null, null, a.getForm());
                        } else {
                            Scdp.MsgUtil.info("已归档合同只可以上转合同扫描附件！");
                            return;
                        }
                    }
                    else {
                        Scdp.doAction("erp-common-file-upload", postData, function (result) {
                            if (result != "" || result != undefined) {
                                Erp.FileUtil.initFileUploadData(fileGrid, result.fileData);
                                var view = fileGrid.up("IView");
                                //view.controller.doSave();
                                //view.controller.loadItem(view.getCmp("scmContractDto->uuid").value);
                            }
                            a.up("window").close();
                            Scdp.MsgUtil.info("上传成功");
                        }, null, null, null, a.getForm());
                    }

                }
            },
            cancelFunc: null
        });
        Scdp.openNewWinByView0(Ext.widget("JFileUploadView0"));
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
        var contractState = me.view.getCmp("scmContractDto->contractState").value;//合同状态
        var selection = grid.getSelectionModel().getSelection();
        if (selection) {
            var contractState = me.view.getHeader().getCmp("contractState").gotValue();
            var beforeCallback = function () {
                if (contractState == "5" || contractState == "4") {
                    for (var i = 0; i < selection.length; i++) {
                        if ("SCM_CONTRACT_SCAN" != selection[i].get("fileClassify")) {
                            Scdp.MsgUtil.warn("已经归档合同不允许删除该类型附件！");
                            return false;
                        }
                    }
                }
                return true;
            };
            var isPersistence = ("4" == contractState || "5" == contractState);

            Erp.FileUtil.erpFileDelete(grid, beforeCallback, isPersistence);
        }
    },
    beforeWorkFlowCommit: function () {
        var me = this;
        var view = me.view;
        var contractState = view.getCmp("scmContractDto->contractState").gotValue();
        if (!me.workFlowTaskId) {
            var errorMsg = me.validateEmpty();
            var supplierCode = me.view.getCmp('scmContractDto->supplierCode').gotValue();
            var postData = {supplierCode: supplierCode};
            Scdp.doAction("scmcontract-supplierNameValidate", postData, function (res) {
                if (res.msg.length > 0) {
                    errorMsg += Erp.Const.BREAK_LINE + res.msg;
                }
            }, false, false, false, null)
            if (Scdp.ObjUtil.isEmpty(errorMsg)) {
                return true;
            } else {
                Erp.Util.showLogView(Erp.I18N.BEFORE_COMMIT_FAILURE + errorMsg);
                //Scdp.MsgUtil.info("提交失败！"+errorMsg,null,600,480);
                return false;
            }
        } else {
            return true;
        }
    },
    //重写架构方法如果需要评价则不弹出提交成功
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
    },
    afterCompelteTask: function (b) {
        var role = Erp.Util.getCurrentUserRoleName();
        if (role.ROLE.indexOf("供应链部采购经理") > -1 || role.ROLE.indexOf("采购人员") > -1) {
            var me = this;
            var postData = {};
            var supplierId = me.view.getCmp("scmContractDto->supplierCode").gotValue();
            var scmContractId = me.view.getCmp("scmContractDto->uuid").gotValue();
            var amount = me.view.getCmp("scmContractDto->amount").gotValue();
            postData.supplierId = supplierId;
            Scdp.doAction("scmcontract-iswaitingforevaluation", postData, function (out) {
                if (out.result == "true") {
                    //M3_C17_F1_供方评价
                    var params = {items: ['市场价格', "经营商务", "综合评价"]};
                    var callback = function (result) {
                        var price = result.scoreArray[0];
                        var business = result.scoreArray[1];
                        var comprehensive = result.scoreArray[2];
                        var remark = result.suggest;
                        if (comprehensive == "0") {
                            Scdp.MsgUtil.info("综合评价不能为空！");
                            return false;
                        }
                        Scdp.doAction("supplierinfor-supplierevaluation", {
                            scmContractId: scmContractId,
                            uuid: supplierId,
                            price: price,
                            business: business,
                            comprehensive: comprehensive,
                            evaluateFrom: '1',
                            remark: remark
                        }, function (res) {
                        });
                    };
                    if (Erp.MathUtil.compare(amount, 50000.00) > 0) {
                        Scdp.FiveStarWin.show(params, callback, false, false);
                    }
                } else {
                    b.success && Scdp.MsgUtil.info(Scdp.I18N.MSG_WORKFLOW_COMPLETE_SUCCESS);
                }
            });
        } else {
            b.success && Scdp.MsgUtil.info(Scdp.I18N.MSG_WORKFLOW_COMPLETE_SUCCESS);
        }

    },
    //validateIfCommit: function () {
    //    var me = this;
    //    var view = me.view;
    //    var errorMsg = "";
    //    var contractState = view.getCmp("scmContractDto->contractState").gotValue();
    //    if (Scdp.ObjUtil.isNotEmpty(contractState)) {
    //        //if (contractState == "0" || contractState == "1") {
    //        //    errorMsg = "没有提交到专员，不能继续提交！";
    //        //}
    //    } else {
    //        errorMsg = "没有合同状态，不能提交！";
    //    }
    //    return errorMsg;
    //},
    validateEmpty: function () {
        //1.校验是否为空，供应商名称，合同数量，合同金额
        var me = this;
        var view = me.view;
        var errorMsg = "";
        var scmContractDto = view.getCmp("scmContractDto");
        var contractState = view.getCmp("contractState").gotValue();
        var supplierName = scmContractDto.getCmp("supplierName").gotValue();
        var quantity = scmContractDto.getCmp("quantity").gotValue();
        var amount = scmContractDto.getCmp("amount").gotValue();
        var contractNature = scmContractDto.getCmp("contractNature").gotValue();
        var purchaseTypes = scmContractDto.getCmp("purchaseTypes").gotValue();
        var contractPayType = scmContractDto.getCmp("contractPayType").gotValue();
        var totalValue = scmContractDto.getCmp("totalValue").gotValue();
        var payType = scmContractDto.getCmp("payType").gotValue();
        var debtDate = scmContractDto.getCmp("debtDate").gotValue();
        var eta = scmContractDto.getCmp("eta").gotValue();
        var newDate = new Date();
        newDate.setHours(0, 0, 0, 0);

        if (Scdp.ObjUtil.isEmpty(supplierName)) {
            var purchaseTypes = me.view.getCmp("scmContractDto->purchaseTypes").gotValue();
            if (Scdp.ObjUtil.isNotEmpty(purchaseTypes)) {
                if ("00" != purchaseTypes) {
                    errorMsg += Erp.Const.BREAK_LINE + "供应商名称" + Erp.I18N.CAN_NOT_EMPTY;
                }
            } else {
                errorMsg += Erp.Const.BREAK_LINE + "供应商名称" + Erp.I18N.CAN_NOT_EMPTY;
            }
        }
        if (Scdp.ObjUtil.isEmpty(quantity)) {
            errorMsg += Erp.Const.BREAK_LINE + "合同数量" + Erp.I18N.CAN_NOT_EMPTY;
        }
        if (Scdp.ObjUtil.isEmpty(amount)) {
            errorMsg += Erp.Const.BREAK_LINE + "合同金额" + Erp.I18N.CAN_NOT_EMPTY;
        }
        if (Scdp.ObjUtil.isEmpty(contractNature)) {
            errorMsg += Erp.Const.BREAK_LINE + "合同性质" + Erp.I18N.CAN_NOT_EMPTY;
        }
        if (Scdp.ObjUtil.isEmpty(payType) && Number(totalValue) > 0) {
            errorMsg += Erp.Const.BREAK_LINE + "付款方式" + Erp.I18N.CAN_NOT_EMPTY;
        }
        if (!Scdp.ObjUtil.isEmpty(debtDate)) {
            if (debtDate < newDate && Number(totalValue) > 0) {
                errorMsg += Erp.Const.BREAK_LINE + "请款日期不能早于提交时间！";
            }
        }
        if (!Scdp.ObjUtil.isEmpty(eta)) {
            if (eta < newDate) {
                errorMsg += Erp.Const.BREAK_LINE + "预计到达时间不能早于提交时间！";
            }
        }
        if (Scdp.ObjUtil.isEmpty(purchaseTypes)) {
            errorMsg += Erp.Const.BREAK_LINE + "采购方式" + Erp.I18N.CAN_NOT_EMPTY;
        } else {
            if ("00" != purchaseTypes || "5" == contractPayType) {        //如果不是零星采购则需要上传采购合同附件
                var cdmFileRelation = me.view.getCmp("cdmFileRelationDto").store.data.items;
                var existContractAttachment = false;
                if (Scdp.ObjUtil.isNotEmpty(cdmFileRelation)) {
                    for (var i = 0; i < cdmFileRelation.length; i++) {
                        if ("SCM_CONTRACT" == cdmFileRelation[i].get("fileClassify")) {
                            existContractAttachment = true;
                        }
                    }
                }
                if (!existContractAttachment) {
                    errorMsg += Erp.Const.BREAK_LINE + "缺少采购合同附件！";
                }
            }
            if ("00" == purchaseTypes) {
                if (Number(totalValue) > 0) {
                    errorMsg += Erp.Const.BREAK_LINE + "零星采购不能做预付款！";
                }
            }
        }
        //如果合同明细不为空则校验合同明细中的“实价总计合计”与“合同金额”是否相等
        var scmContractDetailItems = me.view.getCmp("scmContractDetailDto").store.data.items;
        if (Scdp.ObjUtil.isNotEmpty(scmContractDetailItems)) {
            var allTotalPriceTrue = 0;
            if (Scdp.ObjUtil.isNotEmpty(scmContractDetailItems)) {
                for (var i = 0; i < scmContractDetailItems.length; i++) {
                    allTotalPriceTrue = Erp.MathUtil.plusNumber(allTotalPriceTrue, scmContractDetailItems[i].get("totalPriceTrue"));
                }
            }
            if (allTotalPriceTrue != amount) {
                //scmContractDto.getCmp("amount").addClsWithUI("erp-grid-background-color-red");
                errorMsg += Erp.Const.BREAK_LINE + "合同明细中“实价总计合计”与“合同金额”不等！请核准合同明细信息！";

            }
        }

        return errorMsg;
    },
    //页面上的按钮显示控制
    btnShowControl: function () {
        var me = this;
        var view = me.view;
        var contractStateCmp = view.getCmp("scmContractDto->contractState");//合同状态
        var contractStateValue = contractStateCmp.value;//合同状态
        var state = view.getCmp("scmContractDto->state").value;//工作流状态
        me.view.getCmp("balance").setDisabled(true);
        if (Scdp.ObjUtil.isNotEmpty(state)) {
            if (state == "2") {
                me.view.getCmp("balance").setDisabled(false);
                me.view.getCmp("deleteBtn").setDisabled(true);
            }
        }
        //3.合同流程按钮
        me.view.getCmp("contractRevocation").setDisabled(true);

        if (Scdp.ObjUtil.isNotEmpty(state) && state == "2") {
            me.view.getCmp("contractRevocation").setDisabled(false);
        }

        //4.合同明细按钮
        me.view.getCmp("importExcelBtn").setDisabled(true);
        me.view.getCmp("importPurchaseReq").setDisabled(true);
        me.view.getCmp("clearingBalance").setDisabled(true);
        if (Scdp.ObjUtil.isNotEmpty(contractStateValue)) {
            var uistatus = me.view.getUIStatus();
            if (uistatus == Scdp.Const.UI_INFO_STATUS_NEW
                || uistatus == Scdp.Const.UI_INFO_STATUS_NULL
                || uistatus == Scdp.Const.UI_INFO_STATUS_MODIFY) {
                me.view.getCmp("importExcelBtn").setDisabled(false);
                me.view.getCmp("importPurchaseReq").setDisabled(false);
                me.view.getCmp("clearingBalance").setDisabled(false);
                me.view.getCmp("fileUpload").setDisabled(false);
                me.view.getCmp("fileDelete").setDisabled(false);
            } else {
                me.view.getCmp("importExcelBtn").setDisabled(true);
                me.view.getCmp("fileUpload").setDisabled(true);
                me.view.getCmp("fileDelete").setDisabled(true);
            }
        }

        //if (contractStateValue == "4") {
        //    me.view.getCmp("fileUpload").setDisabled(false);
        //}
    },
    //计算实价总计合计
    calculateTotalTruePrice: function () {
        var me = this;
        var scmContractDetailItems = me.view.getCmp("scmContractDetailDto").store.data.items;
        var allTotalPriceTrue = 0;
        if (Scdp.ObjUtil.isNotEmpty(scmContractDetailItems)) {
            for (var i = 0; i < scmContractDetailItems.length; i++) {
                allTotalPriceTrue = Erp.MathUtil.plusNumber(allTotalPriceTrue, scmContractDetailItems[i].get("totalPriceTrue"));
            }
        }
        me.view.getCmp("totalTrueInDetailGrid").sotValue(allTotalPriceTrue);
    },

    //在合同金额为0或者为空时，初始化合同金额为最大值
    setbudgetAndExpectedAmount: function () {
        var me = this;
        var items = me.view.getCmp("prmPurchaseReqDetailDto").store.data.items;
        var allTotalBudgetMoney = 0;
        var allTotalExpectedMoney = 0;
        if (Scdp.ObjUtil.isNotEmpty(items)) {
            for (var i = 0; i < items.length; i++) {
                allTotalBudgetMoney += Number(items[i].data.totalBudgetMoney);
                allTotalExpectedMoney += Number(items[i].data.totalExpectedMoney);
            }
        }
        me.view.getCmp("scmContractDto->budgetAmount").sotValue(allTotalBudgetMoney);
        me.view.getCmp("scmContractDto->expectedAmount").sotValue(allTotalExpectedMoney);
    },
    initAmount: function () {
        var me = this;
        var amount = me.view.getCmp("scmContractDto->amount").gotValue();
        if (Scdp.ObjUtil.isEmpty(amount) || amount == 0) {
            me.view.getCmp("scmContractDto->amount").sotValue(me.view.getCmp("scmContractDto->expectedAmount").value);
        }
    },
    initIsClosed: function () {
        var me = this;
        var isClosed = me.view.getCmp("scmContractDto->isClosed").gotValue();
        if (Scdp.ObjUtil.isEmpty(isClosed) || isClosed == 0) {
            me.view.getCmp("scmContractDto->isClosed").sotValue("0");
        }
    },
    //结算
    checkIfBalance: function (uuid, state, isClosed) {
        if (Scdp.ObjUtil.isEmpty(uuid)) {
            Scdp.MsgUtil.info("未选择数据！");
            return false;
        }
        if (Scdp.ObjUtil.isNotEmpty(isClosed)) {
            if (isClosed == "1") {
                Scdp.MsgUtil.info("该合同已经处于结算状态！");
                return false;
            }
        }
        if (Scdp.ObjUtil.isNotEmpty(state)) {
            if (state != "2") {
                Scdp.MsgUtil.info("已审核的合同才能结算！");
                return false;
            }
        }
        return true;
    },
    //每个人只可以修改自己新增的数据，除非是供应链部采购经理，供应链部采购经理可以修改采购人员新增的数据
    controlBasicInfoEditable: function () {
        var me = this;
        var cacheUserId = Scdp.CacheUtil.get(Scdp.Const.USER_ID);
        var userId = me.view.getCmp("scmContractDto->createBy").gotValue();
        var role = Erp.Util.getCurrentUserRoleName();
        var departmentCode = me.view.getCmp("scmContractDto->departmentCode").gotValue();
        if (Scdp.ObjUtil.isEmpty(departmentCode)) {
            departmentCode = "";
        }
        if (cacheUserId == userId) {
            me.view.getCmp("scmContractDto").sotEditable(true);
            me.view.getCmp("scmContractPaytypeDto").sotEditable(true);
            me.contractEditInit();
        } else if (role.ROLE.indexOf("供应链部采购经理") > -1 && departmentCode != "CSNT_GYLB") {
            me.view.getCmp("scmContractDto").sotEditable(true);
            me.view.getCmp("scmContractPaytypeDto").sotEditable(true);
            me.contractEditInit();
        } else {
            me.view.getCmp("scmContractDto").sotEditable(false);
            me.view.getCmp("scmContractDto.append").sotEditable(false);
            me.view.getCmp("scmContractPaytypeDto").sotEditable(false);
        }
    },
    fireLoadBankIdCombo: function () {
        var view = this.view;
        var scmContractForm = view.getCmp("scmContractDto");
        var supplierCode = scmContractForm.getCmp("supplierCode");
        var supplierCodeValue = supplierCode.gotValue();
        if (Scdp.ObjUtil.isNotEmpty(supplierCodeValue)) {
            scmContractForm.getCmp("bankId").reload(scmContractForm);
        }

    },
    electricCommercialStoreChange: function (a, b) {
        var me = this;
        var view = me.view;
        if (Scdp.ObjUtil.isNotEmpty(a.lastValue) && a.lastValue != a.oldValue) {
            view.getCmp("scmContractDto->supplierName").putValue(a.lastValue);
            view.getCmp("scmContractDto->payType").sotValue('1');
        }
    },
    //采购方式变更事件
    purchaseTypesChange: function () {
        var me = this;
        var view = me.view;
        var purchaseTypes = me.view.getCmp("scmContractDto->purchaseTypes").gotValue();
        var contractPayType = view.getCmp("scmContractDto->contractPayType");//合同付款方式
        var quantity = view.getCmp("scmContractDto->quantity");
        if (Scdp.ObjUtil.isNotEmpty(purchaseTypes)) {
            if ("00" == purchaseTypes) {
                quantity.sotValue("0");
                contractPayType.sotValue("");
            }
            else {
            }
        }
    },
    initValue: function () {
        var me = this;
        var view = me.view;
        //var effectiveDatefieldFrom = view.getCmp("conditionPanel->effectiveDatefieldFrom");
        //effectiveDatefieldFrom.sotValue(me.samePeriodLastYear());

        var role = Erp.Util.getCurrentUserRoleName();
        if (role.ROLE.indexOf("供应链部采购专员") > -1 || role.ROLE.indexOf("供应链部采购经理") > -1) {
            var operatorId = view.getCmp("conditionPanel->operatorId");
            //var operatorIdName = view.getCmp("conditionPanel->operatorIdName");
            //operatorIdName.putValue(Scdp.getCurrentUserId);
            operatorId.putValue(Scdp.getCurrentUserId());
        }
    },
    samePeriodLastYear: function () {
        var nowEnd = new Date();
        var years = nowEnd.getFullYear() - 1;
        var months = nowEnd.getMonth() + 1;
        var days = nowEnd.getDate();
        var lastEnd = years + "-" + months + "-" + days;
        return lastEnd;
    },
    //clickClearingBalanceBtn: function () {
    //    var me = this;
    //    var scmContractUuid = view.getCmp("scmContractDto->uuid").value;
    //    var postData = {scmContractUuid: scmContractUuid};
    //    Scdp.doAction("scmcontract-clearingbalance", postData, function (result) {
    //        Scdp.MsgUtil.info("结算合同成功！");
    //        me.loadItem(result.scmContractUuid);
    //    })
    //}
    clickClearingBalanceBtn: function () {
        var me = this;
        var view = me.view;
        var scmContractDetailGrid = view.getCmp("scmContractDetailDto");
        var count = scmContractDetailGrid.getStore().getCount();
        if (count > 0) {
            Scdp.MsgUtil.confirm("是否直接生效该合同/虚拟合同？", function (e) {
                if ("yes" == e) {
                    var sumTotalPriceTrue = view.getCmp("totalTrueInDetailGrid").gotValue();
                    var amount = view.getCmp("scmContractDto->amount").gotValue();
                    var balance = Erp.MathUtil.minusNumber(amount, sumTotalPriceTrue);//差额
                    if (balance != 0) {
                        if ((Erp.MathUtil.multiNumber(balance, 1000) % 1) != 0) {//小数位超过4位
                            var falg = false;//跳出标志，找到合适的条目跳出循环
                            for (var i = count; i > 0; i--) {
                                var scmContractDetailModel = scmContractDetailGrid.getStore().getAt(i - 1);
                                var scmContractDetailData = scmContractDetailModel.data;
                                var newUnitPriceTrue = Erp.MathUtil.plusNumber(scmContractDetailData.unitPriceTrue, Erp.MathUtil.multiNumber(balance, 100, 5));//调整后单价
                                if (scmContractDetailData.amount == 0.01) {//如果数量为0.01
                                    scmContractDetailModel.set("unitPriceTrue", newUnitPriceTrue);
                                    scmContractDetailModel.set("totalPriceTrue", Erp.MathUtil.multiNumber(newUnitPriceTrue, 0.01, 5));
                                    scmContractDetailModel.set("isRepair", 1);
                                    scmContractDetailModel.set("remark", "系统补差价");
                                    falg = true;
                                } else if ((scmContractDetailData.amount % 1) != 0) {//非刚性
                                    scmContractDetailModel.set("amount", Erp.MathUtil.minusNumber(scmContractDetailModel.get("amount"), 0.01));
                                    scmContractDetailModel.set("totalPriceTrue", Erp.MathUtil.multiNumber(scmContractDetailModel.get("unitPriceTrue"), scmContractDetailModel.get("amount"), 5));
                                    me.calculateTotalTruePrice();
                                    var temp = {};
                                    temp.materialNumber = scmContractDetailData.materialNumber;
                                    temp.materialName = scmContractDetailData.materialName;
                                    temp.model = scmContractDetailData.model;
                                    temp.amount = 0.01;
                                    temp.units = scmContractDetailData.units;
                                    temp.unitPriceTalk = scmContractDetailData.unitPriceTalk;
                                    temp.unitPriceTrue = Erp.MathUtil.plusNumber(newUnitPriceTrue);
                                    temp.totalPriceTalk = scmContractDetailData.unitPriceTalk;
                                    temp.totalPriceTrue = Erp.MathUtil.plusNumber(Erp.MathUtil.multiNumber(newUnitPriceTrue, 0.01, 5));
                                    temp.factory = scmContractDetailData.factory;
                                    temp.remark = "系统补差价";
                                    temp.isRepair = 1;
                                    //temp.seqNo = Erp.MathUtil.plusNumber(count, 1);
                                    scmContractDetailGrid.addRowItem(temp, false);
                                    falg = true;
                                }
                                if (falg) break;
                            }
                        } else {//小数位小于4位
                            var falg = false;//跳出标志，找到合适的条目跳出循环
                            for (var i = count; i > 0; i--) {
                                var scmContractDetailModel = scmContractDetailGrid.getStore().getAt(i - 1);
                                var scmContractDetailData = scmContractDetailModel.data;
                                var newUnitPriceTrue = Erp.MathUtil.plusNumber(scmContractDetailData.unitPriceTrue, balance);//调整后单价
                                if (scmContractDetailData.amount == 1) {//如果数量为1
                                    scmContractDetailModel.set("unitPriceTrue", newUnitPriceTrue);
                                    scmContractDetailModel.set("totalPriceTrue", newUnitPriceTrue);
                                    scmContractDetailModel.set("isRepair", 1);
                                    scmContractDetailModel.set("remark", "系统补差价");
                                    falg = true;
                                } else if (scmContractDetailData.amount > 1) {//数量大于1
                                    scmContractDetailModel.set("amount", Erp.MathUtil.minusNumber(scmContractDetailModel.get("amount"), 1));
                                    scmContractDetailModel.set("totalPriceTrue", Erp.MathUtil.multiNumber(scmContractDetailModel.get("unitPriceTrue"), scmContractDetailModel.get("amount"), 5));
                                    me.calculateTotalTruePrice();
                                    var temp = {};
                                    temp.materialNumber = scmContractDetailData.materialNumber;
                                    temp.materialName = scmContractDetailData.materialName;
                                    temp.model = scmContractDetailData.model;
                                    temp.amount = 1;
                                    temp.units = scmContractDetailData.units;
                                    temp.unitPriceTalk = scmContractDetailData.unitPriceTalk;
                                    temp.unitPriceTrue = Erp.MathUtil.plusNumber(newUnitPriceTrue);
                                    temp.totalPriceTalk = scmContractDetailData.unitPriceTalk;
                                    temp.totalPriceTrue = Erp.MathUtil.plusNumber(newUnitPriceTrue);
                                    temp.factory = scmContractDetailData.factory;
                                    temp.remark = "系统补差价";
                                    temp.isRepair = 1;
                                    //temp.seqNo = Erp.MathUtil.plusNumber(count, 1);
                                    scmContractDetailGrid.addRowItem(temp, false);
                                    falg = true;
                                }
                                if (falg) break;
                            }
                        }
                        me.calculateTotalTruePrice();
                    }
                }
            });
        } else {
            Scdp.MsgUtil.info("至少要有一项明细项！");
        }
    },
    initReportIframe: function () {
        var me = this;
        var view = me.view;
        var reportJPanel = view.getCmp("scmContractReport");
        reportJPanel.add({
            xtype: 'JPanel',
            cid: 'resultPanel',
            margin: '2 4 2 3 ',
            flex: 1,
            html: '<iframe id="scmContractReportIframe" name="scmContractReportIframe" frameborder="0" style="width: 100% ; height: 100%;background-color: #FFFFFF" src=""></iframe>'
        });
    },
    supplierNameChange: function () {
        var me = this;
        var supplierCode = me.view.getCmp('scmContractDto->supplierCode').gotValue();
        var postData = {supplierCode: supplierCode};
        Scdp.doAction("scmcontract-supplierNameValidate", postData, function (res) {
            if (res.msg.length > 0) {
                return res.msg;
            } else {
                return null;
            }
        }, false, false, false, null);
    },
    initReportForm: function () {
        var me = this;
        var view = me.view;
        //var reportJPanel = view.getCmp("scmContractReport");
        var scmContractId = view.getCmp("scmContractDto->uuid").gotValue();
        var url = Scdp.getSysConfig("base_path") + Scdp.getSysConfig("report_servlet") + '?reportlet=erp/scm/ScmContractSingleQuery.cpt&scmContractId=' + scmContractId;
        var fd = Ext.get('scmContractReportForm');
        if (!fd) {
            fd = Ext.DomHelper.append(
                Ext.getBody(), {
                    tag: 'form',
                    method: 'post',
                    id: 'scmContractReportForm',
                    action: url,
                    target: 'scmContractReportIframe',
                    cls: 'x-hidden',
                    cn: []
                }, true);

        } else {
            $(fd.dom).attr("action", url);
        }
        fd.dom.submit();
    }
})
;