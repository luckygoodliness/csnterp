Ext.define("PrmReport.controller.PrmProjectChangePlanSettlementController", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'PrmReport.view.PrmProjectChangePlanSettlementView',
    queryAction: 'prmReport-query',
    extraEvents: [
        {cid: 'officeIdDesc', name: 'blur', fn: 'officeIdDescChange'},
        {cid: 'projectCode', name: 'blur', fn: 'projectCodeChange'},
        {cid: 'projectName', name: 'blur', fn: 'projectNameChange'},
        {cid: 'btnRtn', name: 'click', fn: 'doReturn'}
    ],
    beforeQuery: function () {
        var me = this;
        var view = me.view;
        return true;
    },
    afterInit: function () {
        var me = this;
        var view = me.view;
    },
    officeIdDescChange: function () {
        var me = this;
        var view = me.view;
        if (Erp.Util.isNullReturnEmpty(view.getCmp("officeId").gotValue()).length > 0) {
            view.getCmp("projectCode").sotValue(null);
            view.getCmp("projectName").sotValue(null);
        }
    },
    projectCodeChange: function () {
        var me = this;
        var view = me.view;
        if (Erp.Util.isNullReturnEmpty(view.getCmp("projectCode").gotValue()).length > 0) {
            view.getCmp("officeId").sotValue(null);
            view.getCmp("officeIdDesc").sotValue(null);
            view.getCmp("projectName").sotValue(null);
        }
    },
    projectNameChange: function () {
        var me = this;
        var view = me.view;
        if (Erp.Util.isNullReturnEmpty(view.getCmp("projectName").gotValue()).length > 0) {
            view.getCmp("officeId").sotValue(null);
            view.getCmp("officeIdDesc").sotValue(null);
            view.getCmp("projectCode").sotValue(null);
        }
    },
    doReturn: function () {
        var menuCode = "PRM_PROJECT_CHANGE_PLAN_SETTLEMENT";
        Scdp.openNewModuleByMenuCode(menuCode, null, "MENU_ITEM_CTL", true);
    }
});