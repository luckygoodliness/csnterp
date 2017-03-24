Ext.define("Income.store.NonProjectIncomeInStore", {
	extend: 'Ext.data.Store',
	model: 'Income.model.NonProjectIncomeInModel',
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