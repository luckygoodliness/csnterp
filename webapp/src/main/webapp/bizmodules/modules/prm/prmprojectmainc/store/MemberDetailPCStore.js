Ext.define("Prmprojectmainc.store.MemberDetailPCStore", {
	extend: 'Ext.data.Store',
	model: 'Prmprojectmainc.model.MemberDetailPCModel',
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