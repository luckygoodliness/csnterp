Ext.define("Responsiblesubject.store.ScmResponsibleSubjectStore", {
	extend: 'Ext.data.Store',
model: 'Responsiblesubject.model.ScmResponsibleSubjectModel',
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