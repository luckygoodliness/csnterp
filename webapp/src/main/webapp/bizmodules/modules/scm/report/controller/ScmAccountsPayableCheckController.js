Ext.define("ScmReport.controller.ScmAccountsPayableCheckController", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'ScmReport.view.ScmAccountsPayableCheckView',
    extraEvents: [],
    queryAction: 'scmReport-query',
    afterInit: function () {
        var me = this;
        var view = me.view;
        var actionParams = me.actionParams;

        if (Scdp.ObjUtil.isNotEmpty(actionParams)) {
            var supplierName = actionParams.supplierName;
            if (Scdp.ObjUtil.isNotEmpty(supplierName)) {
                view.getCmp("supplierName").sotValue(supplierName);
            }
            me.doQuery();
        }
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