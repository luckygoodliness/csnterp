Ext.define('OperateReport.view.OperateReportSkhzbKhzView', {
    extend: 'Scdp.mvc.AbstractReportView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/operate/report',
    cpHeight: 80,
    epHeight: 200,
    allowNullConditions: true,
    queryLayoutFile: 'operatereport-skhz-khb-query-layout.xml',
    initComponent: function () {
        var me = this;
        me.callParent(arguments);
    },
    afterInit: function () {
        var me = this;

        me.getCmp("queryPanel->countryCode").sotValue("CN");
        me.getCmp("queryPanel->countryCode").originalValue = me.getCmp("queryPanel->countryCode").gotValue();

        var now = new Date();
        var year = now.getFullYear();
        var month = now.getMonth() + 1;
        me.getCmp("year").sotValue(year);
        me.getCmp("year").originalValue = me.getCmp("year").gotValue();
        me.getCmp("month").sotValue(month);
        me.getCmp("month").originalValue = me.getCmp("month").gotValue();

        var departMent = Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_NAME);
        if (departMent != "总经理办公室" && departMent != "运营管理部" && departMent != "计划财务部") {
            var contractorOfficeCmp = me.getCmp("contractorOffice");
            contractorOfficeCmp.putValue(Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE));
            contractorOfficeCmp.setReadOnly(true);
            me.getCmp("contractorOfficeDesc").originalValue = me.getCmp("contractorOfficeDesc").gotValue();
            contractorOfficeCmp.originalValue = contractorOfficeCmp.gotValue();
        }

        if (Scdp.ObjUtil.isNotEmpty(me.controller.actionParams)) {
            me.controller.doQuery();
        }
    }
});