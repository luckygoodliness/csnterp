Ext.define("Supplierlimit.store.ScmSupplierLimitDetailStore", {
	extend: 'Ext.data.Store',
model: 'Supplierlimit.model.ScmSupplierLimitDetailModel',
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