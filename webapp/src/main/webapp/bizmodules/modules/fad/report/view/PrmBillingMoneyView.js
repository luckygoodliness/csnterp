Ext.define('FadReport.view.PrmBillingMoneyView', {
    extend: 'Scdp.mvc.AbstractReportView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/report',
    cpHeight: 60,
    epHeight: 200,
    allowNullConditions: true,
    queryLayoutFile: 'prmbillingmoney-query-layout.xml',
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
        //me.getCmp("dateStart").sotValue(Erp.Util.getCurrentYear() + '/' + Erp.Util.getCurrentMonth() + '/' + '01');
        //me.getCmp("dateOver").sotValue(Erp.Util.getDateForStandard(null));

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

        var queryToolbar = me.getCmp('conditionToolbar');
        queryToolbar.insert(2, {
            text: '返回',
            cid: 'btnRtn',
            iconCls: 'erp-return-back'
        });
        var actionParams = me.controller.actionParams;
        if (Scdp.ObjUtil.isEmpty(actionParams)) {
            me.getCmp("conditionToolbar->btnRtn").setVisible(false);
        }
    }
});