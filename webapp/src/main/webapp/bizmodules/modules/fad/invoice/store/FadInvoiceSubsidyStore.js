Ext.define("Invoice.store.FadInvoiceSubsidyStore", {
	extend: 'Ext.data.Store',
model: 'Invoice.model.FadInvoiceSubsidyModel',
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