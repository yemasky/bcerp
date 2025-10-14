app.controller('SupplierResearchController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
		$ocLazyLoad.load([__RESOURCE+"vendor/libs/daterangepicker.js?",__RESOURCE+"styles/booking.css",
						__RESOURCE+"editor/kindeditor/kindeditor-all.js?",
					  	__RESOURCE+"editor/kindeditor/themes/default/default.css"]);
		$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];
		var urlParam = __WEB + 'app.do?channel=' + $stateParams.channel;
	$scope.param = {}; $scope.edit_id = 0;$scope.edit_index = 0;$scope.editType = "";
	//定义变量
	$scope.rsupplier = {};
	$scope.rsupplier.rsupplier_contact = {};
	$scope.rsupplier.rsupplier_product = {};
	$scope.rsupplier.vehicle_type = [];
	$scope.rsupplier.rsupplier_plant = {};
	$scope.rsupplier.rsupplier_detection = {};
	$scope.rsupplier.tech_dev = [];
	$scope.rsupplier.rsupplier_upload = {};
	$scope.rsupplierList = {};
	//增加项相关
	$scope.step = {};$scope.tab = 1;
	//$scope.step['p'] = {};$scope.step['p'].step = "0";$scope.step['p'].length = 0;
	let aside;
	$httpService.header('method', 'getResearch');
	$httpService.post(urlParam, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		$scope.rsupplierList = result.data.item.rsupplierList;//
		//$scope.$apply();//刷新数据
	})
	
	$scope.addEdit = function(editType, rsupplier, i) {
		$scope.editType = editType;
		if(editType == 'edit' && typeof(rsupplier) != 'undefined') {
			$scope.rsupplier = rsupplier;
			$scope.edit_id = angular.copy(rsupplier.rsupplier_id);
			$scope.edit_index = i;
			if(typeof(rsupplier.rsupplier_contact) == "object") {
				for (let c in rsupplier.rsupplier_contact) {$scope.addStep('c');}
			}
			if(typeof(rsupplier.rsupplier_product	) == "object") {
				for (let c in rsupplier.rsupplier_product) {$scope.addStep('p');}
			}
			if(typeof(rsupplier.rsupplier_plant) == "object") {
				for (let c in rsupplier.rsupplier_plant) {$scope.addStep('sc');}
			}
			if(typeof(rsupplier.rsupplier_detection) == "object") {
				for (let c in rsupplier.rsupplier_detection) {$scope.addStep('jc');}
			}
			if(typeof(rsupplier.rsupplier_upload) == "object") {
				for (let c in rsupplier.rsupplier_upload) {$scope.addStep('wj');}
			}
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		aside = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',
				backdrop:"static",container:'#MainController', templateUrl: 'AddEditResearch.html'});
		aside.$promise.then(function() {
			aside.show();
			$(document).ready(function(){
				
			});
		})
		//工作流审核相关
		$rootScope.param = {};$rootScope.param.auditing = $scope.rsupplier;$rootScope.param.id = $scope.rsupplier.rsupplier_id;
	}

	$scope.saveData = function() {
		if(this.rsupplier == null || this.rsupplier == '') {
			$alert({title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(this.rsupplier.rsupplier_name)) {
			$alert({title: 'Notice', content: '名称必须填写！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.loading.show();
		$scope.param.rsupplier = angular.copy(this.rsupplier);
		$httpService.header('method', 'saveResearch');
		$httpService.post(urlParam+"&edit_id="+$scope.edit_id, $scope, function(result){
			$scope.loading.percent();
		    $httpService.deleteHeader('method');
			if(result.data.success == false) { 
				return; 
			} 
			
			if($scope.editType == "add") {
				$scope.param.rsupplier.rsupplier_id = result.data.item.rsupplier_id;
				$scope.rsupplierList.push(angular.copy($scope.param.rsupplier));
			}
			if($scope.editType == "edit") {
				$scope.rsupplierList[$scope.edit_index] = angular.copy($scope.param.rsupplier);
			}
			$scope.rsupplier = {};
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
			$httpService.header('method', 'deleteResearch');
			$httpService.post(urlParam, $scope, function(result) {
				$scope.loading.percent();
				$httpService.deleteHeader('method');
				if (result.data.success == false) {
					return;
				}
				delete $scope.rsupplierList[i]; 				
			});
		}
	}
	$scope.addStep = function(key) {
		if(!angular.isDefined($scope.step[key])) {
			$scope.step[key] = {};$scope.step[key].length = 0;$scope.step[key].step = "0";
		} else {
			$scope.step[key].length++;
			$scope.step[key].step=$scope.step[key].step+''+$scope.step[key].length;
		}
		if(key == 'wj') {
			$(document).ready(function(){
				$scope.setUpload();
			});
		}
		console.log($scope.step);
	}
	$scope.reduceStep = function(key) {
		if($scope.step[key].length < 1) {$alert({title: 'Notice', content: '最少需要1个', templateUrl: '/modal-warning.html', show: true});return;}
		$scope.step[key].step=$scope.step[key].step.substring(0,$scope.step[key].step.length-1);
		$scope.step[key].length--;
	}
	//<!--上传及操作-->
	$scope.setUpload = function() {
		var uploadJsonUrl = $scope.__WEB+'index/uploadFile?channel='+__ImagesUploadUrl+"&UseType=rsupplier";
		var fileManagerJsonUrl = $scope.__WEB+'index/fileManager?channel='+__ImagesManagerUrl+"&UseType=rsupplier";;
		window.K = KindEditor;
		var editor = K.editor({
			uploadJson : uploadJsonUrl,fileManagerJson : fileManagerJsonUrl,allowFileManager : true,formatUploadUrl: false
		});
		K('.insertfile').click(function() {
			let _this = this;
			editor.loadPlugin('insertfile', function() {
				editor.plugin.fileDialog({
					//clickFn : function(url, title, width, height, border, align) {
					clickFn : function(url) {
						editor.hideDialog();
						$(_this).src = url;
						let node = $(_this).attr('node');
						if(typeof($scope.rsupplier.rsupplier_upload[node]) == 'undefined') $scope.rsupplier.rsupplier_upload[node] = {};
						$scope.rsupplier.rsupplier_upload[node].file_url = url;
						$scope.$apply();//刷新数据
					}
				});
			});
		});
	} 
});