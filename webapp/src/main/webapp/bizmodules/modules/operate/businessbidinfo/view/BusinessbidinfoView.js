Ext.define('Businessbidinfo.view.BusinessbidinfoView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/operate/businessbidinfo',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 80,
    epHeight: 220,
//    epiHeight: 1500,
//    xpHeight: 180,
    allowNullConditions: true,
    queryLayoutFile: 'businessbidinfo-query-layout.xml',
    editLayoutFile: 'businessbidinfo-edit-layout.xml',
    //extraLayoutFile: 'businessbidinfo-extra-layout.xml',
    bindings: ['operateBusinessBidInfoDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Operate_Businessbidinfo',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        me.getCmp("editPanel->copyAddBtn").setVisible(false);
        me.getCmp("queryPanel->projectType").sotValue("1");
        me.getCmp("queryPanel->projectType").originalValue = me.getCmp("queryPanel->projectType").gotValue();
    }
});