Ext.define("Card.controller.AssetTransferCardController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Card.view.AssetTransferCardView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'assetCardTransferDto->ransferType', name: 'change', fn: 'doChangeransferType'},
        {cid: 'fileUpload', name: 'click', fn: 'fileUploadBtn'},
        {cid: 'fileDownload', name: 'click', fn: 'fileDownloadBtn'},
        {cid: 'fileDelete', name: 'click', fn: 'fileDeleteBtn'},
        {cid: 'filePreview', name: 'click', fn: 'filePreviewBtn'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.asset.card.dto.AssetCardTransferDto',
    queryAction: 'card-query',
    loadAction: 'card-transferLoad',
    addAction: 'card-add',
    modifyAction: 'card-modify',
    deleteAction: 'card-delete',
    exportXlsAction: "card-exportxls",
    doChangeransferType: function (combo, newValue, oldValue, eOpts) {
        var me = this;
        var view = me.view;

        if (Erp.Util.isNullReturnEmpty(newValue) == "0" || Erp.Util.isNullReturnEmpty(newValue) == "1") {
            view.getCmp("assetCardTransferDto->storeplace").sotValue("民生路600号21#1楼");
            view.getCmp('assetCardTransferDto->endUserCode').sotValue("ZHUJUN");
            view.getCmp('assetCardTransferDto->endUserName').sotValue("朱骏");
            view.getCmp('assetCardTransferDto->liablePerson').sotValue("ZHUJUN");
            view.getCmp('assetCardTransferDto->liablePersonDesc').sotValue("朱骏");
            view.getCmp('assetCardTransferDto->officeId').putValue("CSNT_ZCB");
        }
    },
    clearJFieldSetValue: function (fieldSet, isEditable) {
        var cmps = fieldSet.items.items;
        Ext.Array.each(cmps, function (item) {
            item.sotEditable(isEditable);
            if (!isEditable) {
                item.sotValue(null);
            }
        });
    },
    afterInit: function () {
        var me = this;
        var view = me.view;
        me.callParent(arguments);
        view.getCmp('editToolbar->copyAddBtn').hide();
        view.getCmp('batchDelBtn').hide();
        me.controlFileButton();

        if (Scdp.ObjUtil.isNotEmpty(me.actionParams)) {
            if (Scdp.ObjUtil.isNotEmpty(me.actionParams.assetCardUuid)) {
                me.doAdd();
                new Ext.util.DelayedTask(function () {
                    view.getCmp("assetCardTransferDto->cardUuid").sotValue(Erp.Util.isNullReturnEmpty(me.actionParams.assetCardUuid));

                    var postData = {
                        assetCardUuid: Erp.Util.isNullReturnEmpty(me.actionParams.assetCardUuid)
                    };
                    var actionResult = Scdp.doAction("get-card", postData, null, null, true, false);
                    if (actionResult.assetCard) {
                        view.getCmp("assetCardTransferDto->cardCode").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.cardCode));
                        view.getCmp("assetCardTransferDto->assetCode").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.assetCode));
                        view.getCmp("assetCardTransferDto->assetName").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.assetName));
                        view.getCmp("assetCardTransferDto->unit").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.unit));
                        view.getCmp("assetCardTransferDto->source").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.source));
                        view.getCmp("assetCardTransferDto->deviceType").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.deviceType));
                        view.getCmp("assetCardTransferDto->specification").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.specification));
                        view.getCmp("assetCardTransferDto->model").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.model));
                        view.getCmp("assetCardTransferDto->purchaseTime").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.purchaseTime));
                        view.getCmp("assetCardTransferDto->limitMonth").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.limitMonth));
                        view.getCmp("assetCardTransferDto->discardTime").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.discardTime));
                        view.getCmp("assetCardTransferDto->storeplace").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.storeplace));
                        view.getCmp("assetCardTransferDto->localValue").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.localValue));
                        view.getCmp("assetCardTransferDto->monthDepreciation").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.monthDepreciation));
                        view.getCmp("assetCardTransferDto->netValue").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.netValue));
                        view.getCmp("assetCardTransferDto->factoryName").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.factoryName));
                        view.getCmp("assetCardTransferDto->releaseDate").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.releaseDate));
                        view.getCmp("assetCardTransferDto->identificationNumber").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.identificationNumber));
                        view.getCmp("assetCardTransferDto->chassisNumber").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.chassisNumber));
                        view.getCmp("assetCardTransferDto->vehicleNumber").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.vehicleNumber));
                        view.getCmp("assetCardTransferDto->vehicleType").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.vehicleType));
                        view.getCmp("assetCardTransferDto->annualCheckExpiredDate").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.annualCheckExpiredDate));
                        view.getCmp("assetCardTransferDto->insuranceExpiredDate").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.insuranceExpiredDate));
                        view.getCmp("assetCardTransferDto->buildingProperty").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.buildingProperty));
                        view.getCmp("assetCardTransferDto->area").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.area));
                        view.getCmp("assetCardTransferDto->checkedDate").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.checkedDate));
                        view.getCmp("assetCardTransferDto->validDate").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.validDate));
                        view.getCmp("assetCardTransferDto->authorizationCode").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.authorizationCode));
                        view.getCmp("assetCardTransferDto->operationUnit").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.operationUnit));
                        view.getCmp("assetCardTransferDto->operator").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.operator));
                        view.getCmp("assetCardTransferDto->operatorTel").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.operatorTel));
                        view.getCmp("assetCardTransferDto->accessory").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.accessory));
                        view.getCmp("assetCardTransferDto->remark").sotValue(Erp.Util.isNullReturnEmpty(actionResult.assetCard.descp));
                    }
                }).delay(1500);
            } else if (Scdp.ObjUtil.isNotEmpty(me.actionParams.assetTransferUuid)) {
                new Ext.util.DelayedTask(function () {
                    me.loadItem(me.actionParams.assetTransferUuid);
                    me.afterLoadItem();
                }).delay(1500);
            }
        }
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

        view.getCmp('assetCardTransferDto->createBy').sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_ID));
        view.getCmp('assetCardTransferDto->createTime').sotValue(new Date());
        view.getCmp('assetCardTransferDto->state').sotValue('0');
        view.getCmp('assetCardTransferDto->status').sotValue('I');
        view.getCmp('assetCardTransferDto->assetCode').sotValue(null);
        view.getCmp('assetCardTransferDto->assetName').sotValue(null);
        view.getCmp('assetCardTransferDto->cardCode').sotValue(null);

        //view.getCmp('assetCardTransferDto->endUserCode').sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_ID));
        //view.getCmp('assetCardTransferDto->endUserName').sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_NAME));
        //view.getCmp('assetCardTransferDto->liablePerson').sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_ID));
        //view.getCmp('assetCardTransferDto->liablePersonDesc').sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_NAME));
        //view.getCmp('assetCardTransferDto->officeId').putValue(Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE));
    },
    beforeModify: function () {
        var me = this;
        var view = me.view;
        return true;
    },
    afterModify: function () {
        var me = this;
        var view = me.view;
        view.getCmp("assetCardTransferDto->assetName").sotEditable(false);
        me.refreshCardCmpStatus();
        me.controlFileButton();
    },
    beforeSave: function () {
        var me = this;
        var view = me.view;
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
        var state = view.getCmp("assetCardTransferDto->state").gotValue();
        if (state != '0') {
            Scdp.MsgUtil.warn("您不能删除非新增状态的【固定资产变动申请单】");
            return false;
        }
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
        Ext.Array.each(selection, function (item) {
            var state = item.get("state");
            if ("新增" != state) {
                Scdp.MsgUtil.warn("您不能删除非新增状态的【固定资产变动申请单】");
                isExist = true;
                return;
            }
        });
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
            me.view.getCmp("fileUpload").setDisabled(false);
            me.view.getCmp("fileDownload").setDisabled(false);
            me.view.getCmp("filePreview").setDisabled(false);
            me.view.getCmp("fileDelete").setDisabled(false);
        }
    },

    refreshCardCmpStatus: function () {
        var me = this;
        var view = me.view;
    },
    loadWorkFlowProcessDeptCode: function () {
        var me = this;
        var processDeptCode = me.view.getCmp('assetCardTransferDto->officeId').gotValue();
        return processDeptCode;
    }
});

//var tempUuid = "";
