Ext.define("Scmoverduereceivables.controller.ScmoverduereceivablesController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Scmoverduereceivables.view.ScmoverduereceivablesView',
    uniqueValidateFields: [],
    extraEvents: [],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.scmoverduereceivables.dto.ScmOverdueReceivablesDto',
    queryAction: 'scmoverduereceivables-query',
    loadAction: 'scmoverduereceivables-load',
    addAction: 'scmoverduereceivables-add',
    modifyAction: 'scmoverduereceivables-modify',
    deleteAction: 'scmoverduereceivables-delete',
    exportXlsAction: "scmoverduereceivables-exportxls",
    afterInit: function () {
        var me = this;
        me.doQuery();
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
        me.doQuery();
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