app.controller('CustomerController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
		$ocLazyLoad.load([__RESOURCE + "vendor/modules/angular-ui-select/select.min.js?" + __VERSION,
		__RESOURCE + "vendor/modules/angular-ui-select/select.min.css?" + __VERSION,
		__RESOURCE+"vendor/libs/daterangepicker.js?"+__VERSION]);
		$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];$scope.__IMGWEB = __IMGWEB;
		var urlParam = __WEB + 'app.do?channel=' + $stateParams.channel;
	$scope.param = {}; $scope.edit_id = "";
	//定义变量
	$scope.currencyRate = {};$scope.currencyRateList = [];$scope.employeeNameHash = {};
	let aside;
	$httpService.header('method', 'getCurrencyRate');
	$httpService.post(urlParam, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		$scope.currencyRateList = result.data.item.currencyRateList;
		$scope.employeeNameHash = result.data.item.employeeNameHash;
	})
	
	$scope.addEdit = function(editType, currencyRate, i) {
		$scope.editType = editType;
		$scope.currencyRate = {};$scope.edit_id = "";
		if(editType == 'edit' && typeof(currencyRate) != 'undefined') {
			$scope.currencyRate = angular.copy(currencyRate);
			$scope.edit_id = angular.copy(currencyRate.currency_id);
			$scope.edit_index = i;
			let currencySymbol = $scope.currencySymbol;
			currencySymbol.forEach((val, i) => {
				if(currencyRate.currency_name == val.currency_name) $scope.currencyRate.currency = i;
			});
			//工作流审核相关
			$rootScope.param = {};$rootScope.param.auditing = $scope.currencyRate;$rootScope.param.id = $scope.currencyRate.currency_id;
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		aside = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',
				backdrop:"static",container:'#MainController', templateUrl: 'AddEditCurrencyRate.html'});
		aside.$promise.then(function() {
			aside.show();
			$(document).ready(function(){
				
			});
		})
	}

	$scope.saveData = function() {
		console.log(this.currencyRate, $scope.currencyRate);
		if(this.currencyRate == null || this.currencyRate == '') {
			$alert({title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(this.currencyRate.currency_name)) {
			$alert({title: 'Notice', content: '名称必须填写！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.loading.show();
		$scope.param.currencyRate = angular.copy(this.currencyRate);
		$httpService.header('method', 'saveCurrencyRate');
		$httpService.post(urlParam+"&edit_id="+$scope.edit_id, $scope, function(result){
			$scope.loading.percent();
		    $httpService.deleteHeader('method');
			if(result.data.success == false) { 
				return;
			} 
			if($scope.editType == "add") {
				$scope.param.currencyRate.currency_id = result.data.item.currency_id;
				$scope.currencyRateList.push(angular.copy($scope.param.currencyRate));
			}
			if($scope.editType == "edit") {
				$scope.currencyRateList[$scope.edit_index] = angular.copy($scope.param.currencyRate);
			}
			$scope.auditing = {};
			$scope.edit_id = 0;
			aside.hide(); 
		});
	}
	
	$scope.delete = function(delete_id, i) {
	var message = '您确定要删除吗？';
	$scope.confirm({'content': message,'callback': deleteData});
		function deleteData() {
			$scope.param = {};
			$scope.param.delete_id = delete_id;
			$httpService.header('method', 'deleteCurrencyRate');
			$httpService.post(urlParam, $scope, function(result) {
				$scope.loading.percent();
				$httpService.deleteHeader('method');
				if (result.data.success == false) {
					return;
				}
				delete $scope.currencyRateList[i]; 				
			});
		}
	}
	$scope.showChange = function() {
		console.log($scope.currencyRate);
		$scope.currencyRate.currency_name = $scope.currencySymbol[$scope.currencyRate.currency].currency_name;
		$scope.currencyRate.currency_symbol = $scope.currencySymbol[$scope.currencyRate.currency].currency_symbol;
		$scope.currencyRate.currency_sname = $scope.currencySymbol[$scope.currencyRate.currency].currency_sname;
	}
});