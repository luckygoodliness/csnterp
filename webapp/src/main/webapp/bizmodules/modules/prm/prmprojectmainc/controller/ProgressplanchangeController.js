Ext.define("Prmprojectmainc.controller.ProgressplanchangeController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Prmprojectmainc.view.ProgressplanchangeView',
    uniqueValidateFields: [],
    extraEvents: [],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.prmprojectmainc.dto.PrmProjectMainCDto',
    queryAction: 'prmprojectmainc-query',
    loadAction: 'prmprojectmainc-load',
    addAction: 'prmprojectmainc-add',
    modifyAction: 'prmprojectmainc-modify',
    deleteAction: 'prmprojectmainc-delete',
    exportXlsAction: "prmprojectmainc-exportxls",
    prmDetailType: "PRM_PROGRESS_DETAIL",
    afterInit: function () {
        var me = this;
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        var view = me.view;
        view.getCmp("prmProjectMainCDto->state").sotValue("0");
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
    },
    beforeModify: function () {
        var me = this;
        return true;
    },
    afterModify: function () {
        var me = this;
    },
    beforeSave: function () {
        var me = this;
        return me.validateProgressDate();
    },
    afterSave: function (retData) {
        var me = this;
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
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

        var callBack = function (subView) {
            var grid = subView.getQueryPanel().getCmp("resultPanel");
            var selectedRecords = grid.getSelectionModel().getSelection();
            if (selectedRecords.length == 1) {
                view.setUIStatus(Scdp.Const.UI_INFO_STATUS_NEW);
                view.getCmp("mainTab").setActiveTab(me.view.getEditPanel());
                var uuid = selectedRecords[0].get("uuid");
                var postData = {};
                postData.uuid = uuid;
                postData.prmDetailType = me.prmDetailType;
                Scdp.doAction("wrapper-projectmain-to-change", postData, function (result) {
                    view.sotValue(result, true);
                });
                return true;
            } else if (selectedRecords.length > 1) {
                Scdp.MsgUtil.info(Scdp.I18N.NO_RECORDS_SELECT);
                return false;
            } else {
                return true;
            }
        };
        var queryController = Ext.create("Projectmain.controller.PickProjectmainQueryController");
        Scdp.openNewWinByController(queryController, callBack, 'temp_icon_16');
        var queryExtraParams = {detailType: me.prmDetailType};
        queryController.view.getConditionPanel().queryExtraParams = queryExtraParams;
    },
    validateProgressDate: function () {
        var me = this;
        var view = me.view;

        var progressGrid = view.getCmp("prmProgressDetailPCDto");
        var progressItems = progressGrid.store.data.items;
        for (var i = 0; i < progressItems.length; i++) {
            var record = progressItems[i];
            var estBeginDate = record.get("schemingBeginDate");
            var estEndDate = record.get("schemingEndDate");
            if (Scdp.ObjUtil.isNotEmpty(estBeginDate) && Scdp.ObjUtil.isNotEmpty(estEndDate)) {
                if (estBeginDate > estEndDate) {
                    Scdp.MsgUtil.info(Erp.I18N.PROGRESS_DETAIL_BEGINDATE_ENDDATE_ERROR);
                    return false;
                }
            }
            var actBeginDate = record.get("actualBeginDate");
            var actEndDate = record.get("actualEndDate");
            if (Scdp.ObjUtil.isNotEmpty(actBeginDate) && Scdp.ObjUtil.isNotEmpty(actEndDate)) {
                if (actBeginDate > actEndDate) {
                    Scdp.MsgUtil.info(Erp.I18N.PROGRESS_DETAIL_BEGINDATE_ENDDATE_ACTUAL_ERROR);
                    return false;
                }
            }
        }
        return true;
    }
});