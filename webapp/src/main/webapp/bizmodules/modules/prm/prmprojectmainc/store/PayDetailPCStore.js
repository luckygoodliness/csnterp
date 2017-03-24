Ext.define("Prmprojectmainc.store.PayDetailPCStore", {
	extend: 'Ext.data.Store',
	model: 'Prmprojectmainc.model.PayDetailPCModel',
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