Ext.define('Certificatesetrule.view.AccsubjView', {
    extend: 'Scdp.mvc.AbstractCrudView_1',
    modulePath: 'com/csnt/scdp/bizmodules/modules/fad/certificatesetrule',
    aHeight: 480,  //ָ���������߶�
    aWidth: 900,  //ָ�����������
    cpHeight: 100,//��ѯ�������߶�
    epHeight: 0,//�༭���߶�
    allowNullConditions: true,//�Ƿ�����ղ�ѯ����
    queryLayoutFile: 'accsubj-query-layout.xml',
    hideScroll: true,
    canEdit: false
});