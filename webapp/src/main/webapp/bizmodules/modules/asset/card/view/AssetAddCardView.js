Ext.define('Card.view.AssetAddCardView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/asset/card',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 100,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'cardadd-query-layout.xml',
    editLayoutFile: 'cardadd-edit-layout.xml',
    bindings: ['assetHandoverDto'],

    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    scroll: true,
    workFlowDefinitionKey: 'Asset_Register',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        var editToolbar = me.getEditToolbar();
        editToolbar.add(
            {
                text: '打印交接单',
                cid: 'formPrint',
                iconCls: 'printer_icon_16'
            });
    }
});