Ext.define("Materialclass.controller.MaterialclassController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Materialclass.view.MaterialclassView',
    uniqueValidateFields: ["code"],
    extraEvents: [{cid: 'batchDelete', name: 'click', fn: 'batchDeleteAction'}],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.materialclass.dto.ScmMaterialClassDto',
    queryAction: 'materialclass-query',
    loadAction: 'materialclass-load',
    addAction: 'materialclass-add',
    modifyAction: 'materialclass-modify',
    deleteAction: 'materialclass-delete',
    exportXlsAction: "materialclass-exportxls",
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
        me.view.getCmp("scmMaterialClassDto->code").sotEditable(false);
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