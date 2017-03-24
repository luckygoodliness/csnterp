Ext.define("Cashclearance.store.FadCashClearanceStore", {
	extend: 'Ext.data.Store',
model: 'Cashclearance.model.FadCashClearanceModel',
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