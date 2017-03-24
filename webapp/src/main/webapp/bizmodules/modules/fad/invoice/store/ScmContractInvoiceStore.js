Ext.define("Invoice.store.ScmContractInvoiceStore", {
	extend: 'Ext.data.Store',
model: 'Invoice.model.ScmContractInvoiceModel',
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