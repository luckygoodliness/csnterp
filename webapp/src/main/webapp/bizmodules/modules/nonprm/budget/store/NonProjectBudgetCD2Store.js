Ext.define("Budget.store.NonProjectBudgetCD2Store", {
	extend: 'Ext.data.Store',
model: 'Budget.model.NonProjectBudgetCD2Model',
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