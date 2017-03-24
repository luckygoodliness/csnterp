Ext.define("Receipts.store.ReceiptsStore", {
	extend: 'Ext.data.Store',
model: 'Receipts.model.ReceiptsModel',
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