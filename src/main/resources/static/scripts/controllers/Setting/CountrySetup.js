app.controller('CountrySetupController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
		$ocLazyLoad.load([__RESOURCE+"vendor/libs/md5.min.js",__RESOURCE + "vendor/libs/utils.js"]);
		$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];
	$scope.param = {}; $scope.edit_id = 0;
	//定义变量
	$scope.country = {};$scope.countryList = {};
	let aside;
	$httpService.header('method', 'getCountry');
	$httpService.post(__WEB + 'app.do?channel='+$stateParams.channel, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		$scope.countryList = result.data.item.countryList;//部门职位
		//$scope.$apply();//刷新数据
	})
	
	$scope.addEdit = function(country) {
		if(typeof(country) != 'undefined') {
			$scope.country = country;
			$scope.edit_id = angular.copy(country.country_id);
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		aside = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',
				backdrop:"static",container:'#MainController', templateUrl: 'AddEditCountry.html'});
		aside.$promise.then(function() {
			aside.show();
			$(document).ready(function(){
				
			});
		})
	}

	$scope.saveData = function() {
		if(this.country == null || this.country == '') {
			$alert({title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(this.country.country_sname)) {
			$alert({title: 'Notice', content: '英文名称必须填写！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.loading.show();
		$scope.param.country = angular.copy(this.country);
		$httpService.header('method', 'saveCountry');
		$httpService.post(__WEB + 'app.do?channel='+$stateParams.channel+"&edit_id="+$scope.edit_id, $scope, function(result){
			$scope.loading.percent();
		    $httpService.deleteHeader('method');
			if(result.data.success == false) { 
				return; 
			} 
			$scope.country = {};
			$scope.edit_id = 0;
			aside.hide(); 
			$scope.countryList = result.data.item.countryList;//部门职位
			$scope.reload($stateParams);
		});
	}
});