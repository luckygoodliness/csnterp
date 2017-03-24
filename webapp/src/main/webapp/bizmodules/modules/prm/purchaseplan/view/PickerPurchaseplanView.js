Ext.define('Purchaseplan.view.PickerPurchaseplanView', {
    extend: 'Scdp.mvc.AbstractView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/prm/purchaseplan',
    aHeight: 500,  //指定内容面板高度
    aWidth: 1100,  //指定内容面板宽度
    cpHeight: 60,
    layoutFile: "pickerpurchaseplan-edit-layout.xml",
    bindings: ['prmPurchasePlanDto', 'prmPurchasePlanDto.prmPurchasePlanDetailDto', 'prmPurchasePlanDto.prmPurchasePackageDto', 'prmPurchasePlanDto.cdmFileRelationDto'],
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        me.initPackageGrid();
    },
    initPackageGrid: function () {
        var me = this;
        var packageGrid = me.getCmp("prmPurchasePackageDto");
        packageGrid.getCmp("toolbar").hide();
    }
});