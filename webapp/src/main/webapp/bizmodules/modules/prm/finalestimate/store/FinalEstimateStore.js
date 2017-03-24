Ext.define("Finalestimate.store.FinalEstimateStore", {
	extend: 'Ext.data.Store',
model: 'Finalestimate.model.PrmFinalEstimateModel',
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