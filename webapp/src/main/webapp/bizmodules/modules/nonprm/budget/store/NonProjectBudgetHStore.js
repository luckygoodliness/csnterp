Ext.define("Budget.store.NonProjectBudgetHStore", {
	extend: 'Ext.data.Store',
model: 'Budget.model.NonProjectBudgetHModel',
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