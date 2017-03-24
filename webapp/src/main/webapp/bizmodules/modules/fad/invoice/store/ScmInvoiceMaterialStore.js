Ext.define("Invoice.store.ScmInvoiceMaterialStore", {
	extend: 'Ext.data.Store',
model: 'Invoice.model.ScmInvoiceMaterialModel',
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