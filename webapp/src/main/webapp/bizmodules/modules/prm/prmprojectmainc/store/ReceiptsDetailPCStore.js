Ext.define("Prmprojectmainc.store.ReceiptsDetailPCStore", {
	extend: 'Ext.data.Store',
	model: 'Prmprojectmainc.model.ReceiptsDetailPCModel',
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