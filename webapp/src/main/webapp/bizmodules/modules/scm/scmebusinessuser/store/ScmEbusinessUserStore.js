Ext.define("Scmebusinessuser.store.ScmEbusinessUserStore", {
	extend: 'Ext.data.Store',
model: 'Scmebusinessuser.model.ScmEbusinessUserModel',
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