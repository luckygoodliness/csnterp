Ext.define("Prmprojectmainc.store.SquareDetailPCStore", {
	extend: 'Ext.data.Store',
	model: 'Prmprojectmainc.model.SquareDetailPCModel',
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