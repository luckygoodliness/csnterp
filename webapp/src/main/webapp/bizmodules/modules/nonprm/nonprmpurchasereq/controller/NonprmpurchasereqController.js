Ext.define("Nonprmpurchasereq.controller.NonprmpurchasereqController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Nonprmpurchasereq.view.NonprmpurchasereqView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'okBtn', name: 'click', fn: 'clickControl'},
        {cid: 'fileUpload', name: 'click', fn: 'fileUploadBtn'},
        {cid: 'fileDownload', name: 'click', fn: 'fileDownloadBtn'},
        {cid: 'fileDelete', name: 'click', fn: 'fileDeleteBtn'},
        {cid: 'subUpload', name: 'click', fn: 'subUploadBtn'},
        {cid: 'subDownload', name: 'click', fn: 'subDownloadBtn'},
        {cid: 'subDelfile', name: 'click', fn: 'subDelfileBtn'},
        {cid: 'downloadFormBtn', name: 'click', fn: 'clickDownloadFormBtn'},
        {cid: 'uploadFormBtn', name: 'click', fn: 'clickUploadFormBtn'},
        //M4_C10_F1_操作优化
        {cid:'prmPurchaseReqDto->subjectCode',name:'change',fn:'subjectCodeChange'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.nonprm.nonprmpurchasereq.dto.PrmPurchaseReqDto',
    queryAction: 'nonprmpurchasereq-query',
    loadAction: 'nonprmpurchasereq-load',
    addAction: 'nonprmpurchasereq-add',
    modifyAction: 'nonprmpurchasereq-modify',
    deleteAction: 'nonprmpurchasereq-delete',
    exportXlsAction: "nonprmpurchasereq-exportxls",
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
        me.view.getCmp("okBtn").setDisabled(true);
        me.view.getCmp("downloadFormBtn").setDisabled(true);
        me.view.getCmp("uploadFormBtn").setDisabled(true);

        me.view.getCmp("financialSubjectCode").filterConditions = {selfconditions: " 1=1 AND t.need_purchase = '1' "};

        me.view.getCmp('prmPurchaseReqDetailDto').rowAddFn = function () {
            var view = me.view;
            var callBack = function (subView) {
                var results = subView.getResultPanel(),
                    selectedRecords = results.getSelectionModel().getSelection(),
                    data = [],
                    editGrid = view.getCmp("prmPurchaseReqDetailDto");

                if (selectedRecords.length > 0) {
                    //Get the user info form selected Records.
                    Ext.Array.each(selectedRecords, function (item) {
                        var record = {};
                        record.planAmount = item.get("remainAmount");
                        record.amount = item.get("remainAmount");
                        record.purchaseBudgetMoney = item.get("remainPrice");
                        record.subTotal = Erp.MathUtil.multiNumber(item.get("remainAmount"), item.get("price"), 2);
                        record.price = item.get("price");
                        record.expectedPrice = item.get("price");
                        record.name = item.get("item");
                        record.purchasePackageId = item.get("uuid");
                        data.push(record);

                    });

                    Ext.Array.each(data, function (record) {
                        var hasData = false;
                        if (record.planAmount > 0 && record.purchaseBudgetMoney > 0) {
                            for (var j = 0; j < editGrid.store.data.items.length; j++) {
                                if (editGrid.store.data.items[j].data.purchasePackageId == record.purchasePackageId) {
                                    hasData = true;
                                    break;
                                }
                            }
                            if (!hasData) {
                                me.view.getCmp('totalMoney').sotValue(me.view.getCmp('totalMoney').gotValue() + record.expectedPrice * record.amount);
                                if (editGrid.beforeAddRow) {
                                    editGrid.beforeAddRow(record);
                                }
                                editGrid.addRowItem(record, false);
                                editGrid.setReadOnlyCss(editGrid);
                                editGrid.setCurRecord(editGrid.store.getCount() - 1);
                                if (editGrid.afterAddRow) {
                                    editGrid.afterAddRow();
                                }
                            }
                        }
                    })
                }
                return true;
            };

            var tmpSubjectCode = me.view.getCmp("financialSubjectCode").gotValue();
            if (tmpSubjectCode != null && tmpSubjectCode != "") {
                if (tmpSubjectCode.substring(tmpSubjectCode.indexOf("-") + 1, tmpSubjectCode.length) == "固") {
                    var controller = Ext.create("Nonprmpurchasereq.controller.QueryBudgetController");
                    var param = {prmProjectMainId: me.view.getCmp("prmPurchaseReqDto->bugdetId").gotValue()};
                    controller.actionParams = param;

                    Scdp.openNewWinByController(controller, callBack, 'user_icon_16', "查找预算明细", "选择明细");
                } else {
                    me.view.getCmp("prmPurchaseReqDetailDto").addRowItem(null);
                }
            }
        };

    },
    beforeAdd: function () {
        var me = this;
        me.view.getCmp("okBtn").setDisabled(false);
        return true;
    },
    afterAdd: function () {
        var me = this;

        me.view.getCmp("fileUpload").setDisabled(false);
        me.view.getCmp("fileDelete").setDisabled(false);

        me.view.getCmp("subUpload").setDisabled(false);
        me.view.getCmp("subDelfile").setDisabled(false);

        me.view.getCmp("departmentCodeDesc").sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_NAME));
        me.view.getCmp("departmentCode").sotValue(Erp.Util.getCurrentDeptCode());

        me.view.getCmp("prmPurchaseReqDetailDto").sotEditable(false);
        me.view.getCmp("financialSubjectCode").focus();
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        me.view.getCmp("purchaseReqNo").sotValue("");

        me.view.getCmp("departmentCodeDesc").sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_NAME));
        me.view.getCmp("departmentCode").sotValue(Erp.Util.getCurrentDeptCode());

        me.view.getCmp("prmPurchaseReqDto->state").sotValue("0");
        me.view.getCmp("purchaseLevel").sotValue("");
        me.view.getCmp("financialSubjectCode").setReadOnly(true);
    },
    beforeModify: function () {
        var me = this;
        return true;
    },
    afterModify: function () {
        var me = this;
        me.view.getCmp("fileUpload").setDisabled(false);
        me.view.getCmp("fileDelete").setDisabled(false);

        me.view.getCmp("subUpload").setDisabled(false);
        me.view.getCmp("subDelfile").setDisabled(false);
        var tmpSubjectCode = me.view.getCmp("financialSubjectCode").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(tmpSubjectCode) && !tmpSubjectCode.endsWith("-固")) {
            me.view.getCmp("downloadFormBtn").setDisabled(false);
            me.view.getCmp("uploadFormBtn").setDisabled(false);
        }
        me.setAddressForm();
        me.setThisControl(true);
        me.setTotalMoney();
        return true;
    },
    beforeSave: function () {
        var me = this;
        var detailItems = me.view.getCmp('prmPurchaseReqDetailDto').getStore().data.items;
        if (detailItems.length < 1) {
            Scdp.MsgUtil.info("请选择一条记录！");
            return false;
        }
        var tmpSubjectCode = me.view.getCmp("financialSubjectCode").gotValue();
        var totalBudget = 0;
        if (Scdp.ObjUtil.isNotEmpty(tmpSubjectCode) && tmpSubjectCode.endsWith("-固")) {
            for (var i = 0; i < detailItems.length; i++) {
                totalBudget += (detailItems[i].get("amount") * detailItems[i].get("expectedPrice"));
                if (detailItems[i].get("amount") > detailItems[i].get("planAmount")) {
                    Scdp.MsgUtil.info("申请数量超过最大值！");
                    return false;
                }

                if (detailItems[i].get("expectedPrice") > detailItems[i].get("price")) {
                    Scdp.MsgUtil.info("意向单价超过最大值！");
                    return false;
                }

                if ((detailItems[i].get("amount") * detailItems[i].get("expectedPrice")) > detailItems[i].get("purchaseBudgetMoney")) {
                    Scdp.MsgUtil.info("申请预算超过最大值！");
                    return false;
                }
                detailItems[i].set("needUpdate", "1");
            }
        } else {
            for (var i = 0; i < detailItems.length; i++) {
                totalBudget += (detailItems[i].get("amount") * detailItems[i].get("expectedPrice"));
                if (totalBudget > me.view.getCmp("checkRemainMoney").gotValue()) {
                    Scdp.MsgUtil.info("申请预算超过最大值！");
                    return false;
                }
                detailItems[i].set("needUpdate", "1");
            }
        }
        if (totalBudget < 10000) {
            me.view.getCmp("prmPurchaseReqDto->purchaseLevel").sotValue("2");
        } else {
            me.view.getCmp("prmPurchaseReqDto->purchaseLevel").sotValue("1");
        }
        me.view.getCmp("prmPurchaseReqDto->officeId").sotValue(me.view.getCmp("prmPurchaseReqDto->departmentCode").gotValue());
        return true;
    },
    afterSave: function (retData) {
        var me = this;
        me.callParent(arguments);
        me.setThisControl(true);

        me.view.getCmp("fileUpload").setDisabled(true);
        me.view.getCmp("fileDelete").setDisabled(true);
        me.view.getCmp("subUpload").setDisabled(true);
        me.view.getCmp("subDelfile").setDisabled(true);

        me.view.getCmp("downloadFormBtn").setDisabled(true);
        me.view.getCmp("uploadFormBtn").setDisabled(true);
    },
    beforeLoadItem: function () {
        var me = this;
        me.view.getCmp("okBtn").setDisabled(true);
        me.view.getCmp("downloadFormBtn").setDisabled(true);
        me.view.getCmp("uploadFormBtn").setDisabled(true);
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        me.callParent(arguments);
        me.setAddressForm();
        me.setTotalMoney();
        me.setColumnStatus();
    },
    beforeCancel: function () {
        var me = this;
        me.setThisControl(false);
        return true;
    },
    afterCancel: function () {
        var me = this;
        me.callParent(arguments);
        me.view.getCmp("fileUpload").setDisabled(true);
        me.view.getCmp("fileDelete").setDisabled(true);

        me.view.getCmp("subUpload").setDisabled(true);
        me.view.getCmp("subDelfile").setDisabled(true);

        me.view.getCmp("okBtn").setDisabled(true);
        me.view.getCmp("downloadFormBtn").setDisabled(true);
        me.view.getCmp("uploadFormBtn").setDisabled(true);
        me.setTotalMoney();
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
//文件上传
    fileUploadBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        var fileClassify = "SCM_SUPPLIER_CHANGE";
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
//文件删除
    fileDeleteBtn: function () {
        var me = this;
        var grid = me.view.getCmp("cdmFileRelationDto");
        Erp.FileUtil.erpFileDelete(grid);
    },
//根据回传的数据，在页面上显示相关信息
    initFileUploadData: function (fileGrid, obj) {
        if (obj != null) {
            obj.fileId = obj.uuid;
            obj.module = Scdp.getActiveModule().controller.menuCode;
            obj.cdmFileType = "";
            delete obj["uuid"];
            fileGrid.addRowItem(obj, false);
        }
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
//地址等内容设定
    setAddressForm: function () {
        var me = this;
        var values = me.view.getCmp("prmPurchaseReqDetailDto").gotValue()[0];
        if (values != null) {
            me.view.getCmp("prmPurchaseReqDto->addrArriveLocation").sotValue(values.arriveLocation);
            me.view.getCmp("prmPurchaseReqDto->addrContactWay").sotValue(values.contactWay);
            me.view.getCmp("prmPurchaseReqDto->addrRemark").sotValue(values.remark);
            me.view.getCmp("prmPurchaseReqDto->addrConsignee").sotValue(values.consignee);
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

        if (field == "amount" || field == "expectedPrice") {
            var originalValue = eventObj.originalValue;
            position['column'] = eventObj.colIdx;
            record.set("subTotal", Erp.MathUtil.multiNumber(record.get("amount"), record.get("expectedPrice")));
            if (record.get("planAmount") != null && record.get("planAmount") != "") {
                if (record.get("amount") > record.get("planAmount")) {
                    Scdp.MsgUtil.info("申请数量超过最大值！");
                    var cell = eventObj.view.getCellByPosition(position, true);
                    cell.style.backgroundColor = '#EE0000';
                    return;
                }
            }

            if (record.get("price") != null && record.get("price") != "") {
                if (record.get("expectedPrice") > record.get("price")) {
                    Scdp.MsgUtil.info("意向单价超过最大值！");
                    var cell = eventObj.view.getCellByPosition(position, true);
                    cell.style.backgroundColor = '#EE0000';
                    return;
                }
            }

            if (record.get("purchaseBudgetMoney") != null && record.get("purchaseBudgetMoney") != "") {
                if (record.get("amount") * record.get("expectedPrice") > record.get("purchaseBudgetMoney")) {
                    Scdp.MsgUtil.info("申请预算超过最大值！");
                    var cell = eventObj.view.getCellByPosition(position, true);
                    cell.style.backgroundColor = '#EE0000';
                    return;
                }
            }
        }
    },
    clickControl: function () {
        var me = this;
        var subjectCode = this.view.getCmp('prmPurchaseReqDto->financialSubjectCode').gotValue();
        if (Scdp.ObjUtil.isEmpty(subjectCode)) {
            this.view.getCmp('prmPurchaseReqDto->financialSubjectCode').markInvalid(Erp.I18N.MSG_ITEM_EMPTY);
            return;
        }

        me.setThisControl(true);
        me.view.getCmp("prmPurchaseReqDetailDto").sotEditable(true);
        me.view.getCmp("okBtn").setDisabled(true);
        if (Scdp.ObjUtil.isNotEmpty(subjectCode) && !subjectCode.endsWith("-固")) {
            me.view.getCmp("downloadFormBtn").setDisabled(false);
            me.view.getCmp("uploadFormBtn").setDisabled(false);
        }
        me.setColumnStatus();
    },
    //M4_C10_F1_操作优化
    subjectCodeChange:function(){
        var me = this;
        var subjectCode = this.view.getCmp('prmPurchaseReqDto->financialSubjectCode').gotValue();
        if (Scdp.ObjUtil.isEmpty(subjectCode)) {
            this.view.getCmp('prmPurchaseReqDto->financialSubjectCode').markInvalid(Erp.I18N.MSG_ITEM_EMPTY);
            return;
        }

        me.setThisControl(true);
        me.view.getCmp("prmPurchaseReqDetailDto").sotEditable(true);
        me.view.getCmp("okBtn").setDisabled(true);
        if (Scdp.ObjUtil.isNotEmpty(subjectCode) && !subjectCode.endsWith("-固")) {
            me.view.getCmp("downloadFormBtn").setDisabled(false);
            me.view.getCmp("uploadFormBtn").setDisabled(false);
        }
        me.setColumnStatus();
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
    setThisControl: function (rlst) {
        var me = this;
//        me.view.getCmp("departmentCode").setDisabled(rlst);
        me.view.getCmp("financialSubjectCode").setReadOnly(rlst);
    },
    setColumnStatus: function () {
        var me = this;
        var tmpSubjectCode = me.view.getCmp("financialSubjectCode").gotValue();
        if (Scdp.ObjUtil.isNotEmpty(tmpSubjectCode) && !tmpSubjectCode.endsWith("-固")) {
            me.view.getCmp("prmPurchaseReqDetailDto").getColumnBydataIndex("purchaseBudgetMoney").setVisible(false);
            me.view.getCmp("prmPurchaseReqDetailDto").getColumnBydataIndex("planAmount").setVisible(false);
            me.view.getCmp("prmPurchaseReqDetailDto").getColumnBydataIndex("price").setVisible(false);
        } else {
            me.view.getCmp("prmPurchaseReqDetailDto").getColumnBydataIndex("purchaseBudgetMoney").setVisible(true);
            me.view.getCmp("prmPurchaseReqDetailDto").getColumnBydataIndex("planAmount").setVisible(true);
            me.view.getCmp("prmPurchaseReqDetailDto").getColumnBydataIndex("price").setVisible(true);
        }

    },
    //标准模板下载
    clickDownloadFormBtn: function () {
        var fileClassifyType = "SCM_PURCHASE_REQ_TEMPLATE";
        var cdmFileType = "SCM_PURCHASE";
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
    //导入申请明细
    clickUploadFormBtn: function () {
        var me = this;
        var view = me.view;
//        Scdp.MsgUtil.confirm("是否确认上传？", function (e) {
//            if (e == "yes") {
        var formView = Ext.widget("JForm", {
            height: 150,
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
            uploadData.push("new");
            var postData = {uploadMeta: uploadData};
            Scdp.doAction("nonprmpurchasereq-importexcel", postData, function (result) {
                if (result.saveFlag) {
                    Scdp.MsgUtil.info("导入成功！");
                    var editGrid = me.view.getCmp("prmPurchaseReqDetailDto");
                    var selectRecord = result.prmPurchaseReqDetail;
                    if (selectRecord.length > 0) {
                        editGrid.addRowItems(selectRecord);
                        me.setTotalMoney();
                    }

//                            Ext.Array.each(selectRecord, function (record) {
//                                if (editGrid.beforeAddRow) {
//                                    editGrid.beforeAddRow(record);
//                                }
//                                editGrid.addRowItem(record, false);
//                                editGrid.setReadOnlyCss(editGrid);
//                                editGrid.setCurRecord(editGrid.store.getCount() - 1);
//                                if (editGrid.afterAddRow) {
//                                    editGrid.afterAddRow();
//                                }
//                            });
//                            me.setTotalMoney();
                } else {
                    Erp.Util.showLogView(Erp.I18N.EXCEL_UPLOAD_FAILURE + Erp.Const.BREAK_LINE + result.errorMsgLog);
                }
                win.close();
            }, null, null, null, subView.getForm());
        };
        var win = Scdp.openNewWinByView(formView, callBack, 'temp_icon_16', '文件上传', "上传文件");
//            }
//        });
    },
    loadWorkFlowProcessDeptCode: function () {
        var me = this;
        var processDeptCode = me.view.getCmp('prmPurchaseReqDto->departmentCode').gotValue();
        return processDeptCode;
    }
});
