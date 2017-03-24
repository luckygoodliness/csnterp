Ext.define("Income.store.NonProjectIncomeStore", {
	extend: 'Ext.data.Store',
model: 'Income.model.NonProjectIncomeModel',
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