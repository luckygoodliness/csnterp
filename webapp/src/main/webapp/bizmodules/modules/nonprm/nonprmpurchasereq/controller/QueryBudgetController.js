Ext.define("Nonprmpurchasereq.controller.QueryBudgetController", {
    extend: 'Scdp.mvc.AbstractCrudController',
    viewClass: 'Nonprmpurchasereq.view.QueryBudgetView',
    uniqueValidateFields: [],
    extraEvents: [],
    dtoClass: 'com.csnt.scdp.bizmodules.modules.nonprm.nonprmpurchasereq.dto.NonprmBudgetCDDto',
    queryAction: 'nonprmbudget-query',

    afterInit: function () {
        var me = this;
        if (me.actionParams) {

            var cid = me.actionParams.prmProjectMainId;
            me.view.getCmp("cid").sotValue(cid);
        }

//        me.view.getCmp('beanName').sotValue('sys-role-filter-project');
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