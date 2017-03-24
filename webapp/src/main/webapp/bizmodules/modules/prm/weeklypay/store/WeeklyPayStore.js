Ext.define("Weeklypay.store.WeeklyPayStore", {
	extend: 'Ext.data.Store',
model: 'Weeklypay.model.PrmWeeklyPayModel',
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