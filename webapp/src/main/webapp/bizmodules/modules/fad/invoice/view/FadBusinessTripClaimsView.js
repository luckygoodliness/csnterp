Ext.define('Invoice.view.FadBusinessTripClaimsView', {
	extend: 'Scdp.mvc.AbstractCrudView_2',
	modulePath: 'com/csnt/scdp/bizmodules/modules/fad/invoice',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 100,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'fadBusinessTripClaims-query-layout.xml',
	editLayoutFile: 'fadBusinessTripClaims-edit-layout.xml',
	//extraLayoutFile: 'invoice-extra-layout.xml',
	bindings: ['fadInvoiceDto','fadInvoiceDto.fadCashReqInvoiceDto','fadInvoiceDto.fadInvoiceSubsidyDto','fadInvoiceDto.fadInvoiceTravelDto'],
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
        me.resetToolbar();
        me.resetDetailToolbar();
        var fadCashReqInvoiceGrid = me.getCmp("fadCashReqInvoiceDto");
        fadCashReqInvoiceGrid.beforeEditGrid = function (eventObj) {
            return false;
        }
        var fadInvoiceSubsidyGrid = me.getCmp("fadInvoiceSubsidyDto");
        fadInvoiceSubsidyGrid.beforeEditGrid = function (eventObj) {
            return false;
        }
        var fadInvoiceTravelGrid = me.getCmp("fadInvoiceTravelDto");
        fadInvoiceTravelGrid.beforeEditGrid = function (eventObj) {
            return false;
        }
    },

    resetToolbar: function () {
        var me = this;
        me.getCmp('editToolbar->copyAddBtn').setVisible(false);
    },
    resetDetailToolbar: function () {
        var me = this;
        var fadCashReqInvoiceGrid = me.getCmp("fadCashReqInvoiceDto");
        var fadInvoiceSubsidyGrid = me.getCmp("fadInvoiceSubsidyDto");
        var fadInvoiceTravelGrid = me.getCmp("fadInvoiceTravelDto");
//        var invoiceMaterialGrid = me.getCmp("scmInvoiceMaterialDto");
        fadCashReqInvoiceGrid.doAddRow = function (model) {
            var  fadCashReqInvoiceController = Scdp.getActiveModule().controller;
            fadCashReqInvoiceController.pickCashReq();
        };
//        fadCashReqInvoiceGrid.afterDeleteRow = function (model) {
//            var  contractInvoiceController = Scdp.getActiveModule().controller;
//            contractInvoiceController.afterDeleteRow();
//        };
        fadCashReqInvoiceGrid.getCmp("toolbar->rowMoveTopBtn").hide();
        fadCashReqInvoiceGrid.getCmp("toolbar->rowMoveUpBtn").hide();
        fadCashReqInvoiceGrid.getCmp("toolbar->rowMoveDownBtn").hide();
        fadCashReqInvoiceGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
        fadCashReqInvoiceGrid.getCmp("toolbar->copyAddRowBtn").hide();
        fadInvoiceSubsidyGrid.getCmp("toolbar->rowMoveTopBtn").hide();
        fadInvoiceSubsidyGrid.getCmp("toolbar->rowMoveUpBtn").hide();
        fadInvoiceSubsidyGrid.getCmp("toolbar->rowMoveDownBtn").hide();
        fadInvoiceSubsidyGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
//        fadInvoiceSubsidyGrid.getCmp("toolbar->copyAddRowBtn").hide();
        fadInvoiceTravelGrid.getCmp("toolbar->rowMoveTopBtn").hide();
        fadInvoiceTravelGrid.getCmp("toolbar->rowMoveUpBtn").hide();
        fadInvoiceTravelGrid.getCmp("toolbar->rowMoveDownBtn").hide();
        fadInvoiceTravelGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
//        fadInvoiceTravelGrid.getCmp("toolbar->copyAddRowBtn").hide();

    }
});