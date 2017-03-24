Ext.define("Weekly.store.PrmMemberTrendStore", {
	extend: 'Ext.data.Store',
model: 'Weekly.model.PrmMemberTrendModel',
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