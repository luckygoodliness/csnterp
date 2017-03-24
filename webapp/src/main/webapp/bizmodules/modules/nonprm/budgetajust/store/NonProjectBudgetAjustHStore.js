Ext.define("Budgetajust.store.NonProjectBudgetAjustHStore", {
	extend: 'Ext.data.Store',
model: 'Budgetajust.model.NonProjectBudgetAjustHModel',
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