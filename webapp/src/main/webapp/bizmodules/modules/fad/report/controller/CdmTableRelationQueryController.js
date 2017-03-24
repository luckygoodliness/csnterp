Ext.define("FadReport.controller.CdmTableRelationQueryController", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'FadReport.view.CdmTableRelationView',
    queryAction: 'cdm_menu_table_relation-query',
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
            var tableName = actionParams.tableName;
            if (Scdp.ObjUtil.isNotEmpty(tableName)) {
                view.getCmp("tableName").sotValue(tableName);
            }
            me.doQuery();
        }
    }
})
;