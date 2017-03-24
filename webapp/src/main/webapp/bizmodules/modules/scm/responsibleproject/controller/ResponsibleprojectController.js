Ext.define("Responsibleproject.controller.ResponsibleprojectController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Responsibleproject.view.ResponsibleprojectView',
    uniqueValidateFields: [],
    extraEvents: [],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.scm.responsibleproject.dto.ScmResponsibleProjectDto',
    queryAction: 'responsibleproject-query',
    loadAction: 'responsibleproject-load',
    addAction: 'responsibleproject-add',
    modifyAction: 'responsibleproject-modify',
    deleteAction: 'responsibleproject-delete',
    exportXlsAction: "responsibleproject-exportxls",
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