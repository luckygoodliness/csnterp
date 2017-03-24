Ext.define('FadReport.view.PrmReceiptsTotalView', {
    extend: 'Scdp.mvc.AbstractReportView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/report',
    cpHeight: 60,
    epHeight: 200,
    allowNullConditions: true,
    queryLayoutFile: 'prm-receipts-total-query-layout.xml',
    initComponent: function () {
        var me = this;
        me.callParent(arguments);
    },
    afterInit: function () {
        var me = this;

        var queryToolbar = me.getCmp('conditionToolbar');
        queryToolbar.add({
            xtype: 'button',
            text: '发送微信',
            cid: 'btnSendWeixin',
            iconCls: 'temp_icon_16'
        });
    }
});