Ext.define("Card.controller.AssetAddCardController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Card.view.AssetAddCardView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'formPrint', name: 'click', fn: 'formPrintFn'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.asset.card.dto.AssetHandoverDto',
    queryAction: 'card-query',
    loadAction: 'card-addLoad',
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
    afterInit: function () {
        var me = this;
        var view = me.view;
        me.callParent(arguments);
        view.getCmp('editToolbar->copyAddBtn').hide();
        view.getCmp('batchDelBtn').hide();
    },
    beforeAdd: function () {
        var me = this;
        var view = me.view;
        return true;
    },
    afterAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("assetHandoverDto->handoverNo").sotEditable(false);
        view.getCmp('assetHandoverDto->checkAcceptanceResult').sotValue('合格');
        me.setDefaultValue();
    },
    beforeCopyAdd: function () {
        var me = this;
        var view = me.view;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("assetHandoverDto->handoverNo").sotEditable(false);
        me.setDefaultValue();
    },
    setDefaultValue: function () {
        var me = this;
        var view = me.view;

        view.getCmp('assetHandoverDto->createBy').sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_ID));
        view.getCmp('assetHandoverDto->createTime').sotValue(new Date());
        view.getCmp('assetHandoverDto->state').sotValue('0');
        view.getCmp('assetHandoverDto->status').sotValue('I');
        view.getCmp('assetHandoverDto->assetCode').sotValue(null);
        view.getCmp('assetHandoverDto->assetName').sotValue(null);
        view.getCmp('assetHandoverDto->cardCode').sotValue(null);

        view.getCmp('assetHandoverDto->endUserCode').sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_ID));
        view.getCmp('assetHandoverDto->endUserName').sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_NAME));
        view.getCmp('assetHandoverDto->liablePerson').sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_ID));
        view.getCmp('assetHandoverDto->liablePersonDesc').sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_NAME));
        view.getCmp('assetHandoverDto->officeId').putValue(Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE));

        view.getCmp('assetHandoverDto->purchaseReqNo').sotValue(null);
        view.getCmp('assetHandoverDto->purchaseReqDetailUuid').sotValue(null);
        view.getCmp('assetHandoverDto->handoverNo').sotValue(null);
        view.getCmp('assetHandoverDto->handoverDate').sotValue(null);

        view.getCmp('assetHandoverDto->model').sotValue(null);
        view.getCmp('assetHandoverDto->factoryName').sotValue(null);
    },
    beforeModify: function () {
        var me = this;
        var view = me.view;
        return true;
    },
    afterModify: function () {
        var me = this;
        var view = me.view;
        view.getCmp("assetHandoverDto->purchaseReqNo").sotEditable(false);
        view.getCmp("assetHandoverDto->handoverNo").sotEditable(false);

        me.refreshCardCmpStatus();
    },
    beforeSave: function () {
        var me = this;
        var view = me.view;
        var localValue = view.getCmp("assetHandoverDto->localValue").gotValue();
        var netValue = view.getCmp("assetHandoverDto->netValue").gotValue();
        var deviceType = view.getCmp("assetHandoverDto->deviceType").gotValue();
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

        me.view.getCmp('assetHandoverDto->handoverNo').sotValue(Erp.Util.isNullReturnEmpty(retData.handoverNo));
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
    },
    beforeDelete: function () {
        var me = this;
        var view = me.view;
        var state = view.getCmp("assetHandoverDto->state").gotValue();
        if (state != '0') {
            Scdp.MsgUtil.warn("您不能删除非新增状态的【固定资产验收交接单】");
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
        var resultPanel = view.getCmp('resultPanel');
        var selection = resultPanel.getSelectionModel().getSelection();
        var isExist = false;
        Ext.Array.each(selection, function (item) {
            var state = item.get("state");
            if ("新增" != state) {
                Scdp.MsgUtil.warn("您不能删除非新增状态的【固定资产验收交接单】");
                isExist = true;
                return;
            }
        });
        return !isExist;
    },
    afterBatchDel: function () {
        var me = this;
        var view = me.view;
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
    refreshCardCmpStatus: function () {
        var me = this;
        var view = me.view;
    },
    loadWorkFlowProcessDeptCode: function () {
        var me = this;
        var processDeptCode = me.view.getCmp('assetHandoverDto->officeId').gotValue();
        return processDeptCode;
    },
    formPrintFn: function () {
        var me = this;
        var view = me.view;
        var uuid = view.getCmp("assetHandoverDto->uuid").gotValue();
        var deviceType = view.getCmp("assetHandoverDto->deviceType").gotValue();
        if (Scdp.ObjUtil.isEmpty(uuid)) {
            Scdp.MsgUtil.info("未选择数据");
            return false;
        }
        var param = {uuid: uuid};
        if (deviceType == "DZYH-BG" || deviceType == "DZYH-SC" || deviceType == "DZYH-JL") {
            Scdp.printReport("erp/asset/AssetFixedAssetsReceipt_dzyh.cpt", [param], false, "pdf");
        }
        else if (deviceType == "RJ") {
            Scdp.printReport("erp/asset/AssetFixedAssetsReceipt_wxzc.cpt", [param], false, "pdf");
        }
        else {
            Scdp.printReport("erp/asset/AssetFixedAssetsReceipt.cpt", [param], false, "pdf");
        }
    }
});