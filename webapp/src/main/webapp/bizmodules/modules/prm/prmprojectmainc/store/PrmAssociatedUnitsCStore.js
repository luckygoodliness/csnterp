Ext.define("Prmprojectmainc.store.PrmAssociatedUnitsCStore", {
	extend: 'Ext.data.Store',
	model: 'Prmprojectmainc.model.PrmAssociatedUnitsCModel',
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