Ext.define("Prmprojectmainc.store.PrmQsPCStore", {
	extend: 'Ext.data.Store',
model: 'Prmprojectmainc.model.PrmQsPCModel',
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