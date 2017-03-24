Ext.define("Prmorgrolefilter.store.ScdpOrgStore", {
	extend: 'Ext.data.Store',
model: 'Prmorgrolefilter.model.ScdpOrgModel',
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