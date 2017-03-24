Ext.define("Purchaseplan.store.PurchasePlanDetailStore", {
	extend: 'Ext.data.Store',
model: 'Purchaseplan.model.PurchasePlanDetailModel',
	autoLoad: false,
	sorters: [
		{
			property: 'prmBudgetType',
			direction: 'ASC'
		},
		{
			property: 'serialNumber',
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