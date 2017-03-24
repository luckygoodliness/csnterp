Ext.define("PrmReport.controller.PrmProjectCostTicketUpsideDownTotalReportController", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'PrmReport.view.PrmProjectCostTicketUpsideDownTotalReportView',
    queryAction: 'prmReport-query',
    extraEvents: [
        /*{cid: 'btnRtn', name: 'click', fn: 'doReturn'}*/
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
        /*if (Scdp.ObjUtil.isNotEmpty(view.getCmp("conditionToolbar->btnRtn"))) {
         view.getCmp("conditionToolbar->btnRtn").setVisible(false);
         }*/
        if (Scdp.ObjUtil.isNotEmpty(actionParams)) {
            /*if (Scdp.ObjUtil.isNotEmpty(view.getCmp("conditionToolbar->btnRtn"))) {
             view.getCmp("conditionToolbar->btnRtn").setVisible(true);
             }*/
            var contractorOffice = actionParams.contractorOffice;
            if (Scdp.ObjUtil.isNotEmpty(contractorOffice)) {
                view.getCmp("contractorOffice").putValue(contractorOffice);
                view.getCmp("contractorOffice").resetOriginalValue();
            }
            me.doQuery();
        }
    }/*,
     doReturn: function () {
     var menuCode = "PROJECTCOSTTICKETUPSIDEDOWNTOTALREPORT";
     Scdp.openNewModuleByMenuCode(menuCode, null, "MENU_ITEM_CTL", true);
     }*/
});