app.controller('ProductController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
		$ocLazyLoad.load([__RESOURCE + "vendor/modules/angular-ui-select/select.min.js?" + __VERSION,
						  __RESOURCE + "vendor/modules/angular-ui-select/select.min.css?" + __VERSION,
						  __RESOURCE+"editor/kindeditor/kindeditor-all.js?"+__VERSION,__RESOURCE+"editor/kindeditor/themes/default/default.css"]);
		$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];
		var urlParam = __WEB + 'app.do?channel=' + $stateParams.channel;
	$scope.param = {}; $scope.edit_id = 0;$scope.edit_index = 0;$scope.editType = "";
	//定义变量
	$scope.box = {};$scope.boxList = {};
	$scope.systypeList = [];$scope.systypeHash = {};//系统分类
	$scope.commodityList = [];
	let aside;
	$httpService.header('method', 'getBox');
	$httpService.post(urlParam, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		$scope.boxList = result.data.item.boxList;//
		$scope.systypeList = result.data.item.systypeList;//
		for (var key in $scope.systypeList) {$scope.systypeHash[$scope.systypeList[key].systype_id] = $scope.systypeList[key];}
		$scope.categoryList = result.data.item.categoryList;//
		//$scope.$apply();//刷新数据
	})
	
	$scope.addEdit = function(editType, box, i) {
		$scope.editType = editType;
		if(editType == 'edit' && typeof(box) != 'undefined') {
			$scope.box = box;
			$scope.edit_id = angular.copy(box.box_id);
			$scope.edit_index = i;
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		aside = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',
				backdrop:"static",container:'#MainController', templateUrl: 'AddEditProduct.html'});
		aside.$promise.then(function() {
			aside.show();
			$(document).ready(function(){
				
			});
		})
		//工作流审核相关
		$rootScope.param = {};$rootScope.param.auditing = $scope.box;$rootScope.param.id = 0;//$scope.box.box_id;
	}

	$scope.saveData = function() {
		if(this.box == null || this.box == '') {
			$alert({title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(this.box.box_name)) {
			$alert({title: 'Notice', content: '名称必须填写！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.loading.show();
		$scope.param.box = angular.copy(this.box);
		$httpService.header('method', 'saveBox');
		$httpService.post(urlParam+"&edit_id="+$scope.edit_id, $scope, function(result){
			$scope.loading.percent();
		    $httpService.deleteHeader('method');
			if(result.data.success == false) { 
				return; 
			} 
			
			if($scope.editType == "add") {
				$scope.param.box.box_id = result.data.item.box_id;
				$scope.boxList.push(angular.copy($scope.param.box));
			}
			if($scope.editType == "edit") {
				$scope.boxList[$scope.edit_index] = angular.copy($scope.param.box);
			}
			$scope.box = {};
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
			$httpService.header('method', 'deleteBox');
			$httpService.post(urlParam, $scope, function(result) {
				$scope.loading.percent();
				$httpService.deleteHeader('method');
				if (result.data.success == false) {
					return;
				}
				delete $scope.boxList[i]; 				
			});
		}
	}
	$scope.selectCommodity = function() {
		asideSector = $aside({scope: $scope,title: $scope.action_nav_name,placement: 'center',animation: 'am-fade-and-slide-top',backdrop: "static",
			container: '#MainController',templateUrl: 'SelectCommodity.html'
		});
		asideSector.$promise.then(function() {
			asideSector.show();
			$(document).ready(function() {
				$scope.setImage();
			});
		})
	}
	//<!--图片上传及操作-->
	//images
	$scope.setImage = function() {
		//$scope.__WEB+'app.do?method=uploadCompanyLogo&channel='+$rootScope.__ImagesUploadUrl;
		var uploadJsonUrl = $scope.__WEB+'index/uploadCompanyLogo?channel='+__ImagesUploadUrl+"&UseType=CompanyLogo";
		var fileManagerJsonUrl = $scope.__WEB+'index/fileManager?channel='+__ImagesManagerUrl;
		window.K = KindEditor;
		var editor = K.editor({
			uploadJson : uploadJsonUrl,fileManagerJson : fileManagerJsonUrl,allowFileManager : true,formatUploadUrl: false
		});
		let keditor = K('.set_image_src');
		keditor.click(function() {
			let _this = this;
			editor.loadPlugin('image', function() {
				editor.plugin.imageDialog({
					//clickFn : function(url, title, width, height, border, align) {
					clickFn : function(url) {
						editor.hideDialog();
						$(_this).src = url;
						let node = $(_this).attr('node');
						if(typeof($scope.images[node]) == 'undefined') $scope.images[node] = {};
						$scope.images[node].attribute_val = url;
						$scope.$apply();//刷新数据
					}
				});
			});
		});
	} 
});