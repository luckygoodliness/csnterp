Ext.define("Supplierinfor.store.ScmSupplierBankStore", {
	extend: 'Ext.data.Store',
model: 'Supplierinfor.model.ScmSupplierBankModel',
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