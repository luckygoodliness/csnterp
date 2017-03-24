Ext.define("Weeklyreceipt.store.WeeklyReceiptStore", {
	extend: 'Ext.data.Store',
model: 'Weeklyreceipt.model.PrmWeeklyReceiptModel',
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