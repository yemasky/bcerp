app.controller('CompanyController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
	$scope.param = {}; $scope.company = {}; $scope.param.company = {};$scope.__IMGWEB = __IMGWEB; 
	$scope.company_edit_id = "";$scope.bank = {};
	$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];
	$ocLazyLoad.load([__RESOURCE+"vendor/libs/moment.min.js?"+__VERSION,
					  __RESOURCE+"vendor/libs/daterangepicker.js?"+__VERSION,
					  __RESOURCE+"vendor/modules/angular-ui-select/select.min.js?"+__VERSION,
					  __RESOURCE+"vendor/modules/angular-ui-select/select.min.css?"+__VERSION,
					  __RESOURCE+"editor/kindeditor/kindeditor-all.js?"+__VERSION,
					  //__RESOURCE+"editor/kindeditor/kindeditor-all-min.js?"+__VERSION,
					  __RESOURCE+"editor/kindeditor/themes/default/default.css",
					  __RESOURCE+"vendor/libs/md5.min.js",__RESOURCE + "vendor/libs/utils.js"]);
	/////////
	$scope.people = [
	{ name: 'Adam',      email: 'adam@email.com',      age: 12, country: 'United States' },
	{ name: 'Amalie',    email: 'amalie@email.com',    age: 12, country: 'Argentina' }
	];

	$scope.availableColors = ['Red','Green','Blue','Yellow','Magenta','Maroon','Umbra','Turquoise'];
	
	$scope.multipleDemo = {};
	$scope.multipleDemo.colors = ['Blue','Red'];
	$scope.multipleDemo.selectedPeople = [$scope.people[5], $scope.people[4]];
	$scope.multipleDemo.selectedPeopleWithGroupBy = [$scope.people[8], $scope.people[6]];
	//////////
	let asideCompany;
	$httpService.header('method', 'getCompany');
	$httpService.post(__WEB + 'app.do?channel='+$stateParams.channel, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		$scope.companyList = result.data.item.companyList;//公司
		$scope.bankList = result.data.item.bankList;
	})
	
	$scope.addEdit = function(company) {
		if(typeof(company) != 'undefined') {
			$scope.company_edit_id = angular.copy(company.company_id);
			$scope.company = company;
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		asideCompany = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',
				backdrop:"static",container:'#MainController', templateUrl: __RESOURCE + 'views/Setting/CompanyAddEditAside.html?'+__VERSION});
		asideCompany.$promise.then(function() {
			asideCompany.show();
			$(document).ready(function(){
				$scope.setImage();
			});
		})
	}
	//<!--图片上传及操作-->
	//images
	$scope.setImage = function() {
		//$scope.__WEB+'app.do?method=uploadCompanyLogo&channel='+$rootScope.__ImagesUploadUrl;
		var uploadJsonUrl = $scope.__WEB+'index/uploadCompanyLogo?channel='+__ImagesUploadUrl;
		var fileManagerJsonUrl = $scope.__WEB+'index/fileManager?channel='+__ImagesManagerUrl;
		window.K = KindEditor;
		var editor = K.editor({
			uploadJson : uploadJsonUrl,fileManagerJson : fileManagerJsonUrl,allowFileManager : true,formatUploadUrl: false
		});
		K('.set_image_src').click(function() {
			editor.loadPlugin('image', function() {
				editor.plugin.imageDialog({
					//clickFn : function(url, title, width, height, border, align) {
					clickFn : function(url) {
						editor.hideDialog();
						$scope.company.company_logo = url;
						$scope.$apply();//刷新数据
					}
				});
			});
		});
	} 
	$scope.saveData = function() {
		console.log(this.company, $scope.company);
		if(this.company == null || this.company == '') {
			$alert({title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(this.company.company_name)) {
			$alert({title: 'Notice', content: '企业名称必须填写！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.loading.show();
		$scope.param.company = angular.copy(this.company);
		$httpService.header('method', 'saveCompany');
		$httpService.post(__WEB + 'app.do?channel='+$stateParams.channel+"&company_edit_id="+$scope.company_edit_id, $scope, function(result){
			$scope.loading.percent();
		    $httpService.deleteHeader('method');
			if(result.data.success == '0') { 
				return;
			} 
			$scope.dataList = result.data.item;
			$scope.company = {};
			asideCompany.hide();
			let path = '/app/'+$rootScope._self_module.module_channel+'/'+$stateParams.view+'/'
							+$stateParams.id+'/'+$stateParams.channel;
			$location.path(path);
		});
	}
	$scope.addEditBank = function(bank) {
		if(typeof(bank) != 'undefined') {
			$scope.bank = bank;
			$scope.edit_id = angular.copy(bank.bank_id);
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑银行账户");
		$scope.action = '添加/编辑银行账户';
		aside = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',
				backdrop:"static",container:'#MainController', templateUrl: 'AddEditBank.html'+__VERSION});
		aside.$promise.then(function() {
			aside.show();
			$(document).ready(function(){
				
			});
		})
	}

	$scope.saveDataBank = function() {
		if(this.bank == null || this.bank == '') {
			$alert({title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(this.bank.bank_name)) {
			$alert({title: 'Notice', content: '名称必须填写！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.loading.show();
		$scope.param = {};
		$scope.param.bank = angular.copy(this.bank);
		$httpService.header('method', 'saveBank');
		$httpService.post(__WEB + 'app.do?channel='+$stateParams.channel+"&edit_id="+$scope.edit_id, $scope, function(result){
			$scope.loading.percent();
		    $httpService.deleteHeader('method');
			if(result.data.success == '0') { 
				return; 
			} 
			$scope.seaport = {};
			$scope.edit_id = 0;
			aside.hide(); 
			$scope.reload($stateParams);
		});
	}
});