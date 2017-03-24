Ext.define("Weekly.store.PrmCollectionMeasureStore", {
	extend: 'Ext.data.Store',
model: 'Weekly.model.PrmCollectionMeasureModel',
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