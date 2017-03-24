Ext.define("Invoice.store.FadInvoiceTravelStore", {
	extend: 'Ext.data.Store',
model: 'Invoice.model.FadInvoiceTravelModel',
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