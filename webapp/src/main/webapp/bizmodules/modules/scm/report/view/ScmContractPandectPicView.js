Ext.define('ScmReport.view.ScmContractPandectPicView', {
    extend: 'Scdp.mvc.AbstractReportView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/scm/report',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 80,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'scmContractPandectPic-query-layout.xml',
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
        /*me.getCmp("dateStart").sotValue(Erp.Util.getCurrentYear() + '-01-01');
         me.getCmp("dateOver").sotValue(Erp.Util.getCurrentYear() + '-' + Erp.Util.getCurrentMonth() + '-' + (new Date(Erp.Util.getCurrentYear(), Erp.Util.getCurrentMonth(), 0).getDate()).toString());*/

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