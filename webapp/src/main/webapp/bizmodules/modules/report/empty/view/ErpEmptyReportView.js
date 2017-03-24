Ext.define('ErpReport.view.ErpEmptyReportView', {
    extend: 'Scdp.mvc.AbstractView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/report/empty',
    //aHeight: 400,  //指定内容面板高度
    //aWidth: 850,  //指定内容面板宽度
    hideScroll: true,
    layoutFile: 'erp-empty-report-layout.xml',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        var mainPanel = me.getCmp("mainPanel");
        var actionParams = me.controller.actionParams;
        if (actionParams.reportFileName) {
            mainPanel.add({
                xtype: 'JPanel',
                cid: 'resultPanel',
                margin: '2 4 2 3 ',
                flex: 1,
                html: '<iframe id="" src="' + Erp.Util.getReportBasePath(actionParams.reportFileName) + '" frameborder="1" width="100%" height="100%" scrolling="auto"></iframe>'

            });
        }
    }
});