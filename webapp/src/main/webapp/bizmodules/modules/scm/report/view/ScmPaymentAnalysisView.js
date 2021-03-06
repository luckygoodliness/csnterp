Ext.define('ScmReport.view.ScmPaymentAnalysisView', {
    extend: 'Scdp.mvc.AbstractReportView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/scm/report',
    cpHeight: 60,
    epHeight: 200,
    allowNullConditions: true,
    queryLayoutFile: 'scmPaymentAnalysis-query-layout.xml',
    initComponent: function () {
        var me = this;
        me.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        /*me.getCmp("year").sotValue(Erp.Util.getCurrentYearForInt());
         me.getCmp("year").resetOriginalValue();
         me.getCmp("month").sotValue(Erp.Util.getCurrentMonthForInt());
         me.getCmp("month").resetOriginalValue();*/

        me.initTime();
        /*var departMent = Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_NAME);
         if (departMent != "总经理办公室" && departMent != "运营管理部" && departMent != "计划财务部") {
         me.getCmp("officeId").putValue(Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE));
         me.getCmp("officeId").resetOriginalValue();
         me.getCmp("officeId").setReadOnly(true);

         me.getCmp("officeIdDesc").sotValue(Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_NAME));
         me.getCmp("officeIdDesc").resetOriginalValue();
         }*/

        var orgCode = Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE);
        var orgName = Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_NAME);
        var postData = {orgCode: orgCode};
        Scdp.doAction("manager_dep-get", postData, function (result) {
            if (Erp.Util.isNullReturnEmpty(result.isManagerDep) == "0") {
                me.getCmp("officeId").putValue(orgCode);
                me.getCmp("officeId").resetOriginalValue();
                me.getCmp("officeId").setReadOnly(true);

                me.getCmp("officeIdDesc").sotValue(orgName);
                me.getCmp("officeIdDesc").resetOriginalValue();
            }
        });
    },
    initTime: function () {
        var me = this;
        me.getCmp("dateStart").sotValue(Erp.Util.getCurrentYear() + '-01-01');
        me.getCmp("dateOver").sotValue(Erp.Util.getCurrentYear() + '-' + Erp.Util.getCurrentMonth() + '-' + (new Date(Erp.Util.getCurrentYear(), Erp.Util.getCurrentMonth(), 0).getDate()).toString());
    }
});