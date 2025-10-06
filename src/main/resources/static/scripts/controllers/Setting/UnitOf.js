app.controller('UnitOfController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
		$ocLazyLoad.load([__RESOURCE+"vendor/libs/md5.min.js",__RESOURCE + "vendor/libs/utils.js"]);
		$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];
		var urlParam = __WEB + 'app.do?channel=' + $stateParams.channel;
	$scope.param = {};$scope.edit_id = 0; $scope.edit_index = 0;$scope.editType = "";
	//定义变量
	$scope.unit = {};$scope.unitList = [];
	let aside;
	$httpService.header('method', 'getUnit');
	$httpService.post(urlParam, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		$scope.unitList = result.data.item.unitList;//部门职位
		//$scope.$apply();//刷新数据
	})
	
	$scope.addEdit = function(editType, unit, i) {
		$scope.editType = editType;
		if(typeof(unit) != 'undefined') {
			$scope.unit = angular.copy(unit);
			$scope.edit_id = angular.copy(unit.unit_id);
			$scope.edit_index = i;
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		aside = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',
				backdrop:"static",container:'#MainController', templateUrl: 'AddEditUnit.html'});
		aside.$promise.then(function() {
			aside.show();
			$(document).ready(function(){
				
			});
		})
	}

	$scope.saveData = function() {
		if(this.unit == null || this.unit == '') {
			$alert({title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(this.unit.unit_name)) {
			$alert({title: 'Notice', content: '名称必须填写！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.loading.show();
		$scope.param.unit = angular.copy(this.unit);
		$httpService.header('method', 'saveUnit');
		$httpService.post(urlParam+"&edit_id="+$scope.edit_id, $scope, function(result){
			$scope.loading.percent();
		    $httpService.deleteHeader('method');
			if(result.data.success == false) { 
				return; 
			} 
			
			if($scope.editType == "add") {
				$scope.param.unit.unit_id = result.data.item.unit_id;
				$scope.unitList.push(angular.copy($scope.param.unit));
			}
			if($scope.editType == "edit") {
				$scope.unitList[$scope.edit_index] = angular.copy($scope.param.unit);
			}
			$scope.unit = {};
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
			$httpService.header('method', 'deleteUnit');
			$httpService.post(urlParam, $scope, function(result) {
				$scope.loading.percent();
				$httpService.deleteHeader('method');
				if (result.data.success == false) {
					return;
				}
				delete $scope.unitList[i]; 				
			});
		}
	}
});