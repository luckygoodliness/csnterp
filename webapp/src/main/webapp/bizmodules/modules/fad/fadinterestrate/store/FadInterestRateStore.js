Ext.define("Fadinterestrate.store.FadInterestRateStore", {
	extend: 'Ext.data.Store',
model: 'Fadinterestrate.model.FadInterestRateModel',
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