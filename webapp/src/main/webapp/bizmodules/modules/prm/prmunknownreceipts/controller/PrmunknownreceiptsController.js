Ext.define("Prmunknownreceipts.controller.PrmunknownreceiptsController", {
    extend: 'ErpMvc.controller.ErpAbstractCrudController',
    viewClass: 'Prmunknownreceipts.view.PrmunknownreceiptsView',
    uniqueValidateFields: [],
    extraEvents: [],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.prm.prmunknownreceipts.dto.PrmUnknownReceiptsDto',
    queryAction: 'prmunknownreceipts-query',
    loadAction: 'prmunknownreceipts-load',
    addAction: 'prmunknownreceipts-add',
    modifyAction: 'prmunknownreceipts-modify',
    deleteAction: 'prmunknownreceipts-delete',
    exportXlsAction: "prmunknownreceipts-exportxls",
    afterInit: function () {
        var me = this;
        me.callParent(arguments);
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
        me.callParent(arguments);
    },
    beforeLoadItem: function () {
        var me = this;
        return true;
    },
    afterLoadItem: function () {
        var me = this;
        me.callParent(arguments);
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
    }
});