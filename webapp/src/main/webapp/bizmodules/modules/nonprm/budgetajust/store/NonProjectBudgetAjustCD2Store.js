Ext.define("Budgetajust.store.NonProjectBudgetAjustCD2Store", {
	extend: 'Ext.data.Store',
model: 'Budgetajust.model.NonProjectBudgetAjustCD2Model',
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