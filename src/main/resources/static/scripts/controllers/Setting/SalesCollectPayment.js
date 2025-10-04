app.controller('SalesCollectPaymentController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
		//$ocLazyLoad.load([__RESOURCE + "vendor/modules/angular-ui-select/select.min.js?" + __VERSION]);
		$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];$scope.__IMGWEB = __IMGWEB;
		var urlParam = __WEB + 'app.do?channel=' + $stateParams.channel;
	$scope.param = {}; $scope.edit_id = "";
	//定义变量
	$scope.currencyRate = {};$scope.currencyRateList = [];
	let aside;
	$httpService.header('method', 'getSalesCollectPayment');
	$httpService.post(urlParam, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		$scope.currencyRateList = result.data.item.currencyRateList;
	})
	
	$scope.addEdit = function(editType, currencyRate, i) {
		$scope.editType = editType;
		$scope.currencyRate = {};
		if(editType == 'edit' && typeof(currencyRate) != 'undefined') {
			$scope.edit_id = angular.copy(currencyRate.currency_id);
			$scope.edit_index = i;
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
		$httpService.header('method', 'saveSalesCollectPayment');
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
			$httpService.header('method', 'deleteSalesCollectPayment');
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

});