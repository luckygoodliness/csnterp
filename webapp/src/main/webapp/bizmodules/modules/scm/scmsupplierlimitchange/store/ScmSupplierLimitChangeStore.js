Ext.define("Scmsupplierlimitchange.store.ScmSupplierLimitChangeStore", {
	extend: 'Ext.data.Store',
model: 'Scmsupplierlimitchange.model.ScmSupplierLimitChangeModel',
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