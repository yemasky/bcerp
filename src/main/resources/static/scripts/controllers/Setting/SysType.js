app.controller('SysTypeController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
	$scope.param = {}; $scope.category = {};$scope.categoryList = {};$scope.edit_id = 0;//定义变量
	$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];$scope.edit_index = 0;$scope.editType = "";
	$ocLazyLoad.load([__RESOURCE+"vendor/libs/md5.min.js",__RESOURCE + "vendor/libs/utils.js"]);
	let aside;
	$httpService.header('method', 'getCategory');
	$httpService.post(__WEB + 'app.do?channel='+$stateParams.channel, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		$scope.categoryList = result.data.item.categoryList;//部门职位
		//$scope.$apply();//刷新数据
	})
	
	$scope.addEdit = function(editType, category, i) {
		$scope.editType = editType;
		if(typeof(category) != 'undefined') {
			$scope.category = category;
			$scope.edit_id = angular.copy(category.category_id);
			$scope.edit_index = i;
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		aside = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',
				backdrop:"static",container:'#MainController', templateUrl: 'AddEditCategory.html'+__VERSION});
		aside.$promise.then(function() {
			aside.show();
			$(document).ready(function(){
				
			});
		})
	}

	$scope.saveData = function() {
		if(this.category == null || this.category == '') {
			$alert({title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(this.category.category_name)) {
			$alert({title: 'Notice', content: '名称必须填写！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.loading.show();
		$scope.param.category = angular.copy(this.category);
		$httpService.header('method', 'saveCategory');
		$httpService.post(__WEB + 'app.do?channel='+$stateParams.channel+"&edit_id="+$scope.edit_id, $scope, function(result){
			$scope.loading.percent();
		    $httpService.deleteHeader('method');
			if(result.data.success == false) { 
				return; 
			} 
			
			if($scope.editType == "add") {
				$scope.param.category.category_id = result.data.item.category_id;
				$scope.categoryList.push(angular.copy($scope.param.category));
			}
			if($scope.editType == "edit") {
				$scope.categoryList[$scope.edit_index] = angular.copy($scope.param.category);
			}
			$scope.category = {};
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
			$httpService.header('method', 'deleteCategory');
			$httpService.post(__WEB + 'app.do?channel='+$stateParams.channel, $scope, function(result) {
				$scope.loading.percent();
				$httpService.deleteHeader('method');
				if (result.data.success == false) {
					return;
				}
				delete $scope.categoryList[i]; 				
			});
		}
	}
});