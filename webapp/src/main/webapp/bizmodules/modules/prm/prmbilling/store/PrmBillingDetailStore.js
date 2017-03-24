Ext.define("Prmbilling.store.PrmBillingDetailStore", {
	extend: 'Ext.data.Store',
model: 'Prmbilling.model.PrmBillingDetailModel',
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