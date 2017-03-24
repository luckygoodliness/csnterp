Ext.define("Scmdepartmentbuyer.controller.ScmdepartmentbuyerController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Scmdepartmentbuyer.view.ScmdepartmentbuyerView',
    uniqueValidateFields: ["officeCode"],
    extraEvents: [],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.scmdepartmentbuyer.dto.ScmDepartmentBuyerDto',
    queryAction: 'scmdepartmentbuyer-query',
    loadAction: 'scmdepartmentbuyer-load',
    addAction: 'scmdepartmentbuyer-add',
    modifyAction: 'scmdepartmentbuyer-modify',
    deleteAction: 'scmdepartmentbuyer-delete',
    exportXlsAction: "scmdepartmentbuyer-exportxls",
    afterInit: function () {
        var me = this;
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