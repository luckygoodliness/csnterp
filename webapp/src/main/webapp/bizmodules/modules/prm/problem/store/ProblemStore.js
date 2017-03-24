Ext.define("Problem.store.ProblemStore", {
	extend: 'Ext.data.Store',
model: 'Problem.model.PrmProblemModel',
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