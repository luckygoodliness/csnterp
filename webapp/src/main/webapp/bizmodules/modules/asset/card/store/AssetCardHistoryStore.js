Ext.define("Card.store.AssetCardHistoryStore", {
	extend: 'Ext.data.Store',
model: 'Card.model.AssetCardHistoryModel',
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