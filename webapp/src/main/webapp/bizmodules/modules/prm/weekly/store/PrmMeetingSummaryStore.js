Ext.define("Weekly.store.PrmMeetingSummaryStore", {
	extend: 'Ext.data.Store',
model: 'Weekly.model.PrmMeetingSummaryModel',
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