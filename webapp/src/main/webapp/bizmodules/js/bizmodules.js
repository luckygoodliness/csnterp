Ext.Loader.setConfig({
    disableCaching: true
});
document.oncontextmenu = function (event) {
};
Ext.onReady(function () {
    Scdp.MainFrameEvents.addListener('ready', function () {
        Scdp.DebugUtil.logInfo('MainFrame Ready!');

        // 角色模块、项目过滤缓存
        Scdp.doAction("roleFilterCache-Add", null, null, null, false, null, null);
    });
});

Ext.ns("Erp.Util");
Ext.ns("Erp.ArrayUtil");
Ext.ns("Erp.MathUtil");
//Ext.ns("Erp.CacheUtil");
//注册操作文件公共类
Ext.ns("Erp.FileUtil");
//注册静态变量公共类
Ext.ns("Erp.Const");


Ext.Loader.setPath('Brief', 'bizmodules/modules/prm/brief');

<!--****************************运管管理begin*******************************-->
<!--****************************项目管理begin*******************************-->

//客户管理
Ext.Loader.setPath('Prmcustomer', 'bizmodules/modules/prm/prmcustomer');

//合同管理
Ext.Loader.setPath('Contract', 'bizmodules/modules/prm/contract');
Ext.Loader.setPath('ContractC', 'bizmodules/modules/prm/contractc');
Ext.Loader.setPath('ContractE', 'bizmodules/modules/prm/contracte');

Ext.Loader.setPath('Contractremove', 'bizmodules/modules/prm/contractremove');
//关联单位表C
Ext.Loader.setPath('Prmassociatedunitsc', 'bizmodules/modules/prm/prmassociatedunitsc');
1
//采购申请
Ext.Loader.setPath('Prmpurchasereq', 'bizmodules/modules/prm/prmpurchasereq');

//项目角色过滤
Ext.Loader.setPath('Prmrolefilter', 'bizmodules/modules/prm/prmrolefilter');

//部门角色过滤
Ext.Loader.setPath('Prmorgrolefilter', 'bizmodules/modules/prm/prmorgrolefilter');

//项目收款
Ext.Loader.setPath('Receipts', 'bizmodules/modules/prm/receipts');

//待确认收款
Ext.Loader.setPath('Prmunknownreceipts', 'bizmodules/modules/prm/prmunknownreceipts');

//开票申请
Ext.Loader.setPath('Prmbilling', 'bizmodules/modules/prm/prmbilling');

//项目追踪
//人员动向
Ext.Loader.setPath('Membertrend', 'bizmodules/modules/prm/membertrend');
//项目简报
Ext.Loader.setPath('Brief', 'bizmodules/modules/prm/brief');
//项目周报
Ext.Loader.setPath('Weekly', 'bizmodules/modules/prm/weekly');
// 到货确认
Ext.Loader.setPath('Goodsarrival', 'bizmodules/modules/prm/goodsarrival');
//收款计量
Ext.Loader.setPath('Collectionmeasure', 'bizmodules/modules/prm/collectionmeasure');
//工期进度
Ext.Loader.setPath('Progress', 'bizmodules/modules/prm/progress');
//问题申报
Ext.Loader.setPath('Problem', 'bizmodules/modules/prm/problem');
//会议纪要
Ext.Loader.setPath('Meetingsummary', 'bizmodules/modules/prm/meetingsummary');
//项目结算
Ext.Loader.setPath('Finalestimate', 'bizmodules/modules/prm/finalestimate');
//项目归档
Ext.Loader.setPath('Archiving', 'bizmodules/modules/prm/archiving');
// 2.1项目计划

//2.2 立项变更
//2.2.1 项目立项
Ext.Loader.setPath('Projectmain', 'bizmodules/modules/prm/projectmain');

//2.2.2 项目新增与变更
Ext.Loader.setPath('Prmprojectmainc', 'bizmodules/modules/prm/prmprojectmainc');
//项目报表
Ext.Loader.setPath('PrmReport', 'bizmodules/modules/prm/report');
Ext.Loader.setPath('PrmExamPeriod', 'bizmodules/modules/prm/prmexamperiod');
Ext.Loader.setPath('PrmExamdateHistory', 'bizmodules/modules/prm/prmexamdatehistory');

//<!--****************************非项目资金管理begin*************************-->
//4.1.非项目资金管理-非项目费用内容维护
Ext.Loader.setPath('Financialsubject', 'bizmodules/modules/nonprm/financialsubject');
//4.2非项目资金管理 - 非项目费用内容分配
Ext.Loader.setPath('Subjectsubject', 'bizmodules/modules/nonprm/subjectsubject');
//4.3非项目资金管理 -预算计划申报
Ext.Loader.setPath('Budget', 'bizmodules/modules/nonprm/budget');
//4.4非项目资金管理 -收支预算平衡表
Ext.Loader.setPath('Income', 'bizmodules/modules/nonprm/income');
//4.5.非项目资金管理 -预算调整
Ext.Loader.setPath('Budgetajust', 'bizmodules/modules/nonprm/budgetajust');
//4.6非项目资金管理 -预算拨款
Ext.Loader.setPath('Budgeth', 'bizmodules/modules/nonprm/budgeth');
//4.7非项目资金管理 -预算执行与监控
Ext.Loader.setPath('Monitor', 'bizmodules/modules/nonprm/monitor');
//4.8.非项目资金管理 -预算年终评估
Ext.Loader.setPath('Monitory', 'bizmodules/modules/nonprm/monitory');
//4.5非项目资金管理 -预算调整
Ext.Loader.setPath('Budgetajust', 'bizmodules/modules/nonprm/budgetajust');
//4.5非项目资金管理 -月底实时录入
Ext.Loader.setPath('Monitorm', 'bizmodules/modules/nonprm/monitorm');
//4.5【人工费用】、【其他分摊费用】月度实际发生录入
Ext.Loader.setPath('Monitorlaborcostandothershareexpense', 'bizmodules/modules/nonprm/monitorlaborcostandothershareexpense');
//非项目采购申请
Ext.Loader.setPath('Nonprmpurchasereq', 'bizmodules/modules/nonprm/nonprmpurchasereq');
<!--****************************供应链管理begin*****************************-->
//3.供应链管理
//3.1配置
//3.1.1 项目设置
Ext.Loader.setPath('Responsibleproject', 'bizmodules/modules/scm/responsibleproject');
//3.1.1 物料科目设置
Ext.Loader.setPath('Responsibleclass', 'bizmodules/modules/scm/responsibleclass');
//3.1.2.费用科目设置
Ext.Loader.setPath('Responsiblesubject', 'bizmodules/modules/scm/responsiblesubject');
//部门专员设置(二级目录)
Ext.Loader.setPath('Scmdepartmentbuyer', 'bizmodules/modules/scm/scmdepartmentbuyer');
//3.1.3.部门设置
Ext.Loader.setPath('Responsibledepartment', 'bizmodules/modules/scm/responsibledepartment');
//3.2采购计划
Ext.Loader.setPath('Purchaseplan', 'bizmodules/modules/prm/purchaseplan');
//3.2.1采购分包
Ext.Loader.setPath('Budgetprincipal', 'bizmodules/modules/prm/budgetprincipal');
//3.2.2申请单分配
Ext.Loader.setPath('Requestallot', 'bizmodules/modules/scm/requestallot');
//3.3.采购办理
Ext.Loader.setPath('Purchasereq', 'bizmodules/modules/scm/purchasereq');
//3.4.采购合同管理
//3.4.1合同编制
Ext.Loader.setPath('Scmcontract', 'bizmodules/modules/scm/scmcontract');
//3.4.2合同变更
Ext.Loader.setPath('Scmcontractchange', 'bizmodules/modules/scm/scmcontractchange');
//3.4.3合同收支情况
Ext.Loader.setPath('Balanceofcontract', 'bizmodules/modules/scm/balanceofcontract');
//采购合同总览
Ext.Loader.setPath('ScmReport', 'bizmodules/modules/scm/report');
//3.6票据管理
//3.6.1票据催收计划
Ext.Loader.setPath('Scmoverduereceivables', 'bizmodules/modules/scm/scmoverduereceivables');
//3.6.1票据计划（新）
Ext.Loader.setPath('Notesplan', 'bizmodules/modules/scm/notesplan');

//3.6.12供方考评方案维护
Ext.Loader.setPath('Scmsaecase', 'bizmodules/modules/scm/scmsaecase');

Ext.Loader.setPath('Scmsaeappraisercase', 'bizmodules/modules/scm/scmsaeappraisercase');

//3.7支付管理
//3.7.1月度黑名单
Ext.Loader.setPath('Prmblacklistmonth', 'bizmodules/modules/scm/prmblacklistmonth');

//3.8.1物料科目管理
Ext.Loader.setPath('Materialclass', 'bizmodules/modules/scm/materialclass');
//物料细目
Ext.Loader.setPath('Materialitem', 'bizmodules/modules/scm/materialitem');
//3.8.2供应商基本信息
Ext.Loader.setPath('Supplierinfor', 'bizmodules/modules/scm/supplierinfor');


//3.6.13年度供方考评管理
Ext.Loader.setPath('Scmsae', 'bizmodules/modules/scm/scmsae');

<!--****************************资产管理begin*******************************-->
//5.1固定资产卡片
Ext.Loader.setPath('Card', 'bizmodules/modules/asset/card');
//5.2设备维修申请
Ext.Loader.setPath('Maintain', 'bizmodules/modules/asset/maintain');
//5.3设备转移
Ext.Loader.setPath('Transfer', 'bizmodules/modules/asset/transfer');
//5.4设备年检登记
Ext.Loader.setPath('Check', 'bizmodules/modules/asset/check');
//5.5资产报废
Ext.Loader.setPath('Apply', 'bizmodules/modules/asset/apply');
//5.6设备类型维护
Ext.Loader.setPath('Type', 'bizmodules/modules/asset/type');
//资产报表
Ext.Loader.setPath('AssetReport', 'bizmodules/modules/asset/report');
<!--****************************账务管理begin*******************************-->
//发票录入
Ext.Loader.setPath('Invoice', 'bizmodules/modules/fad/invoice');
//费用申请
Ext.Loader.setPath('Cashreq', 'bizmodules/modules/fad/cashreq');
//支付申请
Ext.Loader.setPath('Payreq', 'bizmodules/modules/fad/payreq');
//凭证编辑
Ext.Loader.setPath('Certificate', 'bizmodules/modules/fad/certificate');
//辅助核算项NC人员设定
Ext.Loader.setPath('Ncperson', 'bizmodules/modules/fad/ncperson');
//辅助核算项NC部门设定
Ext.Loader.setPath('Ncorg', 'bizmodules/modules/fad/ncorg');
//辅助核算项其他设定
Ext.Loader.setPath('Rtfreevalue', 'bizmodules/modules/fad/rtfreevalue');
//凭证生成规则维护模块
Ext.Loader.setPath('Certificatesetrule', 'bizmodules/modules/fad/certificatesetrule');
//现金核销
Ext.Loader.setPath('Cashclearance', 'bizmodules/modules/fad/cashclearance');
//客商管理
Ext.Loader.setPath('FadSupplierinfo', 'bizmodules/modules/fad/supplierinfo');
<!--****************************运营管理begin*******************************-->
//1.2经营开发
Ext.Loader.setPath('Businessbidinfo', 'bizmodules/modules/operate/businessbidinfo');

//1.2.2合同协议草案
Ext.Loader.setPath('Bidcontractplan', 'bizmodules/modules/operate/bidcontractplan');
//1.5重点经营项目
Ext.Loader.setPath('Operatekeyprojectsinfo', 'bizmodules/modules/operate/operatekeyprojectsinfo');
//1.6资源调度
Ext.Loader.setPath('Resourcerequest', 'bizmodules/modules/operate/resourcerequest');
//<!--经营管理-->
Ext.Loader.setPath('Companyplan', 'bizmodules/modules/operate/companyplan');

<!--****************************文件管理begin*******************************-->
//文件管理
Ext.Loader.setPath('Cdmfile', 'bizmodules/modules/cdm/cdmfile');
<!--****************************系统相关表begin*******************************-->
//部门相关
Ext.Loader.setPath('Erpoffice', 'bizmodules/modules/erpsys/erpoffice');
Ext.Loader.setPath('ErpReport', 'bizmodules/modules/report/empty');

Ext.Loader.setPath("ErpMvc", "bizmodules/modules/common");

<!--****************************财务报表*******************************-->
Ext.Loader.setPath('FadReport', 'bizmodules/modules/fad/report');

<!--****************************经营管理报表*******************************-->
Ext.Loader.setPath('OperateReport', 'bizmodules/modules/operate/report');
//供方设置
Ext.Loader.setPath('Supplierlimit', 'bizmodules/modules/scm/supplierlimit');
//供方设置变更
Ext.Loader.setPath('Scmsupplierlimitchange', 'bizmodules/modules/scm/scmsupplierlimitchange');
//供方变更
Ext.Loader.setPath('Supplierinfochange', 'bizmodules/modules/scm/supplierinfochange');
//应付账款调整
Ext.Loader.setPath('Fadsupplieradjust', 'bizmodules/modules/scm/fadsupplieradjust');
//供方映射表
Ext.Loader.setPath('Fadsuppliermapping', 'bizmodules/modules/scm/fadsuppliermapping');

//年度供方考评
Ext.Loader.setPath('Scmsaetask', 'bizmodules/modules/scm/scmsaetask');
//电商用户
Ext.Loader.setPath('Scmebusinessuser', 'bizmodules/modules/scm/scmebusinessuser');

//年度合格供方审批
Ext.Loader.setPath('Scmsaeapproval', 'bizmodules/modules/scm/scmsaeapproval');

//保证金申请利率维护
Ext.Loader.setPath('Fadinterestrate', 'bizmodules/modules/fad/fadinterestrate');

//工作流删除
Ext.Loader.setPath('WorkflowDelete', 'bizmodules/modules/workflow/workflowdelete');
//价格库
Ext.Loader.setPath('Scmpricedatabase', 'bizmodules/modules/scm/scmpricedatabase');
//测试ztrain
Ext.Loader.setPath('Ztraincode', 'bizmodules/modules/ztrain/ztraincode');
//测试ztest
Ext.Loader.setPath('Ztestcode', 'bizmodules/modules/ztest/ztestcode');