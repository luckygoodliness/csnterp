Ext.define("Budgeth.controller.BudgetapproController", {
	extend: 'Scdp.mvc.AbstractController',
	viewClass: 'Budgeth.view.BudgetapproView',
	loadAction: 'budgeth-approdetail',
	extraEvents: [],
	dtoClass: 'com.csnt.scdp.bizmodules.modules.nonprm.budgeth.dto.NonProjectBudgetApproDto',
	afterInit: function () {
		var me = this;
		var view = me.view;
		var nonBudgetAppropriationGrid = view.getCmp("nonProjectBudgetApproDto");
		nonBudgetAppropriationGrid.store.on('update', function (store, record, operation, mdColumns) {
		});
	}
});