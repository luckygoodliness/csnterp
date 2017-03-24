Ext.define("Cashclearance.store.FadCashReqInvoiceStore", {
	extend: 'Ext.data.Store',
model: 'Cashclearance.model.FadCashReqInvoiceModel',
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