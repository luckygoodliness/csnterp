Ext.define('Financialsubject.view.FinancialsubjectView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/nonprm/financialsubject',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 60,
    epHeight: 160,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'financialsubject-query-layout.xml',
    editLayoutFile: 'financialsubject-edit-layout.xml',
    //extraLayoutFile: 'financialsubject-extra-layout.xml',
    bindings: ['financialSubjectDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var view = this;
        var editToolbar = view.getEditToolbar();
        editToolbar.add({
            text: '分配',
            cid: 'extBenFill',
            iconCls: 'temp_icon_16'
        });
        view.initConditionPanel();
    },
    initConditionPanel: function () {
        var view = this;
        var conditionPanel = view.getConditionPanel();
        if (view.controller.actionParams.type == "prm") {
            conditionPanel.queryExtraParams = {selfconditions: "bs.subject_type in ('1','2')"};
        }
        else if (view.controller.actionParams.type == "nonprm") {
            conditionPanel.queryExtraParams = {selfconditions: "bs.subject_type ='0'"};
            conditionPanel.getCmp("subjectTypeQ").hide();
            view.getCmp("rate").hide();
        }
    }
});