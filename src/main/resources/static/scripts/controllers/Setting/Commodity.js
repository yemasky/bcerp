app.controller('CommodityController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
		$ocLazyLoad.load([__RESOURCE + "vendor/modules/angular-ui-select/select.min.js?" + __VERSION,
		__RESOURCE + "vendor/modules/angular-ui-select/select.min.css?" + __VERSION,
		__RESOURCE+"editor/kindeditor/kindeditor-all.js?"+__VERSION,__RESOURCE+"editor/kindeditor/themes/default/default.css"]);
		$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];$scope.__IMGWEB = __IMGWEB;
		var urlParam = __WEB + 'app.do?channel=' + $stateParams.channel;
	$scope.param = {}; $scope.edit_id = "";$scope.commodity = {};
	$scope.commodity_name_sources = [{'id':'1','name':'知名经销商'},{'id':'2','name':'专业书籍'},{'id':'3','name':'网络'}];
	//定义变量
	$scope.step = {};$scope.step['spare'] = {};$scope.step['desc'] = {};$scope.step['pic'] = {}; $scope.step['hs'] = {};
	$scope.step['spare'].step = "0";$scope.step['spare'].length = 0;
	$scope.step['desc'].step = "0";$scope.step['desc'].length = 0;
	$scope.step['pic'].step = "0";$scope.step['pic'].length = 0;
	$scope.step['hs'].step = "0";$scope.step['hs'].length = 0;
	$scope.images = {};$scope.attribute = {};$scope.hs = {};
	$scope.systypeList = [];$scope.systypeHash = {};//系统分类
	let aside;
	$httpService.header('method', 'getCommodity');
	$httpService.post(urlParam, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		$scope.systypeList = result.data.item.systypeList;//
		for (var key in $scope.systypeList) {$scope.systypeHash[$scope.systypeList[key].systype_id] = $scope.systypeList[key];}
	})
	
	$scope.addEdit = function(editType, auditing, i) {
		$scope.editType = editType;
		$scope.auditing = {};
		if(editType == 'edit' && typeof(auditing) != 'undefined') {
			$scope.auditing = angular.copy(auditing);//编辑开始
			$scope.auditing.module_id = angular.copy($scope.auditingModuleHash[auditing.module_id]);//模块
			for(j in $scope.auditing.examine) {//解析examine
				let examine = $scope.auditing.examine[j];
				let position_id =  examine.position_id;
				let sector_id = examine.sector_id;
				$scope.auditing.examine[j].position_id = angular.copy($scope.sectorHash[position_id]);
				if(typeof($scope.auditingEmployeeList[i]) == "undefined") {
					$scope.auditingEmployeeList[i] = [];
				} else {
					$scope.auditingEmployeeList[i] = [];//清空之前的数据
				}
				for(e in $scope.employeeList) {
					let employee = $scope.employeeList[e];
					if(employee.position_id == position_id && employee.sector_id == sector_id) 
						$scope.auditingEmployeeList[i].push(employee);
				}
				$scope.auditing.examine[j].employee_id = angular.copy($scope.employeeHash[examine.employee_id]);
			}
			$scope.edit_id = angular.copy(auditing.auditing_id);
			$scope.edit_index = i;
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		aside = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',
				backdrop:"static",container:'#MainController', templateUrl: __RESOURCE + 'views/Setting/CommodityAddEditAside.html?'+__VERSION});
		aside.$promise.then(function() {
			aside.show();
			$(document).ready(function(){
				$scope.setImage();
			});
		})
	}

	$scope.saveData = function() {
		console.log(this.commodity, $scope.commodity);
		if(this.commodity == null || this.commodity == '') {$alert({title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true});return;}
		if(!angular.isDefined(this.commodity.commodity_name)) {$alert({title: 'Notice', content: '名称必须填写！', templateUrl: '/modal-warning.html', show: true});return;}
		$scope.loading.show();
		$scope.param.commodity = angular.copy(this.commodity);
		$scope.param.images = angular.copy(this.images);
		$scope.param.attribute = angular.copy(this.attribute);
		$scope.param.hs = angular.copy(this.hs);
		$scope.param.commodity.systype_id = angular.copy(this.commodity.systype_id.systype_id);
		$httpService.header('method', 'saveCommodity');
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
			$httpService.header('method', 'deleteCommodity');
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
	$scope.selectSystype = function() {
		console.log($scope.commodity)
	}
	$scope.addStep = function(key) {
		//if($scope.step_length > 4) {$alert({title: 'Notice', content: '最多允许6个', templateUrl: '/modal-warning.html', show: true});return;}
		$scope.step[key].length++;
		$scope.step[key].step=$scope.step[key].step+''+$scope.step[key].length;
		if(key == 'pic') {
			$(document).ready(function(){
				$scope.setImage();
			});
		}
	}
	$scope.reduceStep = function(key) {
		if($scope.step[key].length < 1) {$alert({title: 'Notice', content: '最少需要1个', templateUrl: '/modal-warning.html', show: true});return;}
		$scope.step[key].step=$scope.step[key].step.substring(0,$scope.step[key].step.length-1);
		$scope.step[key].length--;
	}
});