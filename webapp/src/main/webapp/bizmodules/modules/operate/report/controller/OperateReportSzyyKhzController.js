Ext.define("OperateReport.controller.OperateReportSzyyKhzController", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'OperateReport.view.OperateReportSzyyKhzView',
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
            var officeId = actionParams.officeId;
            if (Scdp.ObjUtil.isNotEmpty(officeId)) {
                view.getCmp("officeId").putValue(officeId);
            }
            var fadSubjectCode = actionParams.fadSubjectCode;
            if (Scdp.ObjUtil.isNotEmpty(fadSubjectCode)) {
                view.getCmp("fadSubjectCode").sotValue(fadSubjectCode);
            }
            //me.doQuery();
        }
    }
});