Ext.define("Supplierinfor.store.ScmSukpplierKeyStore", {
	extend: 'Ext.data.Store',
model: 'Supplierinfor.model.ScmSukpplierKeyModel',
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