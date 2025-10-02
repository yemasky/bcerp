app.controller('SysTypeController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
		$ocLazyLoad.load([__RESOURCE+"vendor/libs/md5.min.js",__RESOURCE + "vendor/libs/utils.js"]);
		$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];
	$scope.param = {};$scope.edit_id = 0;
	//定义变量
	$scope.systype = {};$scope.systypeList = [];//系统分类
	$scope.edit_index = 0;$scope.editType = "";
	
	let aside;
	$httpService.header('method', 'getSystype');
	$httpService.post(__WEB + 'app.do?channel='+$stateParams.channel, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		$scope.systypeList = result.data.item.systypeList;//
		//$scope.$apply();//刷新数据
	})
	
	$scope.addEdit = function(editType, systype, i) {
		$scope.editType = editType;
		if(typeof(systype) != 'undefined') {
			$scope.systype = systype;
			$scope.edit_id = angular.copy(systype.systype_id);
			$scope.edit_index = i;
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		aside = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',
				backdrop:"static",container:'#MainController', templateUrl: 'AddEditSystype.html'});
		aside.$promise.then(function() {
			aside.show();
			$(document).ready(function(){
				
			});
		})
	}

	$scope.saveData = function() {
		if(this.systype == null || this.systype == '') {
			$alert({title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(this.systype.systype_name)) {
			$alert({title: 'Notice', content: '名称必须填写！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.loading.show();
		$scope.param.systype = angular.copy(this.systype);
		$httpService.header('method', 'saveSystype');
		$httpService.post(__WEB + 'app.do?channel='+$stateParams.channel+"&edit_id="+$scope.edit_id, $scope, function(result){
			$scope.loading.percent();
		    $httpService.deleteHeader('method');
			if(result.data.success == false) { 
				return; 
			} 
			
			if($scope.editType == "add") {
				$scope.param.systype.systype_id = result.data.item.systype_id;
				$scope.systypeList.push(angular.copy($scope.param.systype));
			}
			if($scope.editType == "edit") {
				$scope.systypeList[$scope.edit_index] = angular.copy($scope.param.systype);
			}
			$scope.systype = {};
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
			$httpService.header('method', 'deleteSystype');
			$httpService.post(__WEB + 'app.do?channel='+$stateParams.channel, $scope, function(result) {
				$scope.loading.percent();
				$httpService.deleteHeader('method');
				if (result.data.success == false) {
					return;
				}
				delete $scope.systypeList[i]; 				
			});
		}
	}
});