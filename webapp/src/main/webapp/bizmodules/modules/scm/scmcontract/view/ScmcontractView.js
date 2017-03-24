Ext.define('Scmcontract.view.ScmcontractView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/scm/scmcontract',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 125,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'scmcontract-query-layout.xml',
    editLayoutFile: 'scmcontract-edit-layout.xml',
    //extraLayoutFile: 'scmcontract-extra-layout.xml',
    bindings: ['scmContractDto', 'scmContractDto.scmContractPaytypeDto', 'scmContractDto.prmPurchaseReqDetailDto', 'scmContractDto.scmContractDetailDto', 'scmContractDto.cdmFileRelationDto', 'scmContractDto.scmContractChangeDto'],
    // bindings: ['scmContractDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    withSelMode: false,
    workFlowDefinitionKey: 'Scm_Contract_Prepare',
    initComponent: function () {
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        me.initView();
        me.initScmContractGrid();
        me.initScmContractDetailGrid();
        me.prmPurchaseReqDetailGridInit(me);
        me.getCmp("scmContractDto->fadSubjectCode").inputEl.on('click', me.gotoPrmProject);
        me.getCmp("scmContractDto->supplierName").inputEl.on('click', me.gotoSupplier);
        //me.initReportIframe();
    },
    gotoPrmProject: function () {
        var me = this;
        var prmProjectIdPlusId = me.id.substr(0, me.id.lastIndexOf("-inputEl"));
        var prmProjectId = Ext.getCmp(prmProjectIdPlusId).up("IView").getCmp("scmContractDto->projectId").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(prmProjectId)) {
            var param = {uuid: prmProjectId};
            var menuCode = 'PROJECT_MAIN_PLAN';
            Scdp.openNewModuleByMenuCode(menuCode, {erp_msg_business_obj: param}, "MENU_ITEM_CTL", true);
        }
    },
    gotoSupplier: function () {
        var me = this;
        var supplierIdPlusId = me.id.substr(0, me.id.lastIndexOf("-inputEl"));
        var supplierId = Ext.getCmp(supplierIdPlusId).up("IView").getCmp("scmContractDto->supplierCode").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(supplierId)) {
            var param = {uuid: supplierId};
            var menuCode = 'SUPPLIERINFOR';
            Scdp.openNewModuleByMenuCode(menuCode, {erp_msg_business_obj: param}, "MENU_ITEM_CTL", true);
        }
    },
    initScmContractGrid: function () {
        var view = this;
        var scmContractForm = view.getCmp("scmContractDto");
        var supplierCode = scmContractForm.getCmp("supplierCode");
        supplierCode.afterSotValue = function () {
            var supplierCodeValue = supplierCode.gotValue();
            var electricCommercialStoreCmp = view.getCmp("scmContractDto.append->electricCommercialStore");
            var officeCode = view.getCmp("scmContractDto->officeId").gotValue();
            electricCommercialStoreCmp.putValue();
            if (Scdp.ObjUtil.isNotEmpty(supplierCodeValue)) {
                var postData = {supplierCode: supplierCodeValue};
                Scdp.doAction("scmcontract-querysupplierbankinfo", postData, function (result) {
                    if (Scdp.ObjUtil.isNotEmpty(result)) {
                        scmContractForm.getCmp("bankId").reload(scmContractForm);
                        scmContractForm.getCmp("bankId").sotValue(result.bankId);
                        scmContractForm.getCmp("bank").sotValue(result.bankName);
                    }
                });
                electricCommercialStoreCmp.filterConditions = {selfconditions: " office_id = '" + officeCode + "' and  t1.scm_supplier_id = '" + supplierCodeValue + "'"};
            } else {
                //electricCommercialStoreCmp.filterConditions = {selfconditions: " office_id = '" + officeCode + "'"};
                scmContractForm.getCmp("bankId").sotValue("");
                scmContractForm.getCmp("bank").sotValue("");
            }
        }
    },
    initScmContractDetailGrid: function () {
        var me = this;
        var detailGrid = me.getCmp("scmContractDetailDto");
        var scmContractPayTypeGrid = me.getCmp("scmContractPaytypeDto");
        ////隐藏上移，下移，上移到顶部，下移到底部按钮
        //detailGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
        //detailGrid.getCmp("toolbar->rowMoveTopBtn").hide();
        //detailGrid.getCmp("toolbar->rowMoveUpBtn").hide();
        //detailGrid.getCmp("toolbar->rowMoveDownBtn").hide();
        scmContractPayTypeGrid.afterEditGrid = me.changeScmContractPayTypeGrid;
        scmContractPayTypeGrid.beforeDeleteRow = me.deleteScmContractPayTypeGrid;
        detailGrid.afterEditGrid = me.changeScmContractDetail;
        detailGrid.afterDeleteRow = me.deleteScmContractDetailGrid;
        //scmContractPayTypeGrid.afterDeleteRow = me.function (a,b) {
        //    var  contractInvoiceController = Scdp.getActiveModule().controller;
        //    contractInvoiceController.afterDeleteRow();
        //    view.getCmp("scmContractDto->totalValue").sotValue(record.get("actuallyPaid"));
        //};
    },
    changeScmContractPayTypeGrid: function (eventObj, isChanged) {
        //var me = this;
        if (!isChanged) {
            return;
        }
        var field = eventObj.field;
        var record = eventObj.record;
        var view = this.up("IView");
        var amount = view.getCmp("scmContractDto->amount").value;
        if (field == "value") {
            record.set("actuallyPaid", record.get("value") * amount / 100);
        } else if (field == "actuallyPaid") {
            record.set("value", record.get("actuallyPaid") * 100 / amount);
        }
        if (record.data.title == "预付款") {
            view.getCmp("scmContractDto->totalValue").sotValue(record.get("actuallyPaid"));
        }
    },
    deleteScmContractPayTypeGrid: function (a) {
        var me = this;
        var view = me.up("IView");
        if (a[0].data.title == "预付款") {
            view.getCmp("scmContractDto->totalValue").sotValue(0.00);
        }

    },
    changeScmContractDetail: function (eventObj, isChanged) {
        if (!isChanged) {
            return;
        }
        var field = eventObj.field;
        var record = eventObj.record;
        var allTotalPriceTrue = 0;
        if (field == "unitPriceTalk") {
            record.set("totalPriceTalk", record.get("unitPriceTalk") * record.get("amount"));
        } else if (field == "unitPriceTrue") {

            record.set("totalPriceTrue", record.get("unitPriceTrue") * record.get("amount"));
            var tempItems = eventObj.store.data.items;
            if (Scdp.ObjUtil.isNotEmpty(tempItems)) {
                for (var i = 0; i < tempItems.length; i++) {
                    allTotalPriceTrue = Number(tempItems[i].data.totalPriceTrue) + Number(allTotalPriceTrue);
                }
                eventObj.grid.up("IView").getCmp("totalTrueInDetailGrid").sotValue(allTotalPriceTrue);
            }
        } else if (field == "amount") {
            record.set("totalPriceTrue", record.get("unitPriceTrue") * record.get("amount"));
            record.set("totalPriceTalk", record.get("unitPriceTalk") * record.get("amount"));
            var tempItems = eventObj.store.data.items;
            if (Scdp.ObjUtil.isNotEmpty(tempItems)) {
                for (var i = 0; i < tempItems.length; i++) {
                    allTotalPriceTrue = Number(tempItems[i].data.totalPriceTrue) + Number(allTotalPriceTrue);
                }
                eventObj.grid.up("IView").getCmp("totalTrueInDetailGrid").sotValue(allTotalPriceTrue);
            }
        }
    },
    deleteScmContractDetailGrid: function () {
        var me = this;
        var scmContractDetailItems = me.store.data.items;
        var allTotalPriceTrue = 0;
        if (Scdp.ObjUtil.isNotEmpty(scmContractDetailItems)) {
            for (var i = 0; i < scmContractDetailItems.length; i++) {
                allTotalPriceTrue = Erp.MathUtil.plusNumber(allTotalPriceTrue, scmContractDetailItems[i].get("totalPriceTrue"));
            }
        }
        me.up("JPanel").getCmp("totalTrueInDetailGrid").sotValue(allTotalPriceTrue);
    },
    initView: function () {
        var me = this;
        //1.去除新增，复制新增，删除按钮,批量删除按钮
        me.getCmp("addNew2Btn").setVisible(false);
        me.getCmp("addNew1Btn").setVisible(false);
        me.getCmp("copyAddBtn").setVisible(false);
        me.getCmp("deleteBtn").setVisible(false);
        //1.添加编辑区的工具按钮
        var editToolbar = me.getEditToolbar();
        editToolbar.add({
            text: '合同废止',
            cid: 'contractRevocation',
            iconCls: 'temp_icon_16',
            disabled: "true"
        });
        editToolbar.add({
            text: '结算',
            cid: 'balance',
            iconCls: 'temp_icon_16',
            disabled: "true"
        });
        //2.添加查询区的工具按钮
        var queryToolbar = me.getQueryToolbar();
        queryToolbar.add({
            text: '结算',
            cid: 'balanceQuery',
            iconCls: 'temp_icon_16'
            //disabled: "true"
        });
        var scmContractPaytypeGrid = me.getCmp("scmContractPaytypeDto");

        scmContractPaytypeGrid.getCmp("toolbar->rowMoveTopBtn").hide();
        scmContractPaytypeGrid.getCmp("toolbar->rowMoveUpBtn").hide();
        scmContractPaytypeGrid.getCmp("toolbar->rowMoveDownBtn").hide();
        scmContractPaytypeGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
        me.getCmp("contractRevocation").setVisible(false);
        //me.getCmp("contractRevocationQuery").setVisible(false);
        me.getCmp("balance").setVisible(false);
        me.getCmp("balanceQuery").setVisible(false);
        //根据角色判断按钮显示
        var role = Erp.Util.getCurrentUserRoleName();
        if (role.ROLE.indexOf("供应链部采购经理") > -1) {
            //me.getCmp("directEffectQuery").setVisible(true);
            //me.getCmp("directEffectEdit").setVisible(true);
            me.getCmp("contractRevocation").setVisible(true);
            //me.getCmp("contractRevocationQuery").setVisible(true);
            me.getCmp("balance").setVisible(true);
            me.getCmp("balanceQuery").setVisible(true);

        }
        if (role.ROLE.indexOf("供应链部主任") > -1) {
            me.getCmp("balance").setVisible(true);
            me.getCmp("balanceQuery").setVisible(true);
        }
        if (Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_NAME) == "计划财务部") {
            me.getCmp("jdInfo->jdPassword").inputEl.dom.type = "text";
        }
        var supplierNameCmp = me.getCmp("scmContractDto->supplierName");
        supplierNameCmp.filterConditions = {selfconditions: " supplier_Genre in ('0','1') "};
        //var electricCommercialStoreCmp = me.getCmp("scmContractDto.append->electricCommercialStore");
        //electricCommercialStoreCmp.filterConditions = {selfconditions: " office_id = '" + Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE) + "'"};
    },

    prmPurchaseReqDetailGridInit: function (view) {
        openPurchaseReq = function (uuid, isProject) {
            var postData = {};
            postData.wf_businessKey = uuid;
            var menuCode = "";
            if (isProject == "1") {//项目采购申请
                menuCode = 'PRMPURCHASEREQ';
            } else {//非项目采购申请
                menuCode = 'NONPRMPURCHASEREQ';
            }
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        };
        openScmContractChange = function (uuid, isProject) {
            var postData = {};
            postData.wf_businessKey = uuid;
            var menuCode = "SCMCONTRACTCHANGE";
            Scdp.openNewModuleByMenuCode(menuCode, postData, "MENU_ITEM_CTL", true);
        };
        var prmPurchaseReqDetailGrid = view.getCmp("prmPurchaseReqDetailDto");
        var purchaseReqNoColumns = prmPurchaseReqDetailGrid.getColumnBydataIndex("purchaseReqNo");
        purchaseReqNoColumns.renderer = function (value, p, record) {
            return Ext.String.format(
                '<a href="javascript:openPurchaseReq(\'' + record.data.prmPurchaseReqId + '\',\'' + record.data.isProject + '\');" target="_blank" style="color: blue">' + value + '</a>'
            );
        };
        var scmContractChangeGrid = view.getCmp("scmContractChangeDto");
        var runningNoColumns = scmContractChangeGrid.getColumnBydataIndex("runningNo");
        runningNoColumns.renderer = function (value, p, record) {
            return Ext.String.format(
                '<a href="javascript:openScmContractChange(\'' + record.data.uuid + '\');" target="_blank" style="color: blue">' + value + '</a>'
            );
        };
    },
    UIStatusChanged: function (view, uistatus) {

        if (!(uistatus == Scdp.Const.UI_INFO_STATUS_VIEW || uistatus == Scdp.Const.UI_INFO_STATUS_MODIFY)) {
            //clear
            var frame = window.frames["scmContractReportIframe"];
            if (Scdp.ObjUtil.isNotEmpty(frame)) {
                frame.document.body.innerHTML = "";
            }
        }

    },
    initReportIframe: function () {
        //var me = this;
        var view = this;
        var reportJPanel = view.getCmp("scmContractReport");
        reportJPanel.add({
            xtype: 'JPanel',
            cid: 'resultPanel',
            margin: '2 4 2 3 ',
            flex: 1,
            html: '<iframe id="scmContractReportIframe" name="scmContractReportIframe" frameborder="0" style="width: 100% ; height: 100%;background-color: #FFFFFF" src=""></iframe>'
        });
    }
});