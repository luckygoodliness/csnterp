Ext.define("Purchaseplan.store.PrmPurchasePackageStore", {
	extend: 'Ext.data.Store',
model: 'Purchaseplan.model.PrmPurchasePackageModel',
	autoLoad: false,
	sorters: [
		{
			property: 'packageNo',
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