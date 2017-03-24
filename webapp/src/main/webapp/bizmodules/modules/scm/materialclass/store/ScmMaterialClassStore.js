Ext.define("Materialclass.store.ScmMaterialClassStore", {
	extend: 'Ext.data.Store',
model: 'Materialclass.model.ScmMaterialClassModel',
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