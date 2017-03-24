Ext.define("Ztraincode.store.TestTableStore", {
	extend: 'Ext.data.Store',
model: 'Ztraincode.model.TestTableModel',
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