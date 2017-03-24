Ext.define("OperateReport.controller.OperateReportSkhzbKhzController", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'OperateReport.view.OperateReportSkhzbKhzView',
    queryAction: 'operateReport-query',
    extraEvents: [],
    beforeQuery: function () {
        var me = this;
        var view = me.view;
        return true;
    },
    afterInit: function () {
        var me = this;
        var view = me.view;
        var actionParams = me.actionParams;

        if (Scdp.ObjUtil.isNotEmpty(actionParams)) {
            var year = actionParams.year;
            if (Scdp.ObjUtil.isNotEmpty(year)) {
                view.getCmp("year").sotValue(year);
            }
            var contractorOffice = actionParams.contractorOffice;
            if (Scdp.ObjUtil.isNotEmpty(contractorOffice)) {
                view.getCmp("contractorOffice").putValue(contractorOffice);
            }
            var fadSubjectCode = actionParams.fadSubjectCode;
            if (Scdp.ObjUtil.isNotEmpty(fadSubjectCode)) {
                view.getCmp("fadSubjectCode").sotValue(fadSubjectCode);
            }
            //me.doQuery();
        }
    }
});