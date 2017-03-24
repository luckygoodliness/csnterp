Ext.define("Budgetajust.store.NonProjectBudgetAjustCDStore", {
	extend: 'Ext.data.Store',
model: 'Budgetajust.model.NonProjectBudgetAjustCDModel',
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