Ext.define("Projectmain.store.ProjectMainStore", {
	extend: 'Ext.data.Store',
model: 'Projectmain.model.ProjectMainModel',
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