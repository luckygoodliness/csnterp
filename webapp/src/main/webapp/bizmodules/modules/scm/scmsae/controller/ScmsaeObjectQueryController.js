Ext.define("Scmsae.controller.ScmsaeObjectQueryController", {
	extend: 'Scdp.mvc.AbstractCrudController',
	viewClass: 'Scmsae.view.ScmsaeObjectQueryView',
	uniqueValidateFields: [],
	extraEvents: [],
	queryAction: 'pick-scmsaeobject-query',
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