Ext.define("Budgeth.store.NonProjectBudgetApproStore", {
	extend: 'Ext.data.Store',
model: 'Budgeth.model.NonProjectBudgetApproModel',
	autoLoad: false,
	sorters: [
		{
			property: 'createTime',
			direction: 'DESC'
		}
	],
	proxy: {
		type: 'memory',
		reader: {
			type: 'json'
		}
	}
});