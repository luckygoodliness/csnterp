Ext.define("Prmprojectmainc.store.ProgressDetailPCStore", {
	extend: 'Ext.data.Store',
	model: 'Prmprojectmainc.model.ProgressDetailPCModel',
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