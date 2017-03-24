Ext.define("Supplierinfor.store.ScmSupplierStore", {
	extend: 'Ext.data.Store',
model: 'Supplierinfor.model.ScmSupplierModel',
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