Ext.define("Fadsupplieradjust.store.FadSupplierAdjustStore", {
	extend: 'Ext.data.Store',
model: 'Fadsupplieradjust.model.FadSupplierAdjustModel',
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