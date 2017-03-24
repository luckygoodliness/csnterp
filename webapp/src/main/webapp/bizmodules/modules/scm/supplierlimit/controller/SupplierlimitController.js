Ext.define("Supplierlimit.controller.SupplierlimitController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Supplierlimit.view.SupplierlimitView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'batchSetting', name: 'click', fn: 'batchSetting'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.supplierlimit.dto.ScmSupplierLimitDto',
    queryAction: 'supplierlimit-query',
    loadAction: 'supplierlimit-load',
    addAction: 'supplierlimit-add',
    modifyAction: 'supplierlimit-modify',
    deleteAction: 'supplierlimit-delete',
    exportXlsAction: "supplierlimit-exportxls",
    afterInit: function () {
        var me = this;
        me.callParent(arguments);
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        me.initialize();
        me.resetEditToolbar(false);
    },
    beforeCopyAdd: function () {
        var me = this;
        return true;
    },
    afterCopyAdd: function () {
        var me = this;
        me.initialize();


    },
    beforeModify: function () {
        var me = this;
        return true;
    },
    afterModify: function () {
        var me = this;
        me.resetEditToolbar(false);
    },
    beforeSave: function () {
        var me = this;
        me.resetEditToolbar(true);
        return me.validation();
    },
    afterSave: function (retData) {
        var me = this;
        me.callParent(arguments);
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        me.callParent(arguments);
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
        me.resetEditToolbar(true);
        me.callParent(arguments);
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
    //点击批量设置按钮
    batchSetting: function () {
        var me = this;
        var view = this.view;
        var callBack = function (subView) {
            var supplierLimitDetailGrid = view.getCmp("scmSupplierLimitDetailDto");
            var count = supplierLimitDetailGrid.getStore().getCount();
            if (count > 0) {
                for (var i = 0; i < count; i++) {
                    var rowData = supplierLimitDetailGrid.getStore().getAt(i).set("maxAmount", subView.getCmp("maxAmount").gotValue());
                    var rowData = supplierLimitDetailGrid.getStore().getAt(i).set("maxVolume", subView.getCmp("maxVolume").gotValue());
                }
            }
            win.close();
        };
        var form = Ext.widget("JForm", {
            height: 150,
            width: 300,
            layout: 'form',
            bodyPadding: '55 10 10 10',
            items: [
                {
                    xtype: 'JInt',
                    cid: 'maxVolume',
                    allowBlank: false,
                    minValue: '0',
                    fieldLabel: '最大合同数量'
                },
                {
                    xtype: 'JDec',
                    cid: 'maxAmount',
                    allowBlank: false,
                    minValue: '0.00',
                    fieldLabel: '最大合同金额'
                }
            ]
        });
        win = Scdp.openNewWinByView(form, callBack, 'temp_icon_16', '批量设置', "确认");
    },
    initialize: function () {
        var view = this.view;
        view.getCmp("scmSupplierLimitDto->state").sotValue("0");
    },
    pickContract: function () {//相关合同弹出窗
        var me = this;
        var view = me.view;
        var callBack = function (subView, isCancel) {
            if (isCancel) {
                return;
            }
            var grid = subView.getQueryPanel().getCmp("resultPanel");
            var selectedRecords = grid.getSelectionModel().getSelection();
            if (selectedRecords.length > 0) {
                if (selectedRecords.length != 1) {
                    var supplier = '';
                    var subjectCode = '';
                    var supplierNull = false;
                    var isSameSupplier = true;
                    //var isSameSubjectCode = true;
                    //var isSameContractNature = true;
                    Ext.Array.each(selectedRecords, function (a) {
                        var rowData = a.data;
                        if (Scdp.ObjUtil.isNotEmpty(rowData)) {
                            if (Scdp.ObjUtil.isEmpty(rowData.supplierCode)) {
                                supplierNull = true;
                            } else if (Scdp.ObjUtil.isNotEmpty(supplier) && supplier != rowData.supplierCode) {
                                isSameSupplier = false;
                            }
                            supplier = rowData.supplierCode;
                        }
                    });
                    me.addData(selectedRecords, me);
                    subView.up("window").close();
                } else {
                    me.addData(selectedRecords, me);
                    return true;
                }

            } else {
                return true;
            }

        };
        var notInRow = "";
        var supplierLimitDetailGrid = view.getCmp("scmSupplierLimitDetailDto");
        var count = supplierLimitDetailGrid.getStore().getCount();
        if (count > 0) {
            for (var i = 0; i < count; i++) {
                var rowData = supplierLimitDetailGrid.getStore().getAt(i).data;
                notInRow = notInRow + "'" + rowData.scmSupplierId + "',";
            }
        }
        notInRow = notInRow + "'.'";

        var queryController = Ext.create("Supplierinfor.controller.PickSupplierQueryController");
        var param = {
            notInRow: notInRow
        };
        queryController.actionParams = param;
        Scdp.openNewWinByController(queryController, callBack, 'temp_icon_16', '供应商选择', null, true);
    },
    resetEditToolbar: function (u) {
        var me = this;
        var view = me.view;
        view.getCmp('editToolbar->batchSetting').setDisabled(u);

    },
    validation: function () {
        var me = this;
        var view = me.view;
        var supplierLimitDetailGrid = view.getCmp("scmSupplierLimitDetailDto");
        var count = supplierLimitDetailGrid.getStore().getCount();
        if (count == 0) {
            Scdp.MsgUtil.info("子表不能为空");
            return false;
        }else{
            return true;
        }
    },
    addData: function (selectedRecords, controller) {
        var me = controller;
        var view = me.view;
        var supplierLimitDetailGrid = view.getCmp("scmSupplierLimitDetailDto");
        Ext.Array.each(selectedRecords, function (a) {
            var rowData = a.data;
            var c = supplierLimitDetailGrid.getStore(),
                d = Ext.ModelManager.create({}, supplierLimitDetailGrid.store.model.modelName);
            if (Scdp.ObjUtil.isNotEmpty(rowData)) {
                if (Scdp.ObjUtil.isNotEmpty(rowData.uuid)) {
                    d.set("scmSupplierId", rowData.uuid);
                    d.set("scmSupplierName", rowData.completeName);
                    d.set("curVolume", rowData.curVolume);
                    d.set("curAmount", rowData.curAmount);
                    d.set("supplierType", rowData.supplierGenreName);
                }
            }
            supplierLimitDetailGrid.store.insert(c.getCount(), d);
            supplierLimitDetailGrid.getSelectionModel().select(c.getCount() - 1)
        });

    }
});