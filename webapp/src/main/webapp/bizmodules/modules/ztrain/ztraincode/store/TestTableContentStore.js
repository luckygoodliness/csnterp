Ext.define("Ztraincode.store.TestTableContentStore", {
	extend: 'Ext.data.Store',
model: 'Ztraincode.model.TestTableContentModel',
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