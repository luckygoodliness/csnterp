Ext.define("Check.store.AssetAnnualCheckStore", {
	extend: 'Ext.data.Store',
model: 'Check.model.AssetAnnualCheckModel',
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