Ext.define("Supplierinfochange.store.ScmSupplierContactsCStore", {
	extend: 'Ext.data.Store',
model: 'Supplierinfochange.model.ScmSupplierContactsCModel',
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