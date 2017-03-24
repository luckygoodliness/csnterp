Ext.define('Card.view.AssetTransferCardView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/asset/card',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 80,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'cardtransfer-query-layout.xml',
    editLayoutFile: 'cardtransfer-edit-layout.xml',
    bindings: ['assetCardTransferDto',
        'assetCardTransferDto.cdmFileRelationDto'
    ],

    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    scroll: true,
    workFlowDefinitionKey: 'Asset_Transfer_Card',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
    }
});