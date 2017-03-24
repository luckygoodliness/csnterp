Ext.define("Supplierinfochange.store.ScmSupplierCStore", {
	extend: 'Ext.data.Store',
model: 'Supplierinfochange.model.ScmSupplierCModel',
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