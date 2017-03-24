Ext.define("Budgetajust.store.NonProjectBudgetAjustCStore", {
	extend: 'Ext.data.Store',
model: 'Budgetajust.model.NonProjectBudgetAjustCModel',
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