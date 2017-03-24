Ext.define("Prmorgrolefilter.controller.PrmorgrolefilterController", {
	extend: 'Scdp.mvc.AbstractCrudController',
	viewClass: 'Prmorgrolefilter.view.PrmorgrolefilterView',
    uniqueValidateFields: [],
	extraEvents: [],
	dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.prmorgrolefilter.dto.ScdpOrgDto',
	queryAction: 'prmorgrolefilter-query',
	loadAction: 'prmorgrolefilter-load',
	addAction: 'prmorgrolefilter-add',
	modifyAction: 'prmorgrolefilter-modify',
	deleteAction: 'prmorgrolefilter-delete',
    exportXlsAction:"prmorgrolefilter-exportxls",
	afterInit: function () {
        var me = this;

        me.view.getCmp('prmOrgRoleFilterDto').rowAddFn = function () {
            var view = me.view;
            var callBack = function (subView) {
                var results = subView.getResultPanel(),
                    selectedRecords = results.getSelectionModel().getSelection(),
                    data = [],
                    editGrid = view.getCmp("prmOrgRoleFilterDto");

                if (selectedRecords.length > 0) {
                    //Get the user info form selected Records.
                    Ext.Array.each(selectedRecords, function (item) {
                        var record = {};
                        record.tableName =  item.get("tableNames")
                        data.push(record);
                    });

                    Ext.Array.each(data, function (record) {
                        var hasData = false;
                        for (var j = 0; j < editGrid.store.data.items.length; j++) {
                            if (editGrid.store.data.items[j].data.tableName == record.tableName) {
                                hasData = true;
                                break;
                            }
                        }
                        if (!hasData) {
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
                    })
                }
                return true;
            };
            var controller = Ext.create("Prmrolefilter.controller.QueryfilterController");
            Scdp.openNewWinByController(controller, callBack, 'user_icon_16', "查找过滤表", "选择过滤表");
        };
},
beforeAdd: function () {
var me = this;
return true;
},
afterAdd: function () {
var me = this;
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
	}
});