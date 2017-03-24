Ext.define("Transfer.store.AssetTransferStore", {
	extend: 'Ext.data.Store',
model: 'Transfer.model.AssetTransferModel',
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