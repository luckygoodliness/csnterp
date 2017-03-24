Ext.define('PrmReport.view.PrmProjectLowMarginTotalReportView', {
    extend: 'Scdp.mvc.AbstractReportView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/prm/report',
    cpHeight: 60,
    epHeight: 200,
    allowNullConditions: true,
    queryLayoutFile: 'prmProjectLowMarginTotalReport-query-layout.xml',
    initComponent: function () {
        var me = this;
        me.callParent(arguments);
    },
    afterInit: function () {
        var me = this;

        me.getCmp("queryPanel->countryCode").sotValue("CN");
        me.getCmp("queryPanel->countryCode").originalValue = me.getCmp("queryPanel->countryCode").gotValue();

        var orgCode = Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_CODE);
        var orgName = Scdp.CacheUtil.get(Scdp.Const.USER_DEPARTMENT_NAME);
        var postData = {orgCode: orgCode};
        Scdp.doAction("manager_dep-get", postData, function (result) {
            if (Erp.Util.isNullReturnEmpty(result.isManagerDep) == "0") {
                me.getCmp("contractorOffice").putValue(orgCode);
                me.getCmp("contractorOffice").resetOriginalValue();
                me.getCmp("contractorOffice").setReadOnly(true);

                me.getCmp("contractorOfficeDesc").sotValue(orgName);
                me.getCmp("contractorOfficeDesc").resetOriginalValue();
            }
        });

        /*var queryToolbar = me.getCmp('conditionToolbar');
         queryToolbar.insert(2, {
         text: '返回',
         cid: 'btnRtn',
         iconCls: 'erp-return-back'
         });
         var actionParams = me.controller.actionParams;
         if (Scdp.ObjUtil.isEmpty(actionParams)) {
         me.getCmp("conditionToolbar->btnRtn").setVisible(false);
         }*/
    }
});