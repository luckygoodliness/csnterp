Ext.define("Financialsubject.store.FinancialSubjectStore", {
	extend: 'Ext.data.Store',
model: 'Financialsubject.model.FinancialSubjectModel',
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