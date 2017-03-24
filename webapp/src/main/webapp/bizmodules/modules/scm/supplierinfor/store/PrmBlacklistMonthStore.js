Ext.define("Supplierinfor.store.PrmBlacklistMonthStore", {
	extend: 'Ext.data.Store',
model: 'Supplierinfor.model.PrmBlacklistMonthModel',
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