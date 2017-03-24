Ext.define("Subjectsubject.store.NonProjectSubjectSubjectStore", {
	extend: 'Ext.data.Store',
model: 'Subjectsubject.model.NonProjectSubjectSubjectModel',
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