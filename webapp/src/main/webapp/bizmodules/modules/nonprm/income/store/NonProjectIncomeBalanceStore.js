Ext.define("Income.store.NonProjectIncomeBalanceStore", {
	extend: 'Ext.data.Store',
model: 'Income.model.NonProjectIncomeBalanceModel',
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