Ext.define("Balanceofcontract.store.ScmContractInvoiceStore", {
	extend: 'Ext.data.Store',
model: 'Balanceofcontract.model.ScmContractInvoiceModel',
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