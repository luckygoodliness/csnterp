Ext.define('Companyplan.view.CompanyplanView', {
	extend: 'Scdp.mvc.AbstractCrudView_2',
	modulePath: 'com/csnt/scdp/bizmodules/modules/operate/companyplan',
	//aHeight: 1500,  //指定内容面板高度
	//aWidth: 2000,  //指定内容面板宽度
	cpHeight: 100,
	epHeight: 200,
	//epiHeight: 1500,
    //xpHeight: 100,
	allowNullConditions: true,
	queryLayoutFile: 'companyplan-query-layout.xml',
	editLayoutFile: 'companyplan-edit-layout.xml',
	//extraLayoutFile: 'companyplan-extra-layout.xml',
	bindings: ['operateCompanyPlanHDto','operateCompanyPlanHDto.operateCompanyPlanCDto'],
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
	},
    resetDetailToolbar: function () {
        var me = this;
        var operateCompanyPlanCGrid = me.getCmp("operateCompanyPlanCDto");
        operateCompanyPlanCGrid.doAddRow = function (model) {
            var  operateCompanyPlanCController = Scdp.getActiveModule().controller;
            operateCompanyPlanCController.pickOffice();
        };
        operateCompanyPlanCGrid.getCmp("toolbar->rowMoveTopBtn").hide();
        operateCompanyPlanCGrid.getCmp("toolbar->rowMoveUpBtn").hide();
        operateCompanyPlanCGrid.getCmp("toolbar->rowMoveDownBtn").hide();
        operateCompanyPlanCGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
        operateCompanyPlanCGrid.getCmp("toolbar->copyAddRowBtn").hide();
		var operateCompanyPlanDGrid = me.getCmp("operateCompanyPlanDDto");
		operateCompanyPlanDGrid.getCmp("toolbar->rowMoveTopBtn").hide();
		operateCompanyPlanDGrid.getCmp("toolbar->rowMoveUpBtn").hide();
		operateCompanyPlanDGrid.getCmp("toolbar->rowMoveDownBtn").hide();
		operateCompanyPlanDGrid.getCmp("toolbar->rowMoveBottomBtn").hide();
		operateCompanyPlanDGrid.getCmp("toolbar->copyAddRowBtn").hide();
    }
});