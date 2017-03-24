Ext.define('PrmReport.view.PrmProjectChangePlanSettlementView', {
    extend: 'Scdp.mvc.AbstractReportView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/prm/report',
    cpHeight: 60,
    epHeight: 200,
    allowNullConditions: true,
    queryLayoutFile: 'prmprojectchangeplansettlement-query-layout.xml',
    initComponent: function () {
        var me = this;
        me.callParent(arguments);
    },
    afterInit: function () {
        var me = this;

        var orgCode = Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE);
        var orgName = Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_NAME);
        var postData = {orgCode: orgCode};
        Scdp.doAction("manager_dep-get", postData, function (result) {
            if (Erp.Util.isNullReturnEmpty(result.isManagerDep) == "0") {
                me.getCmp("officeId").sotValue(orgCode);
                me.getCmp("officeId").resetOriginalValue();

                me.getCmp("officeIdDesc").sotValue(orgName);
                me.getCmp("officeIdDesc").resetOriginalValue();
                me.getCmp("officeIdDesc").setReadOnly(true);
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