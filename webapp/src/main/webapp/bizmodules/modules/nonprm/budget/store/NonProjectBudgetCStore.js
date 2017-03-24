Ext.define("Budget.store.NonProjectBudgetCStore", {
	extend: 'Ext.data.Store',
model: 'Budget.model.NonProjectBudgetCModel',
	autoLoad: false,
	sorters: [
		{
			property: 'seqNo',
			direction: 'ASC'
		}
	],
	proxy: {
		type: 'memory',
		reader: {
			type: 'json'
		}
	}
});