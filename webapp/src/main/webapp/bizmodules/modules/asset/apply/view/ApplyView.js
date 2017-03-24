Ext.define('Apply.view.ApplyView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/asset/apply',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 100,
    epHeight: 254,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'apply-query-layout.xml',
    editLayoutFile: 'apply-edit-layout.xml',
    //extraLayoutFile: 'apply-extra-layout.xml',
    bindings: ['assetDiscardApplyDto', 'assetDiscardApplyDto.cdmFileRelationDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Asset_Retirement',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        me.getCmp("editPanel->copyAddBtn").hide();
        me.getEditToolbar().add(
            {
                text: '打印',
                cid: 'singlePrint',
                iconCls: 'printer_icon_16'
            }
        );
    }
});