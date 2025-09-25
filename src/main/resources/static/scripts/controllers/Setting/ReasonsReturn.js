app.controller('ReasonsReturntroller', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
	$scope.param = {}; $scope.edit_id = "";//定义变量
	$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];
	$ocLazyLoad.load([__RESOURCE+"vendor/libs/moment.min.js?"+__VERSION,
					  __RESOURCE+"vendor/libs/md5.min.js",__RESOURCE + "vendor/libs/utils.js"]);
	let aside;
	$httpService.header('method', 'getCountry');
	$httpService.post('app.do?'+param, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		let countryList = result.data.item.countryList;//部门职位
	})
	
	$scope.addEdit = function(company) {
		if(typeof(company) != 'undefined') {
			$scope.company_edit_id = angular.copy(company.company_id);
			$scope.company = company;
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		aside = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',
				backdrop:"static",container:'#MainController', templateUrl: __RESOURCE + 'views/Setting/CompanyAddEditAside.html?'+__VERSION});
		aside.$promise.then(function() {
			aside.show();
			$(document).ready(function(){
				$scope.setImage();
			});
		})
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
			console.log(path);
			$location.path();
		});
	}
});