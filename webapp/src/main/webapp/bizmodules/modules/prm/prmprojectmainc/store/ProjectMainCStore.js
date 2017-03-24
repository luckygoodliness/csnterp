Ext.define("Prmprojectmainc.store.ProjectMainCStore", {
	extend: 'Ext.data.Store',
model: 'Prmprojectmainc.model.ProjectMainCModel',
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