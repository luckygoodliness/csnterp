Ext.define("Prmorgrolefilter.store.PrmOrgRoleFilterStore", {
	extend: 'Ext.data.Store',
model: 'Prmorgrolefilter.model.PrmOrgRoleFilterModel',
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