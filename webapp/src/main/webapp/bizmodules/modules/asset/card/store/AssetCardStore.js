Ext.define("Card.store.AssetCardStore", {
	extend: 'Ext.data.Store',
model: 'Card.model.AssetCardModel',
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