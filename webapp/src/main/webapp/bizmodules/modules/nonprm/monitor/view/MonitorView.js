Ext.define('Monitor.view.MonitorView', {
    extend: 'Scdp.mvc.AbstractReportView',
	modulePath: 'com/csnt/scdp/bizmodules/modules/nonprm/monitor',
	cpHeight: 60,
	epHeight: 200,
	allowNullConditions: false,
	queryLayoutFile: 'monitor-query-layout.xml',
	initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit:function(){
        var me = this;
        var now = new Date();
        var year = now.getFullYear();
        var month = now.getMonth();
        me.getCmp("year").sotValue(year);
        me.getCmp("month").sotValue(month+1);
        var queryToolbar = me.getCmp('conditionToolbar');
        queryToolbar.add({
            text: '实际发生录入',
            cid: 'btnMonitorM',
            iconCls: 'temp_icon_16'
        });
    }
});