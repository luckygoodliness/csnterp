Ext.define("FadReport.controller.OperateReport1Controller", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'FadReport.view.OperateReport1View',
    queryAction: 'fadReport-query',
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
            var officeId = actionParams.officeId;
            if (Scdp.ObjUtil.isNotEmpty(officeId)) {
                view.getCmp("officeId").putValue(officeId);
                view.getCmp("officeId").resetOriginalValue();
            }
            /*var year = actionParams.year;
             if (Scdp.ObjUtil.isNotEmpty(year)) {
             view.getCmp("year").sotValue(year);
             view.getCmp("year").resetOriginalValue();
             }*/
            /*var month = actionParams.month;
             if (Scdp.ObjUtil.isNotEmpty(month)) {
             view.getCmp("month").sotValue(month);
             view.getCmp("month").resetOriginalValue();
             }*/
            /*var dateStart = actionParams.dateStart;
             if (Scdp.ObjUtil.isNotEmpty(dateStart)) {
             view.getCmp("dateStart").sotValue(dateStart);
             }
             var dateOver = actionParams.dateOver;
             if (Scdp.ObjUtil.isNotEmpty(dateOver)) {
             view.getCmp("dateOver").sotValue(dateOver);
             }*/
            me.doQuery();
        }
    },
    doReturn: function () {
        var menuCode = "ERP_OP_REPORT_TEST";
        Scdp.openNewModuleByMenuCode(menuCode, null, "MENU_ITEM_CTL", true);
    }
});