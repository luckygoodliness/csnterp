Ext.define("Income.store.NonProjectIncome2Store", {
	extend: 'Ext.data.Store',
model: 'Income.model.NonProjectIncome2Model',
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