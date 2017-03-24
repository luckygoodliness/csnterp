Ext.define("Materialclass.store.ScmMaterialKeyStore", {
	extend: 'Ext.data.Store',
model: 'Materialclass.model.ScmMaterialKeyModel',
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