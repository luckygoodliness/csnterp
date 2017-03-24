Ext.define("Card.controller.AssetCardController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Card.view.AssetCardView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'btnCardTransfer', name: 'click', fn: 'doBtnCardTransfer'},
        {cid: 'btnYearCheckRemind', name: 'click', fn: 'doBtnYearCheckRemind'},
        {cid: 'btnInsuranceMaturity', name: 'click', fn: 'doBtnInsuranceMaturity'},
        {cid: 'btnMustCheckRemind', name: 'click', fn: 'doBtnMustCheckRemind'},
        {cid: 'fileUpload', name: 'click', fn: 'fileUploadBtn'},
        {cid: 'fileDownload', name: 'click', fn: 'fileDownloadBtn'},
        {cid: 'fileDelete', name: 'click', fn: 'fileDeleteBtn'},
        {cid: 'filePreview', name: 'click', fn: 'filePreviewBtn'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.asset.card.dto.AssetCardDto',
    queryAction: 'card-query',
    loadAction: 'card-load',
    addAction: 'card-add',
    modifyAction: 'card-modify',
    deleteAction: 'card-delete',
    exportXlsAction: "card-exportxls",
    clearJFieldSetValue: function (fieldSet, isEditable) {
        var cmps = fieldSet.items.items;
        Ext.Array.each(cmps, function (item) {
            item.sotEditable(isEditable);
            if (!isEditable) {
                item.sotValue(null);
            }
        });
    },
    doBtnCardTransfer: function () {
        var me = this;
        var view = me.view;

        var menuCode = 'ASSETTRANSFERCARD';
        var param = {};
        param.assetCardUuid = Erp.Util.isNullReturnEmpty(view.getCmp('assetCardDto->uuid').gotValue());
        Scdp.openNewModuleByMenuCode(menuCode, param, "MENU_ITEM_CTL", true);
    },
    doBtnYearCheckRemind: function () {
        var callMe = this;
        var parentView = callMe.view;

        var uuid_P = parentView.getCmp('assetCardDto->uuid');
        var tblVersion_P = parentView.getCmp('assetCardDto->tblVersion');
        var assetCode_P = parentView.getCmp('assetCardDto->assetCode');
        var assetName_P = parentView.getCmp('assetCardDto->assetName');
        var model_P = parentView.getCmp('assetCardDto->model');
        var officeIdDesc_P = parentView.getCmp('assetCardDto->officeIdDesc');
        var officeId_P = parentView.getCmp('assetCardDto->officeId');
        var endUserCode_P = parentView.getCmp('assetCardDto->endUserCode');
        var endUserName_P = parentView.getCmp('assetCardDto->endUserName');
        var purchaseTime_P = parentView.getCmp('assetCardDto->purchaseTime');
        var vehicleNumber_P = parentView.getCmp('assetCardDto->vehicleNumber');
        var annualCheckExpiredDate_P = parentView.getCmp('assetCardDto->annualCheckExpiredDate');
        var insuranceExpiredDate_P = parentView.getCmp('assetCardDto->insuranceExpiredDate');

        var callback = function (subView) {
            var form = subView.getCmp('assetCardDto');
            if (!form.validate()) {
                return false;
            }

            var historyField = 'ANNUAL_CHECK_EXPIRED_DATE';
            var postData = {'assetCardDto': subView.getCmp('assetCardDto').gotValue(), historyField: historyField};
            Scdp.doAction('asset-card', postData, function (retData) {
                if (retData.success == true) {
                    var newRecord = retData.assetCardDto;
                    //var assetCardGrid = parentView.getCmp('assetCardDto');
                    //assetCardGrid.addRowItem(newRecord, false);
                    uuid_P.sotValue(newRecord.uuid);
                    tblVersion_P.sotValue(newRecord.tblVersion);
                    assetCode_P.sotValue(newRecord.assetCode);
                    assetName_P.sotValue(newRecord.assetName);
                    model_P.sotValue(newRecord.model);
                    officeIdDesc_P.sotValue(newRecord.officeIdDesc);
                    officeId_P.sotValue(newRecord.officeId);
                    endUserCode_P.sotValue(newRecord.endUserCode);
                    endUserName_P.sotValue(newRecord.endUserName);
                    purchaseTime_P.sotValue(newRecord.purchaseTime);
                    vehicleNumber_P.sotValue(newRecord.vehicleNumber);
                    annualCheckExpiredDate_P.sotValue(newRecord.annualCheckExpiredDate);
                    insuranceExpiredDate_P.sotValue(newRecord.insuranceExpiredDate);

                    callMe.refreshCardCmpStatus();
                    Scdp.MsgUtil.info("操作成功！");
                }
            });
            return true;
        };

        var controller = Ext.create("Card.controller.CarYearCheckRemindController");
        var win = Scdp.openNewWinByController(controller, callback, 'temp_icon_16', '年检提醒', '提交');

        var sonView = controller.view;

        var uuid_S = sonView.getCmp('assetCardDto->uuid');
        var tblVersion_S = sonView.getCmp('assetCardDto->tblVersion');
        var assetCode_S = sonView.getCmp('assetCardDto->assetCode');
        var assetName_S = sonView.getCmp('assetCardDto->assetName');
        var model_S = sonView.getCmp('assetCardDto->model');
        var officeIdDesc_S = sonView.getCmp('assetCardDto->officeIdDesc');
        var officeId_S = sonView.getCmp('assetCardDto->officeId');
        var endUserCode_S = sonView.getCmp('assetCardDto->endUserCode');
        var endUserName_S = sonView.getCmp('assetCardDto->endUserName');
        var purchaseTime_S = sonView.getCmp('assetCardDto->purchaseTime');
        var vehicleNumber_S = sonView.getCmp('assetCardDto->vehicleNumber');
        var annualCheckExpiredDate_S = sonView.getCmp('assetCardDto->annualCheckExpiredDate');
        var insuranceExpiredDate_S = sonView.getCmp('assetCardDto->insuranceExpiredDate');

        uuid_S.sotValue(uuid_P.gotValue());
        tblVersion_S.sotValue(tblVersion_P.gotValue());
        assetCode_S.sotValue(assetCode_P.gotValue());
        assetName_S.sotValue(assetName_P.gotValue());
        model_S.sotValue(model_P.gotValue());
        officeIdDesc_S.sotValue(officeIdDesc_P.gotValue());
        officeId_S.sotValue(officeId_P.gotValue());
        endUserCode_S.sotValue(endUserCode_P.gotValue());
        endUserName_S.sotValue(endUserName_P.gotValue());
        purchaseTime_S.sotValue(purchaseTime_P.gotValue());
        vehicleNumber_S.sotValue(vehicleNumber_P.gotValue());
        annualCheckExpiredDate_S.sotValue(annualCheckExpiredDate_P.gotValue());
        insuranceExpiredDate_S.sotValue(insuranceExpiredDate_P.gotValue());
    },
    doBtnInsuranceMaturity: function () {
        var callMe = this;
        var view = callMe.view;

        Scdp.MsgUtil.confirm("是否根据保险到期时间顺延1年?请确认!", function (e) {
            if (e == "yes") {
                var insuranceExpiredDate = view.getCmp('assetCardDto->insuranceExpiredDate').gotValue();
                view.getCmp('assetCardDto->insuranceExpiredDate').sotValue(new Date((insuranceExpiredDate.getFullYear() + 1).toString() + "/" + Erp.Util.addZero(insuranceExpiredDate.getMonth() + 1) + "/" + Erp.Util.addZero(insuranceExpiredDate.getDate())));

                var historyField = 'INSURANCE_EXPIRED_DATE';
                var postData = {'assetCardDto': view.getCmp('assetCardDto').gotValue(), historyField: historyField};
                Scdp.doAction('asset-card', postData, function (retData) {
                    if (retData.success == true) {
                        callMe.refreshCardCmpStatus();
                        Scdp.MsgUtil.info("操作成功！");
                    }
                });
            }
        });
    },
    doBtnMustCheckRemind: function () {
        var callMe = this;
        var parentView = callMe.view;

        var uuid_P = parentView.getCmp('assetCardDto->uuid');
        var tblVersion_P = parentView.getCmp('assetCardDto->tblVersion');
        var assetCode_P = parentView.getCmp('assetCardDto->assetCode');
        var assetName_P = parentView.getCmp('assetCardDto->assetName');
        var model_P = parentView.getCmp('assetCardDto->model');
        var officeIdDesc_P = parentView.getCmp('assetCardDto->officeIdDesc');
        var officeId_P = parentView.getCmp('assetCardDto->officeId');
        var endUserCode_P = parentView.getCmp('assetCardDto->endUserCode');
        var endUserName_P = parentView.getCmp('assetCardDto->endUserName');
        var purchaseTime_P = parentView.getCmp('assetCardDto->purchaseTime');
        var checkedDate_P = parentView.getCmp('assetCardDto->checkedDate');

        var callback = function (subView) {
            var form = subView.getCmp('assetCardDto');
            if (!form.validate()) {
                return false;
            }

            var historyField = 'CHECKED_DATE';
            var postData = {'assetCardDto': subView.getCmp('assetCardDto').gotValue(), historyField: historyField};
            Scdp.doAction('asset-card', postData, function (retData) {
                if (retData.success == true) {
                    var newRecord = retData.assetCardDto;
                    //var assetCardGrid = parentView.getCmp('assetCardDto');
                    //assetCardGrid.addRowItem(newRecord, false);
                    uuid_P.sotValue(newRecord.uuid);
                    tblVersion_P.sotValue(newRecord.tblVersion);
                    assetCode_P.sotValue(newRecord.assetCode);
                    assetName_P.sotValue(newRecord.assetName);
                    model_P.sotValue(newRecord.model);
                    officeIdDesc_P.sotValue(newRecord.officeIdDesc);
                    officeId_P.sotValue(newRecord.officeId);
                    endUserCode_P.sotValue(newRecord.endUserCode);
                    endUserName_P.sotValue(newRecord.endUserName);
                    purchaseTime_P.sotValue(newRecord.purchaseTime);
                    checkedDate_P.sotValue(newRecord.checkedDate);

                    callMe.refreshCardCmpStatus();
                    Scdp.MsgUtil.info("操作成功！");
                }
            });
            return true;
        };

        var controller = Ext.create("Card.controller.DeviceMustCheckRemindController");
        var win = Scdp.openNewWinByController(controller, callback, 'temp_icon_16', '检定提醒', '提交');

        var sonView = controller.view;

        var uuid_S = sonView.getCmp('assetCardDto->uuid');
        var tblVersion_S = sonView.getCmp('assetCardDto->tblVersion');
        var assetCode_S = sonView.getCmp('assetCardDto->assetCode');
        var assetName_S = sonView.getCmp('assetCardDto->assetName');
        var model_S = sonView.getCmp('assetCardDto->model');
        var officeIdDesc_S = sonView.getCmp('assetCardDto->officeIdDesc');
        var officeId_S = sonView.getCmp('assetCardDto->officeId');
        var endUserCode_S = sonView.getCmp('assetCardDto->endUserCode');
        var endUserName_S = sonView.getCmp('assetCardDto->endUserName');
        var purchaseTime_S = sonView.getCmp('assetCardDto->purchaseTime');
        var checkedDate_S = sonView.getCmp('assetCardDto->checkedDate');

        uuid_S.sotValue(uuid_P.gotValue());
        tblVersion_S.sotValue(tblVersion_P.gotValue());
        assetCode_S.sotValue(assetCode_P.gotValue());
        assetName_S.sotValue(assetName_P.gotValue());
        model_S.sotValue(model_P.gotValue());
        officeIdDesc_S.sotValue(officeIdDesc_P.gotValue());
        officeId_S.sotValue(officeId_P.gotValue());
        endUserCode_S.sotValue(endUserCode_P.gotValue());
        endUserName_S.sotValue(endUserName_P.gotValue());
        purchaseTime_S.sotValue(purchaseTime_P.gotValue());
        checkedDate_S.sotValue(checkedDate_P.gotValue());
    },
    afterInit: function () {
        var me = this;
        var view = me.view;
        me.callParent(arguments);
        view.getCmp('editToolbar->copyAddBtn').hide();
        view.getCmp('editToolbar->deleteBtn').hide();
        view.getCmp('batchDelBtn').hide();
        view.getCmp('status').sotValue("I");
        me.controlFileButton();

        var assetTransferGrid = view.getCmp("assetTransferDto");
        assetTransferGrid.on("celldblclick", function (obj, td, cellIndex, record, tr, rowIndex, e, eOpts) {
            //alert("hello");
            var menuCode = 'ASSETTRANSFERCARD';
            var param = {};
            param.assetTransferUuid = Erp.Util.isNullReturnEmpty(record.data.uuid);
            Scdp.openNewModuleByMenuCode(menuCode, param, "MENU_ITEM_CTL", true);
        });
    },
    beforeAdd: function () {
        var me = this;
        var view = me.view;
        return true;
    },
    afterAdd: function () {
        var me = this;
        var view = me.view;
        me.setDefaultValue();
        me.controlFileButton();
    },
    beforeCopyAdd: function () {
        var me = this;
        var view = me.view;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        var view = me.view;
        me.setDefaultValue();
        me.controlFileButton();
    },
    setDefaultValue: function () {
        var me = this;
        var view = me.view;

        view.getCmp('assetCardDto->createBy').sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_ID));
        view.getCmp('assetCardDto->createTime').sotValue(new Date());
        view.getCmp('assetCardDto->state').sotValue('2');
        view.getCmp('assetCardDto->status').sotValue('I');
        view.getCmp('assetCardDto->assetCode').sotValue(null);
        view.getCmp('assetCardDto->assetName').sotValue(null);
        view.getCmp('assetCardDto->cardCode').sotValue(null);

        view.getCmp('assetCardDto->endUserCode').sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_ID));
        view.getCmp('assetCardDto->endUserName').sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_NAME));
        view.getCmp('assetCardDto->liablePerson').sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_ID));
        view.getCmp('assetCardDto->liablePersonDesc').sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_NAME));
        view.getCmp('assetCardDto->officeId').putValue(Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE));
    },
    beforeModify: function () {
        var me = this;
        var view = me.view;
        return true;
    },
    afterModify: function () {
        var me = this;
        var view = me.view;
        var state = view.getCmp("assetCardDto->state").gotValue();
        if (state == '2') {
            view.getCmp("assetCardDto->assetCode").sotEditable(false);
            view.getCmp("assetCardDto->cardCode").sotEditable(false);
            view.getCmp("assetCardDto->endUserName").sotEditable(false);
            view.getCmp("assetCardDto->officeId").sotEditable(false);
            view.getCmp("assetCardDto->liablePersonDesc").sotEditable(false);
        }
        me.refreshCardCmpStatus();
        me.controlFileButton();
    },
    beforeSave: function () {
        var me = this;
        var view = me.view;
        var localValue = view.getCmp("assetCardDto->localValue").gotValue();
        var netValue = view.getCmp("assetCardDto->netValue").gotValue();
        var deviceType = view.getCmp("assetCardDto->deviceType").gotValue();
        if (localValue < netValue) {
            Scdp.MsgUtil.warn("净值不能大于金额！");
            return false;
        }
        if (deviceType == "JLSB" && localValue <= 2000) {
            Scdp.MsgUtil.warn("【计量设备】之金额必须大于2000！");
            return false;
        }
        if (deviceType == "DZYH-JL" && localValue > 2000) {
            Scdp.MsgUtil.warn("【低值易耗-计量】之金额必须小于等于2000！");
            return false;
        }
        return true;
    },
    afterSave: function (retData) {
        var me = this;
        var view = me.view;
        me.callParent(arguments);
        me.refreshCardCmpStatus();
        me.controlFileButton();
    },
    beforeLoadItem: function () {
        var me = this;
        var view = me.view;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        var view = me.view;
        me.callParent(arguments);
        me.refreshCardCmpStatus();
        me.controlFileButton();

        var status = view.getCmp('assetCardDto->status').gotValue();
        if (status == "I") {
            view.getCmp('editToolbar->modifyBtn').setDisabled(false);
            view.getCmp('editToolbar->deleteBtn').setDisabled(false);
        }
        else {
            view.getCmp('editToolbar->modifyBtn').setDisabled(true);
            view.getCmp('editToolbar->deleteBtn').setDisabled(true);
        }
    },
    beforeCancel: function () {
        var me = this;
        var view = me.view;
        return true;
    },
    afterCancel: function () {
        var me = this;
        var view = me.view;
        me.callParent(arguments);
        me.refreshCardCmpStatus();
        me.controlFileButton();
    },
    beforeDelete: function () {
        var me = this;
        var view = me.view;
        var state = view.getCmp("assetCardDto->state").gotValue();
        /*if (state != '0') {
         Scdp.MsgUtil.warn("您不能删除非新增状态的【固定资产卡片】");
         return false;
         }*/
        return true;
    },
    afterDelete: function () {
        var me = this;
        var view = me.view;
        me.controlFileButton();
    },
    beforeBatchDel: function () {
        var me = this;
        var view = me.view;
        var resultPanel = view.getCmp('resultPanel');
        var selection = resultPanel.getSelectionModel().getSelection();
        var isExist = false;
        /*Ext.Array.each(selection, function (item) {
         var state = item.get("state");
         if ("新增" != state) {
         Scdp.MsgUtil.warn("您不能删除非新增状态的【固定资产卡片】");
         isExist = true;
         return;
         }
         });*/
        return !isExist;
    },
    afterBatchDel: function () {
        var me = this;
        var view = me.view;
        me.controlFileButton();
    },
    beforeExport: function () {
        var me = this;
        var view = me.view;
        return true;
    },
    afterExport: function () {
        var me = this;
        var view = me.view;
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
        var state = me.view.getCmp("assetCardDto->state").gotValue();
        if (Scdp.ObjUtil.isEmpty(status)) {
            me.view.getCmp("fileUpload").setDisabled(true);
            me.view.getCmp("fileDownload").setDisabled(true);
            me.view.getCmp("filePreview").setDisabled(true);
            me.view.getCmp("fileDelete").setDisabled(true);
            return;
        }
        if (status == Scdp.I18N.VIEW_STATUS) {
            me.view.getCmp("fileUpload").setDisabled(true);
            me.view.getCmp("fileDownload").setDisabled(false);
            me.view.getCmp("filePreview").setDisabled(false);
            me.view.getCmp("fileDelete").setDisabled(true);
        } else if (status == Scdp.I18N.MODIFY_STATUS || status == Scdp.I18N.ADD_STATUS) {
            if (state == '2') {
                me.view.getCmp("fileUpload").setDisabled(true);
                me.view.getCmp("fileDownload").setDisabled(false);
                me.view.getCmp("filePreview").setDisabled(false);
                me.view.getCmp("fileDelete").setDisabled(true);
            } else {
                me.view.getCmp("fileUpload").setDisabled(false);
                me.view.getCmp("fileDownload").setDisabled(false);
                me.view.getCmp("filePreview").setDisabled(false);
                me.view.getCmp("fileDelete").setDisabled(false);
            }
        }
    },

    refreshCardCmpStatus: function () {
        var me = this;
        var view = me.view;

        view.getCmp("editPanel->btnCardTransfer").setDisabled(true);
        view.getCmp("editPanel->btnYearCheckRemind").setDisabled(true);
        view.getCmp("editPanel->btnInsuranceMaturity").setDisabled(true);
        view.getCmp("editPanel->btnMustCheckRemind").setDisabled(true);

        var deviceType = view.getCmp("assetCardDto->deviceType").gotValue();
        var state = view.getCmp("assetCardDto->state").gotValue();
        var status = view.getCmp("assetCardDto->status").gotValue();

        if (state == '2') {
            view.getCmp("editPanel->btnCardTransfer").setDisabled(false);

            if (status == 'I') {
                var currentDate = new Date();
                currentDate.setDate(currentDate.getDate() + 30);

                var annualCheckExpiredDate = view.getCmp("assetCardDto->annualCheckExpiredDate").gotValue();
                if (annualCheckExpiredDate) {
                    if (currentDate.getTime() >= annualCheckExpiredDate.getTime()) {
                        view.getCmp("editPanel->btnYearCheckRemind").setDisabled(false);
                    }
                }

                var insuranceExpiredDate = view.getCmp("assetCardDto->insuranceExpiredDate").gotValue();
                if (insuranceExpiredDate) {
                    if (currentDate.getTime() >= insuranceExpiredDate.getTime()) {
                        view.getCmp("editPanel->btnInsuranceMaturity").setDisabled(false);
                    }
                }

                var checkedDate = view.getCmp("assetCardDto->checkedDate").gotValue();
                if (checkedDate) {
                    if (currentDate.getTime() >= checkedDate.getTime()) {
                        view.getCmp("editPanel->btnMustCheckRemind").setDisabled(false);
                    }
                }
            }
        }
    },
    loadWorkFlowProcessDeptCode: function () {
        var me = this;
        var processDeptCode = me.view.getCmp('assetCardDto->officeId').gotValue();
        return processDeptCode;
    }
});