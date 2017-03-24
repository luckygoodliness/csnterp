Ext.define("Operatekeyprojectsinfo.store.OperateKeyProjectsInfoStore", {
	extend: 'Ext.data.Store',
model: 'Operatekeyprojectsinfo.model.OperateKeyProjectsInfoModel',
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