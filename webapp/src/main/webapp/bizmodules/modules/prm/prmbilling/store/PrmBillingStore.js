Ext.define("Prmbilling.store.PrmBillingStore", {
	extend: 'Ext.data.Store',
model: 'Prmbilling.model.PrmBillingModel',
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