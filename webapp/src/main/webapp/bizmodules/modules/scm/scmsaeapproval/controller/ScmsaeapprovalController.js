Ext.define("Scmsaeapproval.controller.ScmsaeapprovalController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Scmsaeapproval.view.ScmsaeapprovalView',
    uniqueValidateFields: [],
    extraEvents: [
        {cid: 'scmSaeApprovalDto->curYear', name: 'change', fn: 'curYearFn'},
        {cid: 'showAllDetail', name: 'change', fn: 'showAllDetail'},
        {cid: 'scmSaeApprovalDDto', name: 'select', fn: 'showAllDetail'},
        {cid: 'addSupplierOfLevelC', name: 'click', fn: 'addSupplierOfLevelCFn'}
    ],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.scmsaeapproval.dto.ScmSaeApprovalDto',
    queryAction: 'scmsaeapproval-query',
    loadAction: 'scmsaeapproval-load',
    addAction: 'scmsaeapproval-add',
    modifyAction: 'scmsaeapproval-modify',
    deleteAction: 'scmsaeapproval-delete',
    exportXlsAction: "scmsaeapproval-exportxls",
    afterInit: function () {
        var me = this;
        me.callParent(arguments);
        me.view.getCmp("scmSaeApprovalDDto").on('beforeedit', function (editor, eventObj) {
            var field = eventObj.field;
            var record = eventObj.record;
            //日常考评 级别不可编辑
            if (record.data.addFrom == 1 && field == "supplierGenre")
                return false;
        });
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        me.view.getCmp("scmSaeApprovalDto->state").sotValue("0");
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
        me.callParent(arguments);
        var uuid = me.view.getCmp("scmSaeApprovalDto->uuid").gotValue();
        me.loadItem(uuid);
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        me.callParent(arguments);
        me.showAllDetail();
    },
    beforeCancel: function () {
        var me = this;
        return true;
    },
    afterCancel: function () {
        var me = this;
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
    addFn: function () {
        var me = this;
        var view = me.view;
        var Year = view.getCmp("scmSaeApprovalDto->curYear").gotValue();
        if (Scdp.ObjUtil.isEmpty(Year)) {
            Scdp.MsgUtil.info("年度不能为空");
            return false;
        }
        var notInRow = "";
        var supplierLimitDetailGrid = view.getCmp("scmSaeApprovalDDto");
        var count = supplierLimitDetailGrid.getStore().getCount();
        if (count > 0) {
            for (var i = 0; i < count; i++) {
                var rowData = supplierLimitDetailGrid.getStore().getAt(i).data;
                notInRow = notInRow + "'" + rowData.supplierId + "',";
            }
        }
        notInRow = notInRow + "'.'";
        var queryController = Ext.create("Scmsaeapproval.controller.ScmsaeapprovalQueryController");
        queryController.actionParams = {
            curYear: me.view.getCmp("scmSaeApprovalDto->curYear").gotValue(),
            notInRow: notInRow
        }
        var callBack = function (subView, isCancel) {
            var selectedRecords = subView.getCmp('scmSaeResultDto').getSelectedRecords();
            if (selectedRecords.length > 0) {
                var dtoGrid = view.getCmp("scmSaeApprovalDDto");
                Ext.Array.each(selectedRecords, function (a) {
                    var rowData = a.data;
                    var c = dtoGrid.getStore(), d = Ext.ModelManager.create({}, dtoGrid.store.model.modelName);
                    if (Scdp.ObjUtil.isNotEmpty(rowData)) {
                        d.set("supplierId", rowData.supplierId);
                        d.set("supplierName", rowData.supplierName);
                        d.set("comprehensive", rowData.comprehensive);
                        d.set("supplierGenre", rowData.cLevel);
                        d.set("addFrom", 0);//年度考评
                        d.set("seqNo", Scdp.getMaxSnoInStore(c) + 1);
                        dtoGrid.store.insert(c.getCount(), d);
                    }
                });
            }
            subView.up('window').close();
        }
        Scdp.openNewWinByController(queryController, callBack, 'temp_icon_16', '选择考评对象');
    },
    addSupplierOfLevelCFn: function () {//添加C级供方
        var me = this;
        var view = me.view;
        var callBack = function (subView, isCancel) {
            if (isCancel) {
                return;
            }
            var grid = subView.getQueryPanel().getCmp("resultPanel");
            var selectedRecords = grid.getSelectionModel().getSelection();
            if (selectedRecords.length > 0) {
                me.addData(selectedRecords, me);
                subView.up("window").close();
            } else {
                return true;
            }
        };

        var connStr = "";
        var scmSaeObjectGrid = view.getCmp("scmSaeApprovalDDto");
        var count = scmSaeObjectGrid.getStore().getCount();
        if (count > 0) {
            for (var i = 0; i < count; i++) {
                var rowData = scmSaeObjectGrid.getStore().getAt(i).data;
                if (i == 0) {
                    connStr = "'" + rowData.supplierId + "'";
                } else if (Scdp.ObjUtil.isNotEmpty(rowData.supplierId) && connStr.indexOf(rowData.supplierId) < 0) {
                    connStr += ",'" + rowData.supplierId + "'";
                }
            }
        }

        var queryController = Ext.create("Scmsae.controller.ScmsaeSupplierQueryController");
        var param = {
            connStr: connStr
        };
        queryController.actionParams = param;
        Scdp.openNewWinByController(queryController, callBack, 'temp_icon_16', '选择考评供应商', null, true);
    },
    addData: function (selectedRecords, controller) {
        var me = controller;
        var view = me.view;
        var scmSaeApprovalDGrid = view.getCmp("scmSaeApprovalDDto");
        Ext.Array.each(selectedRecords, function (record) {
            var rowData = record.data;
            var c = scmSaeApprovalDGrid.getStore(), d = Ext.ModelManager.create({}, scmSaeApprovalDGrid.store.model.modelName);
            if (Scdp.ObjUtil.isNotEmpty(rowData)) {
                d.set("supplierId", rowData.supplierId);
                d.set("supplierName", rowData.supplierName);
                d.set("comprehensive", rowData.comprehensive);
                d.set("supplierGenre", "C");
                d.set("addFrom", 1);//日常考评
                d.set("seqNo", Scdp.getMaxSnoInStore(c) + 1);
                scmSaeApprovalDGrid.store.insert(c.getCount(), d);
            }
        });
    },
    //显示全部
    showAllDetail: function () {
        var me = this;
        var view = this.view;
        var checkBtn = me.view.getCmp("showAllDetail");
        var scmSaeApprovalDDto = me.view.getCmp("scmSaeApprovalDDto").getSelectedRecords();
        var supplier = '';
        Ext.Array.each(scmSaeApprovalDDto, function (a) {
            var rowData = a.data;
            supplier = rowData.supplierId;
        });
        var checked = checkBtn.gotValue();
        var store = view.getCmp("scmSaeObjectDto").getStore();
        store.filterBy(function (record) {
            if (checked && checked == 1) {
                return true
            } else {
                if (record.data.supplierId == supplier) {
                    return true;
                } else {
                    return false;
                }
            }

        });
        var selectedReqDetailGrid = me.view.getCmp("scmSaeObjectDto");
        selectedReqDetailGrid.sotEditable(true);
    },
    curYearFn: function () {
        var me = this;
        var view = me.view;
        var scmSaeApprovalDDto = view.getCmp("scmSaeApprovalDDto");
        scmSaeApprovalDDto.clearData();
        var Year = view.getCmp("scmSaeApprovalDto->curYear").gotValue();
        view.getCmp("scmSaeApprovalDto->title").sotValue(Year + "年度合格供方审批");
    }

});