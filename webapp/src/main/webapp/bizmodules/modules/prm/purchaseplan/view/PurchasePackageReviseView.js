Ext.define('Purchaseplan.view.PurchasePackageReviseView', {
    extend: 'Scdp.mvc.AbstractView',
    modulePath: 'com/csnt/scdp/bizmodules/modules/prm/purchaseplan',
    layoutFile: 'purchase-package-revise-layout.xml',
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        me.initPackageGrid();
        me.showReviseRecord();
    },
    initPackageGrid: function () {
        var me = this;
        var packageGrid = me.getCmp("prmPurchasePackageRecordDto");
        packageGrid.editable=false;
        packageGrid.getCmp("toolbar->rowMoveTopBtn").hide();
        packageGrid.getCmp("toolbar->rowMoveUpBtn").hide();
        packageGrid.getCmp("toolbar->rowMoveDownBtn").hide();
        packageGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
        packageGrid.getCmp("toolbar->copyAddRowBtn").hide();
        packageGrid.getCmp("toolbar->delRowBtn").hide();
        packageGrid.getCmp("toolbar->addRowBtn").hide();
    },
    showReviseRecord: function () {
        var me = this;
        var uuid = me.uuid;
        var postData = {uuid: uuid};
        Scdp.doAction('show-revise-record', postData, function (retData) {
            if (retData.success == true) {
                var newRecord = retData.prmPurchasePackageRecordDtos;
                var purchasePackageRecordCardGrid = me.getCmp('prmPurchasePackageRecordDto');
                purchasePackageRecordCardGrid.sotValue(newRecord);
            }
        });
    }
});