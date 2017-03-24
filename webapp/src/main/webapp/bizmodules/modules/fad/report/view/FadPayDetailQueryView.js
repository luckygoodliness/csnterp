Ext.define('FadReport.view.FadPayDetailQueryView', {
    extend: 'Scdp.mvc.AbstractReportView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/report',
    cpHeight: 80,
    epHeight: 200,
    allowNullConditions: true,
    queryLayoutFile: 'fadPayDetail-query-layout.xml',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        var now = new Date();
        var year = now.getFullYear();
        me.getCmp("year").sotValue(year);
        me.getCmp("year").originalValue = me.getCmp("year").gotValue();
        var departMent = Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_NAME);
        if (departMent != "总经理办公室" && departMent != "供应链管理部" && departMent != "运营管理部" && departMent != "计划财务部") {
            var officeIdCmp = me.getCmp("officeId");
            officeIdCmp.putValue(Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE));
            officeIdCmp.setReadOnly(true);
            me.getCmp("officeIdDesc").originalValue = me.getCmp("officeIdDesc").gotValue();
            officeIdCmp.originalValue = officeIdCmp.gotValue();
        }
    }
});