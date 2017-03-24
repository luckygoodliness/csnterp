Ext.define("FadReport.controller.FadPayDetailQueryController", {
    extend: 'Scdp.mvc.AbstractReportController',
    viewClass: 'FadReport.view.FadPayDetailQueryView',
    queryAction: 'fad_pay_detail-query',
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