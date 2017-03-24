Ext.define("Card.store.AssetHandoverStore", {
	extend: 'Ext.data.Store',
model: 'Card.model.AssetHandoverModel',
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