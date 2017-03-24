Ext.define("Goodsarrival.store.PrmGoodsArrivalStore", {
	extend: 'Ext.data.Store',
model: 'Goodsarrival.model.PrmGoodsArrivalModel',
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