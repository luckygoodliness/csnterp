Ext.define("Supplierinfochange.store.ScmSukpplierKeyCStore", {
	extend: 'Ext.data.Store',
model: 'Supplierinfochange.model.ScmSukpplierKeyCModel',
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