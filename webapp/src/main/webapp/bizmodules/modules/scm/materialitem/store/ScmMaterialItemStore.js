Ext.define("Materialitem.store.ScmMaterialItemStore", {
	extend: 'Ext.data.Store',
model: 'Materialitem.model.ScmMaterialItemModel',
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