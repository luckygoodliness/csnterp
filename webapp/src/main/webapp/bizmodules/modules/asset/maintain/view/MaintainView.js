Ext.define('Maintain.view.MaintainView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/asset/maintain',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 70,
    epHeight: 300,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'maintain-query-layout.xml',
    editLayoutFile: 'maintain-edit-layout.xml',
    //extraLayoutFile: 'maintain-extra-layout.xml',
    bindings: ['assetMaintainDto', 'assetMaintainDto.cdmFileRelationDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    workFlowDefinitionKey: 'Asset_Maintain',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        var editToolbar = me.getEditToolbar();
        editToolbar.add(
            {
                text: '打印申请单',
                cid: 'formPrint',
                iconCls: 'printer_icon_16'
            });
    },
    setUIStatus: function () {
        var me = this;
        me.callParent(arguments);
        if (me.uistatus == Scdp.Const.UI_INFO_STATUS_VIEW) {
            me.getCmp("editPanel->editToolbar->formPrint").setDisabled(false);
        } else if (me.uistatus == Scdp.Const.UI_INFO_STATUS_MODIFY
            || me.uistatus == Scdp.Const.UI_INFO_STATUS_NEW) {
            me.getCmp("editPanel->editToolbar->formPrint").setDisabled(true);
        }
    }
});