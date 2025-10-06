app.controller('FactoryController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
		$ocLazyLoad.load([__RESOURCE+"vendor/libs/daterangepicker.js?"+__VERSION,]);
		$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];
		var urlParam = __WEB + 'app.do?channel=' + $stateParams.channel;
	$scope.param = {}; $scope.edit_id = 0;$scope.edit_index = 0;$scope.editType = "";
	//定义变量
	$scope.classify = {};$scope.classifyList = [];
	$scope.categoryHash = {};$scope.countryHash = {};
	let aside;
	$httpService.header('method', 'getClassify');
	$httpService.post(urlParam, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		$scope.countryList = result.data.item.countryList;//
		$scope.categoryList = result.data.item.categoryList;//
		$scope.classifyList = result.data.item.classifyList;//
		for (var i in $scope.categoryList) {
			$scope.categoryHash[$scope.categoryList[i].category_id] = $scope.categoryList[i];
		}
		for (var i in $scope.countryList) {
			$scope.countryHash[$scope.countryList[i].country_id] = $scope.countryList[i];
		}
		//$scope.$apply();//刷新数据
	})
	
	$scope.addEdit = function(editType, classify, i) {
		$scope.editType = editType;
		if(typeof(classify) != 'undefined') {
			$scope.classify = angular.copy(classify);
			$scope.edit_id = angular.copy(classify.classify_id);
			$scope.edit_index = i;
			$('#country_id').val(classify.country_id);
			$('#category_id').val(classify.category_id);
		}
		if(editType == 'add') $scope.classify = {};
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		aside = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',
				backdrop:"static",container:'#MainController', templateUrl: 'AddEditFactory.html'});
		aside.$promise.then(function() {
			aside.show();
			$(document).ready(function(){
				
			});
		})
	}

	$scope.saveData = function() {
		if(this.classify == null || this.classify == '') {
			$alert({title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(this.classify.classify_enname)) {
			$alert({title: 'Notice', content: '名称必须填写！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.loading.show();
		$scope.param.classify = angular.copy(this.classify);
		$httpService.header('method', 'saveClassify');
		$httpService.post(urlParam+"&edit_id="+$scope.edit_id, $scope, function(result){
			$scope.loading.percent();
		    $httpService.deleteHeader('method');
			if(result.data.success == false) { 
				return; 
			} 
			
			if($scope.editType == "add") {
				$scope.param.classify.classify_id = result.data.item.classify_id;
				$scope.classifyList.push(angular.copy($scope.param.classify));
			}
			if($scope.editType == "edit") {
				$scope.classifyList[$scope.edit_index] = angular.copy($scope.param.classify);
			}
			$scope.classify = {};
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
			$httpService.header('method', 'deleteClassify');
			$httpService.post(urlParam, $scope, function(result) {
				$scope.loading.percent();
				$httpService.deleteHeader('method');
				if (result.data.success == false) {
					return;
				}
				delete $scope.classifyList[i]; 				
			});
		}
	}
});