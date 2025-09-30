app.controller('AuditingController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
		$ocLazyLoad.load([__RESOURCE+"vendor/libs/moment.min.js?"+__VERSION,
					  __RESOURCE+"vendor/libs/md5.min.js",__RESOURCE + "vendor/libs/utils.js"]);
	$scope.param = {}; $scope.edit_id = "";//定义变量
	$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];$scope.__IMGWEB = __IMGWEB;
	var urlParam = __WEB + 'app.do?channel=' + $stateParams.channel;
	$scope.auditingList = [];
	let aside;
	$httpService.header('method', 'getAuditing');
	$httpService.post(urlParam, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		$scope.auditingList = result.data.item.auditingList;//
	})
	
	$scope.addEdit = function(editType, auditing, i) {
		$scope.editType = editType;
		if(typeof(auditing) != 'undefined') {
			$scope.auditing = angular.copy(auditing);
			$scope.edit_id = angular.copy(auditing.auditing_id);
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
			$alert({title: 'Notice', content: '企业名称必须填写！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.loading.show();
		$scope.param.auditing = angular.copy(this.auditing);
		$httpService.header('method', 'saveAuditing');
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