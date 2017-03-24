Ext.define("Prmblacklistmonth.store.PrmBlacklistMonthStore", {
	extend: 'Ext.data.Store',
model: 'Prmblacklistmonth.model.PrmBlacklistMonthModel',
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