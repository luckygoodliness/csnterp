Ext.define("Responsibledepartment.controller.ResponsibledepartmentController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Responsibledepartment.view.ResponsibledepartmentView',
    uniqueValidateFields: ["responsibleDepartment","isProject"],
    extraEvents: [],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.responsibledepartment.dto.ScmResponsibleDepartmentDto',
    queryAction: 'responsibledepartment-query',
    loadAction: 'responsibledepartment-load',
    addAction: 'responsibledepartment-add',
    modifyAction: 'responsibledepartment-modify',
    deleteAction: 'responsibledepartment-delete',
    exportXlsAction: "responsibledepartment-exportxls",
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