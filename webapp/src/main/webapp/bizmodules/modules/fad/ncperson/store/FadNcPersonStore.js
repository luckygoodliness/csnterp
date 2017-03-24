Ext.define("Ncperson.store.FadNcPersonStore", {
	extend: 'Ext.data.Store',
model: 'Ncperson.model.FadNcPersonModel',
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