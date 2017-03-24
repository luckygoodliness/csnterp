Ext.define("Invoice.store.FadInvoiceStore", {
	extend: 'Ext.data.Store',
model: 'Invoice.model.FadInvoiceModel',
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