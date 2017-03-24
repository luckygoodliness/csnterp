Ext.define("PrmReport.controller.PrmProjectAccountTotalController", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'PrmReport.view.PrmProjectAccountTotalView',
    queryAction: 'prmReport-query',
    extraEvents: [
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
        var actionParams = me.actionParams;
        if (Scdp.ObjUtil.isNotEmpty(view.getCmp("conditionToolbar->btnRtn"))) {
            view.getCmp("conditionToolbar->btnRtn").setVisible(false);
        }
        if (Scdp.ObjUtil.isNotEmpty(actionParams)) {
            if (Scdp.ObjUtil.isNotEmpty(view.getCmp("conditionToolbar->btnRtn"))) {
                view.getCmp("conditionToolbar->btnRtn").setVisible(true);
            }
            var contractorOffice = actionParams.contractorOffice;
            if (Scdp.ObjUtil.isNotEmpty(contractorOffice)) {
                view.getCmp("contractorOffice").putValue(contractorOffice);
                view.getCmp("contractorOffice").resetOriginalValue();
            }
            var year = actionParams.year;
            if (Scdp.ObjUtil.isNotEmpty(year)) {
                view.getCmp("year").sotValue(year);
                view.getCmp("year").resetOriginalValue();
            }
            var month = actionParams.month;
            if (Scdp.ObjUtil.isNotEmpty(month)) {
                view.getCmp("month").sotValue(month);
                view.getCmp("month").resetOriginalValue();
            }
            me.doQuery();
        }
    },
    doReturn: function () {
        var menuCode = "PRM_PROJECT_ACCOUNTTOTAL";
        Scdp.openNewModuleByMenuCode(menuCode, null, "MENU_ITEM_CTL", true);
    }
});