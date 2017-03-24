Ext.define("Invoice.store.FadCashReqInvoiceStore", {
	extend: 'Ext.data.Store',
model: 'Invoice.model.FadCashReqInvoiceModel',
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