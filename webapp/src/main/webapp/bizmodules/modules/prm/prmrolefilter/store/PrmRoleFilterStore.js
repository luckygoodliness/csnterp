Ext.define("Prmrolefilter.store.PrmRoleFilterStore", {
	extend: 'Ext.data.Store',
model: 'Prmrolefilter.model.PrmRoleFilterModel',
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