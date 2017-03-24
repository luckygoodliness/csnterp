Ext.define("Scmpricedatabase.controller.ScmPriceDatabaseController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Scmpricedatabase.view.ScmPriceDatabaseView',
    uniqueValidateFields: [],
    extraEvents: [],
    queryAction: 'scmpricedatabase-scmpricedatabasequery',
    exportXlsAction: "scmpricedatabase-exportxls",
    afterInit: function () {
        var me = this;
    },
    beforeAdd: function () {
        var me = this;
        return true;
    },
    afterAdd: function () {
        var me = this;
        me.initialize();

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
        me.resetEditToolbar(false);
    },
    beforeSave: function () {
        var me = this;
        me.resetEditToolbar(true);
        return me.validation();
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
        me.resetEditToolbar(true);
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
    },

});