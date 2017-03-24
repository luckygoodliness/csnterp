Ext.define("Card.store.AssetCardTransferStore", {
	extend: 'Ext.data.Store',
model: 'Card.model.AssetCardTransferModel',
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