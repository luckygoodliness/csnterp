Ext.define("Businessbidinfo.controller.BusinessbidinfoQueryController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Businessbidinfo.view.BusinessbidinfoQueryView',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'businessbidinfo-query',
    exportXlsAction: "businessbidinfo-exportxls",
    doQuery:function() {
        var me = this;
        window.ACTIVE_WINDOW=me.view.up('window');
        me.callParent(arguments);
        var fieldValue = me.view.getCmp("field").value;
        var selectResults = me.view.getResultPanel();
        if(!fieldValue.length==0)
        {
            for (var i = 1; i < selectResults.resultColumns.length+1; i++) {
                var record = selectResults.getColumnByIndex(i);
                //判断哪个字段隐藏
                if (fieldValue.indexOf(record.dataIndex.toUpperCase()) < 0) {
                    record.hide();
                } else {
                    record.show();
                }
            }
        }
    }
})