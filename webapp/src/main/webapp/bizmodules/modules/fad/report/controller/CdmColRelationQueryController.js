Ext.define("FadReport.controller.CdmColRelationQueryController", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'FadReport.view.CdmColRelationView',
    queryAction: 'cdm_col_relation-query',
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
        var actionParams = me.actionParams;
        if (Scdp.ObjUtil.isNotEmpty(actionParams)) {
            var columnUuid = actionParams.column_uuid;
            if (Scdp.ObjUtil.isNotEmpty(columnUuid)) {
                view.getCmp("columnUuid").sotValue(columnUuid);
            }
            me.doQuery();
        }
    }
})
;