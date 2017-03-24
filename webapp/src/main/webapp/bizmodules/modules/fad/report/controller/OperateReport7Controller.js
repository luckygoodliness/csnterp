Ext.define("FadReport.controller.OperateReport7Controller", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'FadReport.view.OperateReport7View',
    queryAction: 'fadReport-query',
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
            var year = actionParams.year;
            if (Scdp.ObjUtil.isNotEmpty(year)) {
                view.getCmp("year").sotValue(year);
                view.getCmp("year").resetOriginalValue();
            }

            var buildRegion = Erp.Util.isNullReturnEmpty(actionParams.buildRegion);
            if (Scdp.ObjUtil.isNotEmpty(buildRegion)) {
                view.getCmp("countryCode").sotValue("CN");
                view.getCmp("countryCode").resetOriginalValue();

                var postData = {
                    field: "stateDesc",
                    sql: "SELECT STATE_DESC FROM FM_STATE WHERE (NVL(IS_VOID, 0) <> 1) AND STATE_CODE='" + buildRegion + "'"
                };
                var actionResult = Scdp.doAction("certificate-getAnyValue", postData, null, null, true, false);
                var value = Erp.Util.isNullReturnEmpty(actionResult.value);
                if (Scdp.ObjUtil.isNotEmpty(value)) {
                    view.getCmp("buildRegion").sotValue(value);
                    view.getCmp("buildRegion").resetOriginalValue();
                }
            }
            var affiliatedInstitutions = Erp.Util.isNullReturnEmpty(actionParams.affiliatedInstitutions);
            if (Scdp.ObjUtil.isNotEmpty(affiliatedInstitutions)) {
                view.getCmp("affiliatedInstitutions").sotValue(affiliatedInstitutions);
                view.getCmp("affiliatedInstitutions").resetOriginalValue();
            }

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
    }/*,
     doReturn: function () {
     var menuCode = "ERP_OP_REPORT_YGB_10";
     Scdp.openNewModuleByMenuCode(menuCode, null, "MENU_ITEM_CTL", true);
     }*/
});