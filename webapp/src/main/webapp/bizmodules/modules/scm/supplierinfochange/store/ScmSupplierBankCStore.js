Ext.define("Supplierinfochange.store.ScmSupplierBankCStore", {
	extend: 'Ext.data.Store',
model: 'Supplierinfochange.model.ScmSupplierBankCModel',
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