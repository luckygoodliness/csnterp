Ext.define('PrmExamPeriod.view.PrmExamPeriodView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/prm/prmexamperiod',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 75,
    epHeight: 150,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'prmexamperiod-query-layout.xml',
    editLayoutFile: 'prmexamperiod-edit-layout.xml',
    //extraLayoutFile: 'finalestimate-extra-layout.xml',
    bindings: ['prmExamPeriodDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: '',
    initComponent: function () {
        var me = this;
        debugger
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        me.initToolbar();
    },
    initToolbar: function () {
        var me = this;
    },
    UIStatusChanged: function (view, uistatus) {
        var view = this;
        view.afterChangeUIStatus();
    },
    afterChangeUIStatus: function () {
        var view = this;
    }
});