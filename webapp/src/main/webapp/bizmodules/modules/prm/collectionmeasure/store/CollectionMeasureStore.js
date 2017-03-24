Ext.define("Collectionmeasure.store.CollectionMeasureStore", {
	extend: 'Ext.data.Store',
model: 'Collectionmeasure.model.PrmCollectionMeasureModel',
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