Ext.define("Responsibledepartment.store.ResponsibleDepartmentStore", {
	extend: 'Ext.data.Store',
model: 'Responsibledepartment.model.ScmResponsibleDepartmentModel',
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