Ext.define("Problem.store.PrmProblemFeedbackStore", {
	extend: 'Ext.data.Store',
model: 'Problem.model.PrmProblemFeedbackModel',
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