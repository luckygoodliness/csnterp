Ext.define("Meetingsummary.store.MeetingSummaryStore", {
	extend: 'Ext.data.Store',
model: 'Meetingsummary.model.PrmMeetingSummaryModel',
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