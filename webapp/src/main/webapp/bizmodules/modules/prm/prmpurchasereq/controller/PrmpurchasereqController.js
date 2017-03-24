var tmpNodes;
Ext.define("Prmpurchasereq.controller.PrmpurchasereqController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Prmpurchasereq.view.PrmpurchasereqView',
    uniqueValidateFields: [],
    extraEvents: [
//        {cid: 'submitEffect', name: 'click', fn: 'clickSubmitEffect'},
//        {cid: 'submitEffect_query', name: 'click', fn: 'clickSubmitEffect'},
//        {cid: 'auditEffect', name: 'click', fn: 'clickAuditEffect'},
//        {cid: 'auditEffect_query', name: 'click', fn: 'clickAuditEffect'},

        {cid: 'fileUpload', name: 'click', fn: 'fileUploadBtn'},
        {cid: 'fileDownload', name: 'click', fn: 'fileDownloadBtn'},
        {cid: 'fileDelete', name: 'click', fn: 'fileDeleteBtn'},
        {cid: 'prmPurchaseReqDetailDto', name: 'edit', fn: 'checkAmountFn'},
        {cid: 'subUpload', name: 'click', fn: 'subUploadBtn'},
        {cid: 'subDownload', name: 'click', fn: 'subDownloadBtn'},
        {cid: 'filePreview', name: 'click', fn: 'filePreviewBtn'},
        {cid: 'subDelfile', name: 'click', fn: 'subDelfileBtn'},
        {cid: 'prmPurchaseReqDto->isInner', name: 'change', fn: 'setInnerSupplierFn'},
        {cid: 'innerSupplier', name: 'expand', fn: 'expandInnerSupplierFn'},
        {cid: 'projectName', name: 'blur', fn: 'checkInnerSupplierFn'},
        {cid: 'prmProjectMainId', name: 'change', fn: 'changeProjectName'},
        {cid: 'abolishBtn', name: 'click', fn: 'abolishBtnFn'},
        {cid: 'signProject', name: 'click', fn: 'signProjectBtnFn'},
        {cid: 'abolishSign', name: 'click', fn: 'abolishSignBtnFn'}

    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.prmpurchasereq.dto.PrmPurchaseReqDto',
    queryAction: 'prmpurchasereq-query',
    loadAction: 'prmpurchasereq-load',
    addAction: 'prmpurchasereq-add',
    modifyAction: 'prmpurchasereq-modify',
    deleteAction: 'prmpurchasereq-delete',
    exportXlsAction: "prmpurchasereq-exportxls",
    //所属项目改变，包清空
    changeProjectName: function () {
        var me = this;
        me.view.getCmp("prmPurchaseReqDetailDto").clearData();
    },
    //M3_C5_F2_页面调整
    pickChasereqPack: function () {
        var me = this;
        var projectName = me.view.getCmp("projectName").getValue();
        var reqUuids = new Array();
        var reqGridData = me.view.getCmp("prmPurchaseReqDetailDto").gotValue();
        for (var i = 0; i < reqGridData.length; i++) {
            reqUuids.push(reqGridData[i].prmPurchasePlanDetailId);
        }

        if (Scdp.ObjUtil.isNotEmpty(projectName)) {
            var prmProjectMainId = me.view.getCmp("prmProjectMainId").gotValue();
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
                reqUuids: reqUuids
            };
            queryController.actionParams = param;
            Scdp.openNewWinByController(queryController, callBack, 'temp_icon_16', "采购明细选择");
        } else {
            Scdp.MsgUtil.info("请选择所属项目！");
        }
    },
    afterInit: function () {
        var me = this;
        me.callParent(arguments);
        me.view.getResultPanel().down().grid.rowColorConfigFn = me.rowColorConfigFn;
        me.view.getCmp("prmPurchaseReqDetailDto").afterEditGrid = me.checkAmountFn;
        me.view.getCmp("prmPurchaseReqDetailDto").afterDeleteRow = function () {
            me.setTotalMoney();
        };
        me.view.getCmp("fileUpload").setDisabled(true);
        me.view.getCmp("fileDelete").setDisabled(true);
    },
    beforeAdd: function () {
        var me = this;
        var view = me.view;
        return true;
    },
    afterAdd: function () {
        var me = this;
        var view = me.view;
        me.view.getCmp("subUpload").setDisabled(false);
        me.view.getCmp("subDelfile").setDisabled(false);
        //设置添加之后按钮的状态
        me.setSignProjectBtnState();
        me.view.getCmp("projectName").focus();
        me.setInnerSupplierFn();
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        me.view.getCmp("purchaseReqNo").sotValue("");
        me.setInnerSupplierFn();
        return true;
    },
    beforeModify: function () {
        var me = this;
        var view = me.view;
        me.view.getCmp("projectName").setReadOnly(true);
        me.setTotalMoney();
        return true;
    },
    afterModify: function () {
        var me = this;
        var view = me.view;
        me.view.getCmp("fileUpload").setDisabled(false);
        me.view.getCmp("fileDelete").setDisabled(false);

        me.setSignProjectBtnState();

        me.setAddressForm();
        me.setInnerSupplierFn();
        //点修改按钮设置按钮
        return true;
    },
    beforeSave: function () {
        var me = this;
        var detailItems = me.view.getCmp('prmPurchaseReqDetailDto').getStore().data.items;
        if (detailItems.length < 1) {
            Scdp.MsgUtil.info("请选择一条记录！");
            return false;
        }
        for (var i = 0; i < detailItems.length; i++) {
            if (detailItems[i].get("amount") > detailItems[i].get("planAmount")) {
                Scdp.MsgUtil.info("申请数量超过最大值！");
                return false;
            }
            if (detailItems[i].get("expectedPrice") > detailItems[i].get("budgetPrice")) {
                Scdp.MsgUtil.info("意向单价不能超过预算单价！");
                return false;
            }
//            if ((detailItems[i].get("amount") * detailItems[i].get("expectedPrice")) > detailItems[i].get("purchaseBudgetMoney")) {
//                Scdp.MsgUtil.info("申请预算超过最大值！");
//                return false;
//            }
            detailItems[i].set("needUpdate", "1");
            detailItems[i].set("chkAmount", detailItems[i].get("amount"));
            detailItems[i].set("chkBudgetPrice", detailItems[i].get("expectedPrice"));
            detailItems[i].set("chkPackageId", detailItems[i].get("purchasePackageId"));
            detailItems[i].set("chkPackageName", detailItems[i].get("packageName"));
            detailItems[i].set("handleAmount", detailItems[i].get("amount"));
        }
        //me.setAddressForm();

        me.view.getCmp("prmPurchaseReqDto->chkPrmProjectMainId").sotValue(me.view.getCmp("prmPurchaseReqDto->prmProjectMainId").gotValue(""));
        me.view.getCmp("prmPurchaseReqDto->chkPrmPurchaseReqId").sotValue(me.view.getCmp("prmPurchaseReqDto->uuid").gotValue(""));

        //var prmPurchaseReqDetailStore = me.view.getCmp('prmPurchaseReqDetailDto').getStore();
        //if (prmPurchaseReqDetailStore && prmPurchaseReqDetailStore.getCount() > 0) {
        //    for (var i = 0; i < prmPurchaseReqDetailStore.data.items.length; i++) {
        //        var subItem = prmPurchaseReqDetailStore.data.items[i];
        //
        //        subItem.data.consigee = me.view.getCmp("prmPurchaseReqDetailDtoAddress->consignee").gotValue();
        //        prmPurchaseReqDetailStore.data.items[i].arriveLocation = me.view.getCmp("prmPurchaseReqDetailDtoAddress->arriveLocation").gotValue();
        //        prmPurchaseReqDetailStore.data.items[i].remark = me.view.getCmp("prmPurchaseReqDetailDtoAddress->remark").gotValue();
        //        prmPurchaseReqDetailStore.data.items[i].contactWay = me.view.getCmp("prmPurchaseReqDetailDtoAddress->contactWay").gotValue();
        //
        //        me.view.getCmp("prmPurchaseReqDetailDto").sotValue(subItem.data);
        //
        //        var subItem = prmPurchaseReqDetailStore.data.items[i];
        //        if (uuid == subItem.data.uuid) {
        //            me.view.getCmp('scmPurchaseReqDto').getView().focusRow(i);
        //            me.view.getCmp('scmPurchaseReqDto').getSelectionModel().select(i);
        //            me.view.getCmp('prmPurchaseReqDetailDto').sotValue(subItem.data.prmPurchaseReqDetailDto);
        //        }
        //    }
        //}
        return true;
    },
    afterSave: function (retData) {
        var me = this;
        me.callParent(arguments);
//        me.view.getCmp("departmentCode").setDisabled(false);
        me.view.getCmp("projectName").setReadOnly(true);
        me.view.getCmp("fileUpload").setDisabled(true);
        me.view.getCmp("fileDelete").setDisabled(true);

        me.view.getCmp("subUpload").setDisabled(true);
        me.view.getCmp("subDelfile").setDisabled(true);

    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        me.callParent(arguments);
        me.setAddressForm();
        me.setTotalMoney();
        me.setAbolishBtnState();
        me.setSignProjectBtnState();
    },
    beforeCancel: function () {
        var me = this;
        me.view.getCmp("projectName").setReadOnly(false);
        return true;
    },
    afterCancel: function () {
        var me = this;
        me.callParent(arguments);
        me.view.getCmp("fileUpload").setDisabled(true);
        me.view.getCmp("fileDelete").setDisabled(true);

        me.view.getCmp("subUpload").setDisabled(true);
        me.view.getCmp("subDelfile").setDisabled(true);
        me.setAddressForm();
        me.setTotalMoney();
    },
    beforeDelete: function () {
        var me = this;
        return true;
    },
    afterDelete: function () {
        var me = this;
        me.setSignProjectBtnState();
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
        var errorMsg = "";
        var detailItems = me.view.getCmp('prmPurchaseReqDetailDto').getStore().data.items;
        for (var i = 0; i < detailItems.length; i++) {
            detailItems[i].set("needUpdate", "1");
            detailItems[i].set("chkAmount", detailItems[i].get("amount"));
            detailItems[i].set("chkBudgetPrice", detailItems[i].get("expectedPrice"));
            detailItems[i].set("chkPackageId", detailItems[i].get("purchasePackageId"));
            detailItems[i].set("chkPackageName", detailItems[i].get("packageName"));
        }
        me.view.getCmp("prmPurchaseReqDto->chkPrmProjectMainId").sotValue(me.view.getCmp("prmPurchaseReqDto->prmProjectMainId").gotValue(""));
        me.view.getCmp("prmPurchaseReqDto->chkPrmPurchaseReqId").sotValue(me.view.getCmp("prmPurchaseReqDto->uuid").gotValue(""));
        if (me.view.getCmp("prmPurchaseReqDto->isInner").gotValue() == '1') {
            var purchasePackageId = "";
            var isSamePurchasePackage = true;
            for (var i = 0; i < detailItems.length; i++) {
                if (Scdp.ObjUtil.isEmpty(purchasePackageId)) {
                    purchasePackageId = detailItems[i].get("purchasePackageId");
                } else if (purchasePackageId != detailItems[i].get("purchasePackageId")) {
                    isSamePurchasePackage = false;
                }
            }
            if (!isSamePurchasePackage) {
                errorMsg += "内委项目采购申请单明细不能存在多个采购包！" + Erp.Const.BREAK_LINE;
            }
        }

        var uuid = me.view.getCmp("prmPurchaseReqDto->uuid").gotValue();
        var prmProjectMainId = me.view.getCmp("prmPurchaseReqDto->prmProjectMainId").gotValue();
        //获取内委状态
        var isInner = me.view.getCmp("prmPurchaseReqDto->isInner").gotValue();
        var enableNull = me.workFlowFormData.indexOf("wf_isStamp=1");
        if (enableNull != "-1") {
            for (var i = 0; i < detailItems.length; i++) {
                var isStamp = detailItems[i].get("isStamp");
                var stampProjectUuid = detailItems[i].get("stampProjectUuid");
                if (Scdp.ObjUtil.isNotEmpty(isStamp) && Scdp.ObjUtil.isEmpty(stampProjectUuid)) {
                    errorMsg += "内委项目采购申请单明细课题编号不能为空" + Erp.Const.BREAK_LINE;
                    break;
                }
            }
        }
        if (Scdp.ObjUtil.isNotEmpty(prmProjectMainId)) {
            var postData = {};
            postData.uuid = uuid;
            postData.prmProjectMainId = prmProjectMainId;
            postData.isInner = isInner;
            Scdp.doAction("purchasereq-validate-before-submit", postData, function (result) {
                if (result.check == false) {
                    errorMsg += "所选内委项目采购申请单明细标识不一致！" + Erp.Const.BREAK_LINE;

                }

                if (!result.result) {
                    errorMsg += "项目经理缺失，不能提交" + Erp.Const.BREAK_LINE;
                }

                if (Scdp.ObjUtil.isEmpty(errorMsg)) {
                    me.executeTask();
                } else {
                    Erp.Util.showLogView(Erp.I18N.BEFORE_COMMIT_FAILURE + errorMsg);
                    return false;
                }
            }, false);
            return false;
        }
        return true;
    },
    //1.点击提交按钮
    clickSubmitEffect: function () {
        var me = this;
        var selectedDetails = me.view.getResultPanel().getSelectionModel().getSelection();
        if (selectedDetails == null || selectedDetails.length == 0) {
            Scdp.MsgUtil.info("未选择数据");
            return false;
        }

        var uuid;
        var uuidlst = [];
        for (var i = 0; i < selectedDetails.length; i++) {
            uuid = selectedDetails[i].data.uuid;
            if (uuid == "") {
                Scdp.MsgUtil.info("未选择数据");
                return false;
            }
            uuidlst.push(uuid);
        }
        var postData = {uuidlst: uuidlst};
        Scdp.doAction("prmpurchasereq-submit", postData, function () {
            //me.view.getCmp("prmPurchaseReqDto->state").sotValue("2");
            me.doQuery();
            Scdp.MsgUtil.info("提交成功");
        })
    },
    //1.点击提交按钮
    clickAuditEffect: function () {
        var me = this;
        var selectedDetails = me.view.getResultPanel().getSelectionModel().getSelection();
        if (selectedDetails == null || selectedDetails.length == 0) {
            Scdp.MsgUtil.info("未选择数据");
            return false;
        }

        var uuid;
        var uuidlst = [];
        for (var i = 0; i < selectedDetails.length; i++) {
            uuid = selectedDetails[i].data.uuid;
            if (uuid == "") {
                Scdp.MsgUtil.info("未选择数据");
                return false;
            }
            uuidlst.push(uuid);
        }
        var postData = {uuidlst: uuidlst};
        Scdp.doAction("prmpurchasereq-audit", postData, function () {
            //me.view.getCmp("prmPurchaseReqDto->state").sotValue("2");
            me.doQuery();
            Scdp.MsgUtil.info("审核成功");
        })
    },
    //补充数据并检查状态
    //M3_C5_F2_页面调整
    clickAddPlanBtn: function (planDetails) {
        //stone
        var me = this;
        var reqGrid = me.view.getCmp("prmPurchaseReqDetailDto");
        for (var i = 0; i < planDetails.length; i++) {
            planDetails[i].prmPurchasePlanDetailId = planDetails[i].uuid;
            planDetails[i].planAmount = planDetails[i].remainAmount;
            planDetails[i].amount = planDetails[i].remainAmount;
            planDetails[i].expectedPrice = planDetails[i].budgetPrice;
            planDetails[i].subTotal = Erp.MathUtil.multiNumber(planDetails[i].amount, planDetails[i].expectedPrice);

        }
        // 校验明细数据的采购级别是否一致
        var purchaseLevel = me.view.getCmp("purchaseLevel").gotValue();
        if (reqGrid.store.data.items.length == 0) {
            purchaseLevel = "-NA";
        }
        for (var i = 0; i < planDetails.length; i++) {
            if (purchaseLevel == null || purchaseLevel == '-NA') {
                purchaseLevel = planDetails[i].purchaseLevel;
                me.view.getCmp("purchaseLevel").sotValue(purchaseLevel);
            }
            if (planDetails[i].purchaseLevel != purchaseLevel) {
                Scdp.MsgUtil.info("不能加入不同采购级别的记录！");
                return;
            }
        }
        reqGrid.addRowItems(planDetails, false);
        me.setTotalMoney();
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
    //文件上传
    subUploadBtn: function () {
        var me = this;
        var grid = me.view.getCmp("prmPurchaseReqDetailDto");
        var fileClassify = "";
        Erp.FileUtil.erpFileUpload(grid, fileClassify, this.initSubFileUploadData);
    },
    //文件下载
    subDownloadBtn: function () {
        var me = this;
        var grid = me.view.getCmp("prmPurchaseReqDetailDto");
        me.erpSubFileDownLoad(grid);
    },
    //文件删除
    subDelfileBtn: function () {
        var me = this;
        var grid = me.view.getCmp("prmPurchaseReqDetailDto");
        me.erpSubFileDelete(grid);
    },
    //根据回传的数据，在页面上显示相关信息
    initSubFileUploadData: function (fileGrid, obj) {
        var selectRecord = fileGrid.getSelectionModel().getSelection();

        if (selectRecord.length > 0) {
            selectRecord[0].set("technicalDrawing", obj.uuid);
            selectRecord[0].set("isUploaded", "有");
        }

//        if (obj != null) {
//            obj.fileId = obj.uuid;
//            obj.module = Scdp.getActiveModule().controller.menuCode;
//            if (obj.fileClassify == "1" || obj.fileClassify == "2") {
//                obj.cdmFileType = "SCM_CONTRACT_OTHER";
//            } else if (obj.fileClassify == "3") {
//                obj.cdmFileType = "SCM_CONTRACT_SCAN";
//            } else {
//                obj.cdmFileType = "";
//            }
//            delete obj["uuid"];
//            fileGrid.addRowItem(obj, false);
//        }
    },
    erpSubFileDownLoad: function (fileGrid) {
        var selectRecord = fileGrid.getSelectionModel().getSelection();
        if (selectRecord.length > 0) {
            var fileId = selectRecord[0].data.technicalDrawing;
            if (fileId != null && fileId != "") {
                var postdata = {};
                var fileList = [];
                fileList.push(fileId);
                postdata.fileList = fileList;
                var ret = Scdp.doAction("erp-common-file-download", postdata, null, null, false, false);
                var urlList = ret.URL_LIST;
                Ext.each(urlList, function (item) {
                    window.open(item);
                })
            } else {
                Scdp.MsgUtil.info("没有附件");
            }
        } else {
            Scdp.MsgUtil.info("未选择记录");
        }
    },
    erpSubFileDelete: function (fileGrid) {
        var c = fileGrid.getStore();
        var b = fileGrid.getSelectionModel().getSelection();
        if (b.length > 0) {
            b[0].set("technicalDrawing", "");
            b[0].set("isUploaded", "");
        } else {
            Scdp.MsgUtil.info("未选择记录");
        }
    },
    //行颜色设置
    rowColorConfigFn: function (record) {
        if (record.data.state == "0") {
            return null;
        } else if (record.data.state == "1") {
            return 'erp-grid-font-color-green';
        } else if (record.data.state == "5") {
            return 'erp-grid-font-color-red';
        }
        return null;
    },
    setInnerSupplierFn: function () {
        var me = this;
        var innerSupplier = me.view.getCmp("prmPurchaseReqDto->innerSupplier")
        if (me.view.getCmp("prmPurchaseReqDto->isInner").checked == false) {
            innerSupplier.sotEditable(false);
            innerSupplier.allowBlank = true;
            innerSupplier.sotValue("");
        } else {
            innerSupplier.allowBlank = false;
            innerSupplier.sotEditable(true);
        }
    },
    //M3_C5_F1_到货地址
    checkInnerSupplierFn: function () {
        var me = this;
        var cmpInnerSupplier = me.view.getCmp("prmPurchaseReqDto->innerSupplier");
        if (me.view.getCmp("prmPurchaseReqDto->officeId").gotValue() == cmpInnerSupplier.gotValue()) {
            cmpInnerSupplier.sotValue("");
        }
        else {
            var officeIdMap = {};
            officeIdMap.prmProjectMainId = me.view.getCmp("prmProjectMainId").gotValue();

            Scdp.doAction("prmpurchasereq-projectquery", officeIdMap, function (result) {
                //根据查询结果设置接货地点，收货人和联系方式
                if (result.hasData) {
                    me.view.getCmp("addrArriveLocation").sotValue(result.lastArriverLocation);
                    me.view.getCmp("addrConsignee").sotValue(result.consignee);
                    me.view.getCmp("addrContactWay").sotValue(result.contactWay);
                }
            });
        }
    },
    expandInnerSupplierFn: function () {
        var me = this;

        var innerSupplier = me.view.getCmp("prmPurchaseReqDto->innerSupplier");
        var rootNode = innerSupplier.picker.getRootNode();
        var officeId = me.view.getCmp("prmPurchaseReqDto->departmentCodeDesc").gotValue();

        if (rootNode && rootNode.childNodes.length > 0) {

            if (!tmpNodes) {
                tmpNodes = new Array();
                for (var ti = 0; ti < rootNode.childNodes[0].childNodes.length; ti++) {
                    tmpNodes.push(rootNode.childNodes[0].childNodes[ti]);
                }
            } else {
                rootNode.childNodes[0].childNodes.splice(0, rootNode.childNodes[0].childNodes.length)
                for (var ai = 0; ai < tmpNodes.length; ai++) {
                    rootNode.childNodes[0].childNodes.push(tmpNodes[ai]);
                }
            }
            if (officeId) {
                for (var i = rootNode.childNodes[0].childNodes.length - 1; i >= 0; i--) {
                    var curNodes = rootNode.childNodes[0].childNodes[i];
                    if (curNodes.data.text == officeId) {
                        rootNode.childNodes[0].childNodes.splice(i, 1);
                    }
                }
            }
            if (rootNode.isExpanded()) {
                rootNode.collapse();
            }
            rootNode.expand();
        }
    },
    //地址等内容设定
    setAddressForm: function () {
        var me = this;

        var values = me.view.getCmp("prmPurchaseReqDetailDto").gotValue()[0];
        if (values != null) {
            me.view.getCmp("prmPurchaseReqDto->addrArriveLocation").sotValue(values.arriveLocation);
            me.view.getCmp("prmPurchaseReqDto->addrContactWay").sotValue(values.contactWay);
            me.view.getCmp("prmPurchaseReqDto->addrConsignee").sotValue(values.consignee);
            me.view.getCmp("prmPurchaseReqDto->addrRemark").sotValue(values.remark);
        }
        return null;
    },
    //申请数量和金额检查
    checkAmountFn: function (eventObj, isChanged) {
        var me = this;

        var myView = eventObj.grid.up("IView");
        var detailItems = myView.getCmp('prmPurchaseReqDetailDto').getStore().data.items;
        var totalBudget = 0;
        if (detailItems.length < 1) {
            return false;
        } else {
            for (var i = 0; i < detailItems.length; i++) {
                totalBudget += detailItems[i].get("amount") * detailItems[i].get("expectedPrice");
            }
        }

        myView.getCmp('totalMoney').sotValue(totalBudget);


        var field = eventObj.field;
        var record = eventObj.record;
        var position = {};
        position['row'] = eventObj.rowIdx;

//        if (field == "amount" || field == "expectedPrice") {
        if (field == "amount") {
            var originalValue = eventObj.originalValue;
            position['column'] = eventObj.colIdx;
            if (record.get("amount") > record.get("planAmount")) {
                Scdp.MsgUtil.info("申请数量超过最大值！");

                var cell = eventObj.view.getCellByPosition(position, true);
                cell.style.backgroundColor = '#EE0000';
//                record.set("amount",originalValue);
                return;
            }

//            if (record.get("amount") * record.get("expectedPrice") > record.get("purchaseBudgetMoney")) {
//                Scdp.MsgUtil.info("申请预算超过最大值！");
//                var cell = eventObj.view.getCellByPosition(position, true);
//                cell.style.backgroundColor = '#EE0000';
////                record.set("budgetPrice",originalValue);
//                return;
//            }
        }
        if (field == "expectedPrice") {
            var originalValue = eventObj.originalValue;
            position['column'] = eventObj.colIdx;
            if (record.get("expectedPrice") > record.get("budgetPrice")) {
                Scdp.MsgUtil.info("意向单价不能超过预算单价！");

                var cell = eventObj.view.getCellByPosition(position, true);
                cell.style.backgroundColor = '#EE0000';
                return;
            }
        }
        if (field == "amount" || field == "expectedPrice") {
            record.set("subTotal", Erp.MathUtil.multiNumber(record.get("amount"), record.get("expectedPrice")));
        }

        if (field == "supplierProperty" || field == "supplierId") {
            if (record.get("supplierProperty") == "1" || record.get("supplierProperty") == "2") {
                if (record.get("supplierId") == null || record.get("supplierId") == "") {
                    if (field == "supplierProperty") {
                        position['column'] = eventObj.colIdx - 1;
                    } else {
                        position['column'] = eventObj.colIdx;
                    }

                    Scdp.MsgUtil.info("供应商必须输入！");
                    var cell = eventObj.view.getCellByPosition(position, true);
                    cell.style.backgroundColor = '#EE0000';
                    return;
                }
            }
        }
    },
    loadWorkFlowProcessDeptCode: function () {
        var me = this;
        var processDeptCode = me.view.getCmp('prmPurchaseReqDto->officeId').gotValue();
        return processDeptCode;
    },
    setTotalMoney: function () {
        var me = this;
        var detailItems = me.view.getCmp('prmPurchaseReqDetailDto').getStore().data.items;
        var totalBudget = 0;
        if (detailItems.length < 1) {
            me.view.getCmp('totalMoney').sotValue(0);
            return;
        } else {
            for (var i = 0; i < detailItems.length; i++) {
                totalBudget += detailItems[i].get("amount") * detailItems[i].get("expectedPrice");
                detailItems[i].set("subTotal", detailItems[i].get("amount") * detailItems[i].get("expectedPrice"));
            }
        }

        me.view.getCmp('totalMoney').sotValue(totalBudget);
    },
    setAbolishBtnState: function () {
        var me = this;
        var view = me.view;
        var abolishBtn = view.getCmp('abolishBtn');
        var state = view.getCmp('prmPurchaseReqDto->state').gotValue();
        var isInner = view.getCmp('prmPurchaseReqDto->isInner').gotValue();
        if (state == 2 && isInner == 1) {
            abolishBtn.setDisabled(false);
        } else {
            abolishBtn.setDisabled(true);
        }

    },
    abolishBtnFn: function () {
        var me = this;
        var uuid = me.view.getCmp('prmPurchaseReqDto->uuid').gotValue();
        if (uuid == "") {
            Scdp.MsgUtil.info("未选择数据");
            return false;
        }
        Scdp.MsgUtil.confirm("是否确定废止该内委申请？", function (e) {
            if ("yes" == e) {
                var callBack = function (subView) {
                    var fallbackReason = subView.getCmp("fallbackReason").gotValue();
                    var postData = {uuid: uuid, fallbackReason: fallbackReason};
                    Scdp.doAction("prmpurchasereq-abolish", postData, function (result) {
                        if (result.success) {
                            Scdp.MsgUtil.info(result.result);
                            me.loadItem(uuid);
                        }
                    });
                    win.close();
                };
                var form = Ext.widget("JForm", {
                    height: 150,
                    width: 600,
                    layout: 'form',
                    bodyPadding: '10 10 10 10',
                    items: [
                        {
                            xtype: 'JTextArea',
                            allowBlank: true,
                            fieldLabel: '废止原因',
                            labelWidth: '60',
                            cid: 'fallbackReason',
                            width: 150,
                            height: 130
                        }
                    ]
                });
                var win = Scdp.openNewWinByView(form, callBack, 'temp_icon_16', '废止原因', "废止");

            }
        })
    },
    signProjectBtnFn: function () {
        var me = this;
        var view = me.view;
        var prmPurchaseReqDetailGrid = view.getCmp("prmPurchaseReqDetailDto");
        var uuid = view.getCmp("prmPurchaseReqDto->uuid").gotValue();
        var selectedRecords = prmPurchaseReqDetailGrid.getSelectionModel().getSelection();
        var prmPurchaseReqDetailUuids = "";
        for (var i = 0; i < selectedRecords.length; i++) {
            prmPurchaseReqDetailUuids += selectedRecords[i].data.uuid + ",";
        }
        var callBack = function (subView) {
            var grid = subView.getQueryPanel().getCmp("resultPanel");
            var selectedRecords = grid.getSelectionModel().getSelection();
            if (selectedRecords.length == 1) {
                var signProjectUuid = selectedRecords[0].get("uuid");//选中项目的uuid
                var postData = {};
                postData.prmPurchaseReqDetailUuids = prmPurchaseReqDetailUuids;
                postData.signProjectUuid = signProjectUuid;
                postData.signType = "1";
                Scdp.doAction("prmpurchasereq-signproject", postData, function (result) {
                    Scdp.MsgUtil.info("操作成功！");
                    me.loadItem(uuid);
                });
                return true;
            } else {
                Scdp.MsgUtil.info("请选择一个项目！");
            }
        };
        var queryController = Ext.create("Projectmain.controller.PickProjectmainQueryController");
        Scdp.openNewWinByController(queryController, callBack, 'temp_icon_16');
        var queryExtraParams = {detailType: me.prmDetailType};
        queryController.view.getConditionPanel().queryExtraParams = queryExtraParams;
    },

    abolishSignBtnFn: function () {
        var me = this;
        var view = me.view;

        Scdp.MsgUtil.confirm("您确定要取消标记吗？", function (e) {
            if ("yes" == e) {
                var prmPurchaseReqDetailGrid = view.getCmp("prmPurchaseReqDetailDto");
                var selectedRecords = prmPurchaseReqDetailGrid.getSelectionModel().getSelection();
                var uuid = view.getCmp("prmPurchaseReqDto->uuid").gotValue();
                var prmPurchaseReqDetailUuids = "";
                for (var i = 0; i < selectedRecords.length; i++) {
                    prmPurchaseReqDetailUuids += selectedRecords[i].data.uuid + ",";
                }
                var postData = {};
                postData.prmPurchaseReqDetailUuids = prmPurchaseReqDetailUuids;
                Scdp.doAction("prmpurchasereq-signproject", postData, function (result) {
                    Scdp.MsgUtil.info("操作成功！");
                    me.loadItem(uuid);
                });
            }
        })
    },
    setSignProjectBtnState: function () {
        var me = this;
        var view = me.view;

        var signProjectBtn = view.getCmp('editPanel->signProject');
        var abolishSignBtn = view.getCmp('editPanel->abolishSign');
        var state = view.getCmp('prmPurchaseReqDto->state').gotValue();
        if (state == 1 || state == 9) {
            signProjectBtn.setDisabled(false);
            abolishSignBtn.setDisabled(false);
        } else {
            signProjectBtn.setDisabled(true);
            abolishSignBtn.setDisabled(true);
        }
    }
});
