Ext.define("Prmrolefilter.controller.QueryfilterController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Prmrolefilter.view.QueryfilterView',
    uniqueValidateFields: [],
    extraEvents: [],
    dtoClass: 'com.csnt.scdp.sysmodules.modules.sys.queryfilter.dto.ScdpQueryFilterDto',
    queryAction: 'queryfilter-query',
//    loadAction: 'queryfilter-load',
//    addAction: 'queryfilter-add',
//    modifyAction: 'queryfilter-modify',
//    deleteAction: 'queryfilter-delete',
//    exportXlsAction: "queryfilter-exportxls",
    afterInit: function () {
        var me = this;
        me.view.getCmp('beanName').sotValue('erp-role-filter-project');
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