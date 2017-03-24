Ext.define("Companyplan.controller.CompanyplanController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Companyplan.view.CompanyplanView',
    uniqueValidateFields: [],
    extraEvents: [],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.operate.companyplan.dto.OperateCompanyPlanHDto',
    queryAction: 'companyplan-query',
    loadAction: 'companyplan-load',
    addAction: 'companyplan-add',
    modifyAction: 'companyplan-modify',
    deleteAction: 'companyplan-delete',
    exportXlsAction: "companyplan-exportxls",
    afterInit: function () {
        var me = this;
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        me.initialize();
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
        return true;
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

    //弹出部门选择窗体
    pickOffice: function () {
        var me = this;
        var view = me.view;
        var notInRow = "";
        var dtoGrid = view.getCmp("operateCompanyPlanCDto");
        for (var i = 0; i < dtoGrid.getStore().getCount(); i++) {
            if (Scdp.ObjUtil.isNotEmpty(dtoGrid.getStore().getAt(i).data.objectId))
                notInRow = notInRow + "'" + dtoGrid.getStore().getAt(i).data.objectId + "',"
        }
        notInRow = notInRow + "'.'";
        var param = {notInRow: notInRow};
        var queryController = Ext.create("Erpoffice.controller.OfficePickController");
        queryController.actionParams = param;
        var callBack = function (subView, isCancel) {
            if (isCancel) {
                return;
            }
            var grid = subView.getQueryPanel().getCmp("resultPanel");
            var selectedRecords = grid.getSelectedRecords();
            if (selectedRecords.length > 0) {
                Ext.Array.each(selectedRecords, function (a) {
                    var rowData = a.data;
                    var c = dtoGrid.getStore(), d = Ext.ModelManager.create({}, dtoGrid.store.model.modelName);
                    if (Scdp.ObjUtil.isNotEmpty(rowData)) {
                        d.set("objectId", rowData.orgCode);
                        d.set("officeName", rowData.orgName);
                        d.set("objectType", rowData.orgType);
                    }
                    dtoGrid.store.insert(c.getCount(), d);
                });
                return true;
            } else {
                return true;
            }
        };
        Scdp.openNewWinByController(queryController, callBack, 'temp_icon_16');
    },
    initialize: function () {
        //this.view.getCmp("operateCompanyPlanHDto->executionCycle").sotValue(1);
    }
});