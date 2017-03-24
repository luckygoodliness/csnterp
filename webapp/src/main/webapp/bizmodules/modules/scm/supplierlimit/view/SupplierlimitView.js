Ext.define('Supplierlimit.view.SupplierlimitView', {
    extend: 'Scdp.mvc.AbstractCrudView_2',
    modulePath: 'com/csnt/scdp/bizmodules/modules/scm/supplierlimit',
    //aHeight: 1500,  //指定内容面板高度
    //aWidth: 2000,  //指定内容面板宽度
    cpHeight: 50,
    epHeight: 200,
    //epiHeight: 1500,
    //xpHeight: 100,
    allowNullConditions: true,
    queryLayoutFile: 'supplierlimit-query-layout.xml',
    editLayoutFile: 'supplierlimit-edit-layout.xml',
    //extraLayoutFile: 'supplierlimit-extra-layout.xml',
    bindings: ['scmSupplierLimitDto', 'scmSupplierLimitDto.scmSupplierLimitDetailDto'],
    canEdit: true,
    enableColumnMove: true,
    showHeaderCheckbox: true,
    needSplitPage: true,
    initComponent: function () {
        var me = this;
        this.callParent(arguments);
    },
    afterInit: function () {
        var me = this;
        me.resetDetailToolbar();
        var editToolbar = me.getEditToolbar();
        editToolbar.add({
            text: '批量设置',
            cid: 'batchSetting',
            iconCls: 'temp_icon_16'
        });
        me.getCmp('editToolbar->batchSetting').setDisabled(true);
    },


    resetDetailToolbar: function () {
        var me = this;
        var supplierLimitDetailGrid = me.getCmp("scmSupplierLimitDetailDto");
        supplierLimitDetailGrid.doAddRow = function (model) {
            var supplierLimitDetailController = Scdp.getActiveModule().controller;
            supplierLimitDetailController.pickContract();
        };
    //    contractInvoiceGrid.afterDeleteRow = function (model) {
    //        var contractInvoiceController = Scdp.getActiveModule().controller;
    //        contractInvoiceController.afterDeleteRow();
    //};
        supplierLimitDetailGrid.getCmp("toolbar->rowMoveTopBtn").hide();
        supplierLimitDetailGrid.getCmp("toolbar->rowMoveUpBtn").hide();
        supplierLimitDetailGrid.getCmp("toolbar->rowMoveDownBtn").hide();
        supplierLimitDetailGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
        supplierLimitDetailGrid.getCmp("toolbar->copyAddRowBtn").hide();


    }
});