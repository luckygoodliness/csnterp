Ext.define("Membertrend.store.MemberTrendStore", {
	extend: 'Ext.data.Store',
model: 'Membertrend.model.MemberTrendModel',
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