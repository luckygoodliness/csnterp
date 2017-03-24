Ext.define("FadReport.controller.CdmMenuTableRelationQueryController", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'FadReport.view.CdmMenuTableRelationView',
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
    }
})
;