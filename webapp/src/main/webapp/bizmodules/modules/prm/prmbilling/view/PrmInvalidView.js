/**
 * Created by lijx on 2016/8/18.
 */
Ext.define('Prmbilling.view.PrmInvalidView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/prm/prmbilling',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 80,
    epHeight: 270,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'prminvalid-query-layout.xml',
    editLayoutFile: 'prminvalid-edit-layout.xml',
    bindings: ['prmBillingDto', 'prmBillingDto.prmBillingDetailDto','prmBillingDto.cdmFileRelationDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: false,
    needSplitPage: true,
    workFlowDefinitionKey: 'Prm_Invalid',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        me.getCmp("prmBillingDto->customerInvoiceName").sotCodeAndDesc = me.sotCodeAndDescInvoiceName;
        me.getCmp("editPanel-\x3ecopyAddBtn").setVisible(false);
        me.getCmp("editPanel-\x3emodifyBtn").setVisible(false);
        me.getCmp("fileDelete").setVisible(false);
    }

});