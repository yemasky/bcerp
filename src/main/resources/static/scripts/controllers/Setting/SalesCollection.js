app.controller('SalesCollectionController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
		//$ocLazyLoad.load([__RESOURCE + "vendor/modules/angular-ui-select/select.min.js?" + __VERSION]);
		$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];$scope.__IMGWEB = __IMGWEB;
		var urlParam = __WEB + 'app.do?channel=' + $stateParams.channel;
	$scope.param = {}; $scope.edit_id = "";
	//定义变量
	$scope.collection = {};$scope.collectionList = [];
	let aside;
	$httpService.header('method', 'getSalesCollection');
	$httpService.post(urlParam, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		$scope.collectionList = result.data.item.collectionList;
	})
	
	$scope.addEdit = function(editType, collection, i) {
		$scope.editType = editType;
		$scope.collection = {};
		if(editType == 'edit' && typeof(collection) != 'undefined') {
			$scope.edit_id = angular.copy(collection.collection_id);
			$scope.edit_index = i;
			$scope.collection = angular.copy(collection);
			//工作流审核相关
			$rootScope.param = {};$rootScope.param.auditing = $scope.collection;$rootScope.param.id = $scope.collection.collection_id;
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		aside = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',
				backdrop:"static",container:'#MainController', templateUrl: 'AddEditSalesCollection.html'});
		aside.$promise.then(function() {
			aside.show();
			$(document).ready(function(){
				
			});
		})
	}

	$scope.saveData = function() {
		console.log(this.collection, $scope.collection);
		if(this.collection == null || this.collection == '') {
			$alert({title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(this.collection.collection_num)) {
			$alert({title: 'Notice', content: '序号必须填写！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.loading.show();
		$scope.param.collection = angular.copy(this.collection);
		$httpService.header('method', 'saveSalesCollection');
		$httpService.post(urlParam+"&edit_id="+$scope.edit_id, $scope, function(result){
			$scope.loading.percent();
		    $httpService.deleteHeader('method');
			if(result.data.success == false) { 
				return;
			} 
			if($scope.editType == "add") {
				$scope.param.collection.collection_id = result.data.item.collection_id;
				$scope.collectionList.push(angular.copy($scope.param.collection));
			}
			if($scope.editType == "edit") {
				$scope.collectionList[$scope.edit_index] = angular.copy($scope.param.collection);
			}
			$scope.collection = {};
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
			$httpService.header('method', 'deleteSalesCollection');
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
	
	$scope.setNewVal =function () {
		//30% DEPOSIT,60% AGAINST B/L COPY,10% 11DAYS AFTER LAST PAYMENT DATE
		//30% DEPOSIT,60% AGAINST B/L COPY,10% 1DAYS AFTER LAST PAYMENT DATE
		$scope.collection.collection_en = $scope.collection.collection_deposit+"%DEPOSIT，"+$scope.collection.collection_before
										+"% AGAINST B/L COPY, "+$scope.collection.collection_after+"% "+$scope.collection.collection_days+" DAYS AFTER LAST PAYMENT DATE";
		//$scope.collection.collection_cn = "";
	}
});