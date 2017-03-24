Ext.define('Monitory.view.MonitoryView', {
    extend: 'Scdp.mvc.AbstractReportView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/nonprm/monitory',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 80,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'monitory-query-layout.xml',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        var now = new Date();
        var year = me.getCmp("year");
        year.sotValue(now.getFullYear());
        var queryToolbar = me.getCmp('conditionToolbar');
        queryToolbar.add({
                text: '调整年终评估',
                cid: 'btnEditMonitorY',
                iconCls: 'temp_icon_16'
            }
        );
    }
});