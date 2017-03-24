Ext.define("Nonprmpurchasereq.store.QueryBudgetStore", {
	extend: 'Ext.data.Store',
model: 'Nonprmpurchasereq.model.QueryBudgetModel',
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