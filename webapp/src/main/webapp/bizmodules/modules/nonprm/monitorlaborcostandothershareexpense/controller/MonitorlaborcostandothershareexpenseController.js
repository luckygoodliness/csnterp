Ext.define("Monitorlaborcostandothershareexpense.controller.MonitorlaborcostandothershareexpenseController", {
    extend: 'Scdp.mvc.AbstractController',
    viewClass: 'Monitorlaborcostandothershareexpense.view.MonitorlaborcostandothershareexpenseView',
    loadAction: 'monitorlaborcostandothershareexpense-load',
    extraEvents: [],
    afterInit: function () {
        var me = this;
        var view = me.view;

        var monitorLaborCostGrid = view.getCmp("monitorLaborCostDto");
        monitorLaborCostGrid.getCmp("toolbar->delRowBtn").hide();
        /*for (var i = 0; i < monitorLaborCostGrid.getStore().getCount(); i++) {
         var monitorLaborCostGridRowData = monitorLaborCostGrid.getStore().data.items[i];
         if (
         (monitorLaborCostGridRowData.get("year") != view.getCmp("monitorBaseDto->year").gotValue())
         ||
         (monitorLaborCostGridRowData.get("month") != view.getCmp("monitorBaseDto->month").gotValue())
         ) {
         monitorLaborCostGridRowData.set("money", null);
         }
         }*/
        monitorLaborCostGrid.store.on('update', function (store, record, operation, mdColumns) {
        });
        monitorLaborCostGrid.afterAddRow = function () {
            var monitorLaborCostGridRowDataCur = monitorLaborCostGrid.getCurRecord();
            if (monitorLaborCostGridRowDataCur == null) {
                return;
            }
            monitorLaborCostGridRowDataCur.set("seqNo", monitorLaborCostGrid.getStore().getCount());
            monitorLaborCostGridRowDataCur.set("year", view.getCmp("monitorBaseDto->year").gotValue());
            monitorLaborCostGridRowDataCur.set("month", view.getCmp("monitorBaseDto->month").gotValue());
            monitorLaborCostGridRowDataCur.set("officeId", view.getCmp("monitorBaseDto->officeId").gotValue());
        };
        monitorLaborCostGrid.afterCopyAddRow = function () {
            var monitorLaborCostGridRowDataCur = monitorLaborCostGrid.getCurRecord();
            if (monitorLaborCostGridRowDataCur == null) {
                return;
            }
            monitorLaborCostGridRowDataCur.set("seqNo", monitorLaborCostGrid.getStore().getCount());
        };
        monitorLaborCostGrid.afterDeleteRow = function () {
            for (var i = 0; i < monitorLaborCostGrid.getStore().getCount(); i++) {
                var monitorLaborCostGridRowData = monitorLaborCostGrid.getStore().data.items[i];
                monitorLaborCostGridRowData.set("seqNo", i + 1);
            }
        };

        var monitorOtherShareGrid = view.getCmp("monitorOtherShareDto");
        monitorOtherShareGrid.getCmp("toolbar->delRowBtn").hide();
        /*for (var i = 0; i < monitorOtherShareGrid.getStore().getCount(); i++) {
         var monitorOtherShareGridRowData = monitorOtherShareGrid.getStore().data.items[i];
         if (
         (monitorOtherShareGridRowData.get("year") != view.getCmp("monitorBaseDto->year").gotValue())
         ||
         (monitorOtherShareGridRowData.get("month") != view.getCmp("monitorBaseDto->month").gotValue())
         ) {
         monitorOtherShareGridRowData.set("money", null);
         }
         }*/
        monitorOtherShareGrid.store.on('update', function (store, record, operation, mdColumns) {
        });
        monitorOtherShareGrid.afterAddRow = function () {
            var monitorOtherShareGridRowDataCur = monitorOtherShareGrid.getCurRecord();
            if (monitorOtherShareGridRowDataCur == null) {
                return;
            }
            monitorOtherShareGridRowDataCur.set("seqNo", monitorOtherShareGrid.getStore().getCount());
            monitorOtherShareGridRowDataCur.set("year", view.getCmp("monitorBaseDto->year").gotValue());
            monitorOtherShareGridRowDataCur.set("month", view.getCmp("monitorBaseDto->month").gotValue());
            monitorOtherShareGridRowDataCur.set("officeId", view.getCmp("monitorBaseDto->officeId").gotValue());
        };
        monitorOtherShareGrid.afterCopyAddRow = function () {
            var monitorOtherShareGridRowDataCur = monitorOtherShareGrid.getCurRecord();
            if (monitorOtherShareGridRowDataCur == null) {
                return;
            }
            monitorOtherShareGridRowDataCur.set("seqNo", monitorOtherShareGrid.getStore().getCount());
        };
        monitorOtherShareGrid.afterDeleteRow = function () {
            for (var i = 0; i < monitorOtherShareGrid.getStore().getCount(); i++) {
                var monitorOtherShareGridRowData = monitorOtherShareGrid.getStore().data.items[i];
                monitorOtherShareGridRowData.set("seqNo", i + 1);
            }
        };
    }
});