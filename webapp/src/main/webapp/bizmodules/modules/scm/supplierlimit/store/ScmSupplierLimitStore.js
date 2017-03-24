Ext.define("Supplierlimit.store.ScmSupplierLimitStore", {
	extend: 'Ext.data.Store',
model: 'Supplierlimit.model.ScmSupplierLimitModel',
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