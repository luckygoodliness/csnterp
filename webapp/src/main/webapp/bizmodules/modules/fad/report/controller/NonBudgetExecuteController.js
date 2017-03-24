Ext.define("FadReport.controller.NonBudgetExecuteController", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'FadReport.view.NonBudgetExecuteView',
    queryAction: 'non_budget_execute-query',
    extraEvents: [
    ],
    beforeQuery: function () {
        var me = this;
        var view = this.view;
        return true;
    },

    afterInit: function () {
        var me = this;
        var view = me.view;
    }
})
;