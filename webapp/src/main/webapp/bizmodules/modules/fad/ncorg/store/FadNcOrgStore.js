Ext.define("Ncorg.store.FadNcOrgStore", {
	extend: 'Ext.data.Store',
model: 'Ncorg.model.FadNcOrgModel',
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