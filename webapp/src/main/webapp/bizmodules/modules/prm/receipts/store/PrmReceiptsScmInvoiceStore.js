Ext.define("Receipts.store.PrmReceiptsScmInvoiceStore", {
	extend: 'Ext.data.Store',
model: 'Receipts.model.PrmReceiptsScmInvoiceModel',
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