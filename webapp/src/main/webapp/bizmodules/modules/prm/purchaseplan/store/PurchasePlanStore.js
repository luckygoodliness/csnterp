Ext.define("Purchaseplan.store.PurchasePlanStore", {
	extend: 'Ext.data.Store',
model: 'Purchaseplan.model.PurchasePlanModel',
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