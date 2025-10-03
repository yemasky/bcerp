app.controller('CurrencyRateController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
		$ocLazyLoad.load([__RESOURCE + "vendor/modules/angular-ui-select/select.min.js?" + __VERSION,
		__RESOURCE + "vendor/modules/angular-ui-select/select.min.css?" + __VERSION,]);
		$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];$scope.__IMGWEB = __IMGWEB;
		var urlParam = __WEB + 'app.do?channel=' + $stateParams.channel;
	$scope.param = {}; $scope.edit_id = "";
	//定义变量
	let aside;
	$httpService.header('method', 'getCurrencyRate');
	$httpService.post(urlParam, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		
	})
	
	$scope.addEdit = function(editType, currencyRate, i) {
		$scope.editType = editType;
		$scope.currencyRate = {};
		if(editType == 'edit' && typeof(currencyRate) != 'undefined') {
			$scope.edit_id = angular.copy(currencyRate.auditing_id);
			$scope.edit_index = i;
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		aside = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',
				backdrop:"static",container:'#MainController', templateUrl: 'AddEditAuditing.html'});
		aside.$promise.then(function() {
			aside.show();
			$(document).ready(function(){
				
			});
		})
	}

	$scope.saveData = function() {
		console.log(this.auditing, $scope.auditing);
		if(this.auditing == null || this.auditing == '') {
			$alert({title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(this.auditing.auditing_name)) {
			$alert({title: 'Notice', content: '名称必须填写！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(this.auditing.module_id) || this.auditing.module_id == "") {
			$alert({title: 'Notice', content: '审核模块必须填写！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.loading.show();
		$scope.param.auditing = angular.copy(this.auditing);
		for(i in this.auditing.examine) {
			$scope.param.auditing.examine[i].employee_id = angular.copy(this.auditing.examine[i].employee_id.e_id);
			$scope.param.auditing.examine[i].position_id = angular.copy(this.auditing.examine[i].position_id.sector_id);
			$scope.param.auditing.module_id = angular.copy(this.auditing.module_id.module_id);
		}
		$httpService.header('method', 'saveCurrencyRate');
		$httpService.post(urlParam+"&edit_id="+$scope.edit_id, $scope, function(result){
			$scope.loading.percent();
		    $httpService.deleteHeader('method');
			if(result.data.success == false) { 
				return;
			} 
			if($scope.editType == "add") {
				$scope.param.auditing.auditing_id = result.data.item.auditing_id;
				$scope.auditingList.push(angular.copy($scope.param.auditing));
			}
			if($scope.editType == "edit") {
				$scope.auditingList[$scope.edit_index] = angular.copy($scope.param.auditing);
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
				delete $scope.auditingList[i]; 				
			});
		}
	}
});