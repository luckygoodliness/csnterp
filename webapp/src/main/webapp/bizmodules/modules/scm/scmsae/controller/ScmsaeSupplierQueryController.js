Ext.define("Scmsae.controller.ScmsaeSupplierQueryController", {
	extend: 'Scdp.mvc.AbstractCrudController',
	viewClass: 'Scmsae.view.ScmsaeSupplierQueryView',
	uniqueValidateFields: [],
	extraEvents: [],
	queryAction: 'pick-scmsaesupplier-query',
	afterInit: function () {
		var me = this;
		var view = me.view;
		if (me.actionParams) {
			var queryPanelCmp = view.getQueryPanel();
			var connStrCmp = queryPanelCmp.getCmp("connStr");
			var connStr = me.actionParams.connStr;
			connStrCmp.sotValue(connStr);
		}
	}

});