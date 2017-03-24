Ext.define('Invoice.view.FadFailyClaimsView', {
	extend: 'Scdp.mvc.AbstractCrudView_2',
	modulePath: 'com/csnt/scdp/bizmodules/modules/fad/invoice',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 100,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'fadFailyClaims-query-layout.xml',
	editLayoutFile: 'fadFailyClaims-edit-layout.xml',
	//extraLayoutFile: 'invoice-extra-layout.xml',
	bindings: ['fadInvoiceDto','fadInvoiceDto.fadCashReqInvoiceDto'],
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
        var fadCashReqInvoiceGrid = me.getCmp("fadCashReqInvoiceDto");
        fadCashReqInvoiceGrid.beforeEditGrid = function (eventObj) {
            return false;
        }
	},
    resetDetailToolbar: function () {
        var me = this;
        var fadCashReqInvoiceGrid = me.getCmp("fadCashReqInvoiceDto");
//        var invoiceMaterialGrid = me.getCmp("scmInvoiceMaterialDto");
        fadCashReqInvoiceGrid.doAddRow = function (model) {
            var  fadCashReqInvoiceController = Scdp.getActiveModule().controller;
            fadCashReqInvoiceController.pickCashReq();
        };
        fadCashReqInvoiceGrid.getCmp("toolbar->rowMoveTopBtn").hide();
        fadCashReqInvoiceGrid.getCmp("toolbar->rowMoveUpBtn").hide();
        fadCashReqInvoiceGrid.getCmp("toolbar->rowMoveDownBtn").hide();
        fadCashReqInvoiceGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
        fadCashReqInvoiceGrid.getCmp("toolbar->copyAddRowBtn").hide();
    }
});