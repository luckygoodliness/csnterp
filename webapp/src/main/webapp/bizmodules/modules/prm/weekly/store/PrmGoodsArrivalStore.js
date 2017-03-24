Ext.define("Weekly.store.PrmGoodsArrivalStore", {
	extend: 'Ext.data.Store',
model: 'Weekly.model.PrmGoodsArrivalModel',
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