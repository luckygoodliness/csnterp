Ext.define("Budget.store.NonProjectBudgetCDStore", {
	extend: 'Ext.data.Store',
model: 'Budget.model.NonProjectBudgetCDModel',
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