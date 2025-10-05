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
	$scope.step = {};$scope.step['spare'] = {};$scope.step['attr'] = {};$scope.step['images'] = {}; $scope.step['hs'] = {};
	$scope.step['spare'].step = "0";$scope.step['spare'].length = 0;
	$scope.step['attr'].step = "0";$scope.step['attr'].length = 0;
	$scope.step['images'].step = "0";$scope.step['images'].length = 0;
	$scope.step['hs'].step = "0";$scope.step['hs'].length = 0;
	///////////////////////
	$scope.images = {};$scope.attribute = {};$scope.hs = {};
	$scope.systypeList = [];$scope.systypeHash = {};//系统分类
	$scope.commodityPage = {};$scope.unitList = {};$scope.unitHash = {};
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
		$scope.unitHash = {};$scope.unitList = result.data.item.unitList;
		for (var key in $scope.unitList) {$scope.unitHash[$scope.unitList[key].unit_id] = $scope.unitList[key];}
		//page列表
		$scope.commodityPage = result.data.item.commodityPage;
	})
	
	$scope.addEdit = function(editType, commodity, i) {
		$scope.editType = editType;
		$scope.commodity = {};
		if(editType == 'edit' && typeof(commodity) != 'undefined') {
			$scope.commodity = angular.copy(commodity);//编辑开始
			$scope.commodity.systype_id = angular.copy($scope.systypeHash[$scope.commodity.systype_id]);
			$scope.commodity.unit_id = angular.copy($scope.unitHash[$scope.commodity.unit_id]);
			if(typeof(commodity.commodity_spare) == "object") {
				$scope.step['spare'].step = "";$scope.step['spare'].length = 0;
				for (let si in commodity.commodity_spare) {
					$scope.step['spare'].step = $scope.step['spare'].step + "" + $scope.step['spare'].length;
					$scope.step['spare'].length++;
				}
			}
			$httpService.header('method', 'getAttribute');
			$httpService.post(urlParam + '&attr_id='+commodity.commodity_id, $scope, function(result){
				$scope.loading.hide();
				$httpService.deleteHeader('method'); 
				if(result.data.success == false) {return;} 
				let attrList = result.data.item.attributeList;
				$scope.step['attr'].step = "";$scope.step['attr'].length = 0;$scope.attribute = [];
				$scope.step['hs'].step = "";$scope.step['hs'].length = 0;$scope.hs = [];
				$scope.step['images'].step = "";$scope.step['images'].length = 0;$scope.images = {};
				for (var i in attrList) {
					let attr = attrList[i];
					if(attr.attribute_type=='attr') {
						attr.attribute_check = true;
						if(attr.attribute_check == 0) attr.attribute_check = false;
						$scope.attribute.push(attr);
						$scope.step[attr.attribute_type].step = $scope.step[attr.attribute_type].step + "" + $scope.step[attr.attribute_type].length;
						$scope.step[attr.attribute_type].length++;
					}
					if(attr.attribute_type=='hs') {
						$scope.hs.push(attr);
						$scope.step[attr.attribute_type].step = $scope.step[attr.attribute_type].step + "" + $scope.step[attr.attribute_type].length;
						$scope.step[attr.attribute_type].length++;
					}
					if(attr.attribute_type=='images') {
						$scope.images[attr.attribute_key] = attr;
						$scope.step[attr.attribute_type].step = $scope.step[attr.attribute_type].step + "" + $scope.step[attr.attribute_type].length;
						$scope.step[attr.attribute_type].length++;
					}
				}
				console.log($scope.step,$scope.images,$scope.attribute,$scope.hs);
				//$scope.$apply();//刷新数据
			});
			//图片
			//let images = 
			$scope.edit_id = angular.copy(commodity.commodity_id);
			$scope.edit_index = i;
			//工作流审核相关
			$rootScope.param = {};$rootScope.param.auditing = $scope.commodity;$rootScope.param.id = $scope.commodity.commodity_id;
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
		if(typeof(this.commodity.unit_id) == 'object')
			$scope.param.commodity.unit_id = angular.copy(this.commodity.unit_id.unit_id);
		$httpService.header('method', 'saveCommodity');
		$httpService.post(urlParam+"&edit_id="+$scope.edit_id, $scope, function(result){
			$scope.loading.percent();
		    $httpService.deleteHeader('method');
			if(result.data.success == false) { 
				return;
			} 
			if($scope.editType == "add") {
				$scope.param.commodity.commodity_id = result.data.item.commodity_id;
				$scope.commodityPage.pageList.push(angular.copy($scope.param.commodity));
			}
			if($scope.editType == "edit") {
				$scope.commodityPage.pageList[$scope.edit_index] = angular.copy($scope.param.commodity);
			}
			$scope.commodity = {};
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
				delete $scope.commodityPage.pageList[i]; 				
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
	$scope.selectUnit = function() {
		console.log($scope.commodity)
	}
	$scope.addStep = function(key) {
		//if($scope.step_length > 4) {$alert({title: 'Notice', content: '最多允许6个', templateUrl: '/modal-warning.html', show: true});return;}
		$scope.step[key].length++;
		$scope.step[key].step=$scope.step[key].step+''+$scope.step[key].length;
		if(key == 'images') {
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
	$scope.nextPage = function(page) {	
		$scope.param.page = page;
		$httpService.header('method', 'getCommodity');
		$httpService.post(urlParam, $scope, function(result) {
			$scope.loading.percent();
			$httpService.deleteHeader('method');
			if (result.data.success == false) {
				return;
			}
			$scope.commodityPage = result.data.item.commodityPage;		
		});
	}
});