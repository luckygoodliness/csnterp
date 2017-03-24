Ext.define("Weekly.store.PrmWeeklyReceiptStore", {
	extend: 'Ext.data.Store',
model: 'Weekly.model.PrmWeeklyReceiptModel',
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