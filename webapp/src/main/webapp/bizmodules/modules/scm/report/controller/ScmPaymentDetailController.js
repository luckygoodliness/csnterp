Ext.define("ScmReport.controller.ScmPaymentDetailController", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'ScmReport.view.ScmPaymentDetailView',
    queryAction: 'scmReport-query',
    extraEvents: [
        {cid: 'dateStart', name: 'change', fn: 'dateStartChange'}
        ,
        {cid: 'dateOver', name: 'change', fn: 'dateOverChange'}
        ,
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
             }
             var month = actionParams.month;
             if (Scdp.ObjUtil.isNotEmpty(month)) {
             view.getCmp("month").sotValue(month);
             view.getCmp("month").resetOriginalValue();
             }*/
            var dateStart = actionParams.dateStart;
            if (Scdp.ObjUtil.isNotEmpty(dateStart)) {
                view.getCmp("dateStart").sotValue(dateStart);
            }
            var dateOver = actionParams.dateOver;
            if (Scdp.ObjUtil.isNotEmpty(dateOver)) {
                view.getCmp("dateOver").sotValue(dateOver);
            }

            view.getCmp("dateStartHidden").sotValue(Erp.Util.getDateTimeForStandard(view.getCmp("dateStart").getValue()));
            view.getCmp("dateOverHidden").sotValue(Erp.Util.getDateTimeForStandard(view.getCmp("dateOver").getValue()));

            me.doQuery();
        }
    },
    dateStartChange: function () {
        var me = this;
        var view = me.view;
        if (Erp.Util.isDate(view.getCmp("dateStart").getValue())) {
            view.getCmp("dateStartHidden").sotValue(Erp.Util.getDateTimeForStandard(view.getCmp("dateStart").getValue()));
        }
        else {
            view.getCmp("dateStartHidden").sotValue("");
        }
    },
    dateOverChange: function () {
        var me = this;
        var view = me.view;
        if (Erp.Util.isDate(view.getCmp("dateOver").getValue())) {
            if (Erp.Util.getDateTimeForStandard(view.getCmp("dateOver").getValue()).indexOf(" 00:00:00") != -1) {
                view.getCmp("dateOver").sotValue(Erp.Util.getDateTimeForStandard(view.getCmp("dateOver").getValue()).replace("00:00:00", "23:59:59"));
            }
            view.getCmp("dateOverHidden").sotValue(Erp.Util.getDateTimeForStandard(view.getCmp("dateOver").getValue()));
        }
        else {
            view.getCmp("dateOverHidden").sotValue("");
        }
    },
    doReturn: function () {
        var menuCode = "SCM_PAYMENT_DETAIL";
        Scdp.openNewModuleByMenuCode(menuCode, null, "MENU_ITEM_CTL", true);
    }
});