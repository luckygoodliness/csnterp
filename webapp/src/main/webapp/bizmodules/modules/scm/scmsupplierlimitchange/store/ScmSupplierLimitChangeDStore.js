Ext.define("Scmsupplierlimitchange.store.ScmSupplierLimitChangeDStore", {
	extend: 'Ext.data.Store',
model: 'Scmsupplierlimitchange.model.ScmSupplierLimitChangeDModel',
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