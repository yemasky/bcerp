app.controller('ProductController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
		$ocLazyLoad.load([__RESOURCE + "vendor/modules/angular-ui-select/select.min.js", __RESOURCE + "vendor/modules/angular-ui-select/select.min.css",
						  __RESOURCE+"editor/kindeditor/kindeditor-all.js",__RESOURCE+"editor/kindeditor/themes/default/default.css"]);
		$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];
		var urlParam = __WEB + 'app.do?channel=' + $stateParams.channel;
	$scope.param = {}; $scope.edit_id = 0;$scope.edit_index = 0;$scope.editType = "";
	//定义变量
	let channel_id = $stateParams.id;
	$scope.dict_product_pause_reason =  $scope.commDictHash[channel_id]['product_pause_reason'];
	$scope.dict_product_dev_type =  $scope.commDictHash[$stateParams.id]['product_dev_type'];
	$scope.dict_product_dev_from =  $scope.commDictHash[$stateParams.id]['product_dev_from'];
	$scope.dict_product_factory =  $scope.commDictHash[$stateParams.id]['product_factory'];
	$scope.dict_factory_name =  $scope.commDictHash[$stateParams.id]['factory_name'];
	$scope.productList = [];$scope.product = {};$scope.productAttrList = {};$scope.productVehicle = [];$scope.images = {};
	$scope.systypeList = [];$scope.systypeHash = {};//系统分类
	$scope.commodityList = [];$scope.commodity = {};$scope.commodityAttrList = {};
	$scope.tab = 1;
	$scope.classifyList = [];$scope.classifyHash = {};$scope.categoryHash = {};
	$scope.categoryList = [];$scope.vehicleModelList = [];
	//
	$httpService.header('method', 'getProduct');
	$httpService.post(urlParam, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		$scope.productList = result.data.item.productList;//
		$scope.systypeList = result.data.item.systypeList;//
		for (var key in $scope.systypeList) {$scope.systypeHash[$scope.systypeList[key].systype_id] = $scope.systypeList[key];}
		$scope.categoryList = result.data.item.categoryList;//
		for (var key in $scope.categoryList) {$scope.categoryHash[$scope.categoryList[key].category_id] = $scope.categoryList[key];}
		//$scope.$apply();//刷新数据
	})
	let asideAddEdit = {};
	$scope.addEdit = function(editType, product, i) {
		$scope.editType = editType;
		if(editType == 'edit' && typeof(product) != 'undefined') {
			$scope.product = product;
			$scope.edit_id = angular.copy(product.product_id);
			$scope.edit_index = i;
		}
		$scope.setActionNavName($stateParams.id, "添加/编辑");
		$scope.action = '添加/编辑';
		asideAddEdit = $aside({scope : $scope, title: $scope.action_nav_name, placement:'center',animation:'am-fade-and-slide-top',
				backdrop:"static",container:'#MainController', templateUrl: 'AddEditProduct.html'});
		asideAddEdit.$promise.then(function() {
			asideAddEdit.show();
			$(document).ready(function(){
				
			});
		})
		//工作流审核相关
		$rootScope.param = {};$rootScope.param.auditing = $scope.product;$rootScope.param.id = 0;//$scope.product.product_id;
	}

	$scope.saveData = function() {
		if(this.product == null || this.product == '') {
			$alert({title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(this.product.commodity_id)) {
			$alert({title: 'Notice', content: '品名必须选择！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.loading.show();
		$scope.param.product = angular.copy(this.product);
		$scope.param.product.systype_id = angular.copy(this.product.systype_id.systype_id);
		$scope.param.productAttr = $scope.productAttrList;
		$scope.param.images = $scope.images;
		$scope.param.oem = $scope.oem;
		$scope.param.factoryNum = $scope.factoryNum;
		$scope.param.engineNum = $scope.engineNum;
		$scope.param.pfactory = $scope.pfactory;
		$httpService.header('method', 'saveProduct');
		$httpService.post(urlParam+"&edit_id="+$scope.edit_id, $scope, function(result){
			$scope.loading.percent();
			console.log($scope.param);
		    $httpService.deleteHeader('method');
			if(result.data.success == false) { 
				return; 
			} 
			
			if($scope.editType == "add") {
				$scope.param.product.product_id = result.data.item.product_id;
				$scope.productList.push(angular.copy($scope.param.product));
			}
			if($scope.editType == "edit") {
				$scope.productList[$scope.edit_index] = angular.copy($scope.param.product);
			}
			$scope.product = {};
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
			$httpService.header('method', 'deleteProduct');
			$httpService.post(urlParam, $scope, function(result) {
				$scope.loading.percent();
				$httpService.deleteHeader('method');
				if (result.data.success == false) {
					return;
				}
				delete $scope.productList[i]; 				
			});
		}
	}
	//
	$scope.selectCommodity = function() {
		if($scope.commodityList.length == 0) {
			$httpService.header('method', 'getCommodity');
			$httpService.post(urlParam, $scope, function(result){
				$scope.loading.hide();
				$httpService.deleteHeader('method'); 
				if(result.data.success == false) {
					return;
				} 
				$scope.commodityList = result.data.item.commodityList;//
				//$scope.$apply();//刷新数据
				$(document).ready(function() {
					$scope.showCommodity();
				});
			})
		} else {
			$scope.showCommodity();
		}
		
	}
	let asideSector = {};
	$scope.showCommodity = function() {
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
	$scope.choiceCommodity = function(commodity) {
		$scope.commodity = commodity;
		$scope.product.commodity_id = commodity.commodity_id;
		asideSector.hide(); 
		if(typeof($scope.commodityAttrList[commodity.commodity_id]) == 'undefined') {
			$httpService.header('method', 'getCommodityAttr');
			$httpService.post(urlParam+'&commodity_id='+commodity.commodity_id, $scope, function(result){
				$scope.loading.hide();
				$httpService.deleteHeader('method'); 
				if(result.data.success == false) {
					return;
				} 
				let commodityAttr = result.data.item.commodityAttrList;
				let commodityAttrList = {};
				commodityAttr.forEach(commodity=>{
					if(commodity.attribute_type == 'images') {
						if(typeof commodity.attribute_key === 'number') {
							if(!angular.isDefined($scope.images['img'])) $scope.images['img'] = [];
							$scope.images['img'].push(commodity);
						} else {
							$scope.images[commodity.attribute_key] = commodity;
						}
					} else {
						if(!angular.isDefined(commodityAttrList[commodity.attribute_type])) commodityAttrList[commodity.attribute_type] = [];
						commodityAttrList[commodity.attribute_type].push(commodity);
					}
				}) 
				$scope.commodityAttrList[commodity.commodity_id] = commodityAttrList;//
				console.log($scope.commodityAttrList);
			})
		}
	}
	//selectClassify ////////////////////////////
	$scope.classifyI = 0;
	$scope.selectClassify = function(i) {
		$scope.classifyI = i;
		if($scope.classifyList.length == 0) {
			$httpService.header('method', 'getClassify');
			$httpService.post(urlParam, $scope, function(result){
				$scope.loading.hide();
				$httpService.deleteHeader('method'); 
				if(result.data.success == false) {
					return;
				} 
				$scope.classifyList = result.data.item.classifyList;//
				//$scope.$apply();//刷新数据
				$(document).ready(function() {
					$scope.showClassify();
				});
			})
		} else {
			$scope.showClassify();
		}
		
	}
	let asideClassify = {};
	$scope.showClassify = function() {
		asideClassify = $aside({scope: $scope,title: $scope.action_nav_name,placement: 'center',animation: 'am-fade-and-slide-top',backdrop: "static",
			container: '#MainController',templateUrl: 'SelectClassify.html'
		});
		asideClassify.$promise.then(function() {
			asideClassify.show();
			$(document).ready(function() {
			});
		})
	}
	$scope.productClassify = {};
	$scope.choiceClassify = function(classify) {
		console.log(classify);
		asideClassify.hide(); 
		if(!angular.isDefined($scope.productClassify[$scope.classifyI])) 
			$scope.productClassify[$scope.classifyI] = {};
		$scope.productClassify[$scope.classifyI].classify = classify;
	}
	//selectVehicleModel ////////////////////////////
	$scope.selectVehicleModel = function() {
		if($scope.vehicleModelList.length == 0) {
			$httpService.header('method', 'getVehicleModel');
			$httpService.post(urlParam, $scope, function(result){
				$scope.loading.hide();
				$httpService.deleteHeader('method'); 
				if(result.data.success == false) {
					return;
				} 
				$scope.vehicleModelList = result.data.item.vehicleModelList;//
				$scope.classifyList = result.data.item.classifyList;//
				$scope.classifyList.forEach(item => {
					$scope.classifyHash[item.classify_id] = item;
				})
				console.log($scope.classifyHash);
				//$scope.$apply();//刷新数据
				$(document).ready(function() {
					$scope.showVehicleModel();
				});
			})
		} else {
			$scope.showVehicleModel();
		}
		
	}
	let asideVehicleModel = {};
	$scope.showVehicleModel = function() {
		asideVehicleModel = $aside({scope: $scope,title: $scope.action_nav_name,placement: 'center',animation: 'am-fade-and-slide-top',backdrop: "static",
			container: '#MainController',templateUrl: 'SelectVehicleModel.html'
		});
		asideVehicleModel.$promise.then(function() {
			asideVehicleModel.show();
			$(document).ready(function() {
			});
		})
	}
	$scope.choiceVehicle = {};$scope.checkVehicle = {};
	$scope.choiceVehicleModel  = function(vehicle) {
		$scope.choiceVehicle[vehicle.vehicle_model_id] = vehicle;
		//console.log(this);
	}
	$scope.oem = {};$scope.param.classify = {};
	$scope.choiceVehicleOk = function() {
		$scope.productVehicle = {};
		asideVehicleModel.hide();
		for(id in $scope.choiceVehicle) {
			let choice = angular.copy($scope.choiceVehicle[id]);
			if($scope.checkVehicle[id]) {
				if(!angular.isDefined($scope.productVehicle[choice.classify_id])) {
					$scope.productVehicle[choice.classify_id] = {};
					$scope.productVehicle[choice.classify_id].classify_id = choice.classify_id;
					$scope.productVehicle[choice.classify_id].childen = [];
					$scope.param.classify[choice.classify_id] = {};
					$scope.param.classify[choice.classify_id].classify_id = choice.classify_id;
					$scope.param.classify[choice.classify_id].vehicle_model_ids = [];
				}
				$scope.productVehicle[choice.classify_id].childen.push(choice);	
				$scope.param.classify[choice.classify_id].vehicle_model_ids.push(choice.vehicle_model_id);
				$scope.oem[choice.classify_id] = [];
			} else {
				delete $scope.choiceVehicle[id];
			}
		}
		console.log($scope.productVehicle);
	}
	////SelectVehicleOEM.html
	let asideVehicleOEM = {};
	$scope.oemI = 0;
	$scope.showVehicleOEM = function(i) {
		$scope.oemI = i;
		asideVehicleOEM = $aside({scope: $scope,title: $scope.action_nav_name,placement: 'center',animation: 'am-fade-and-slide-top',backdrop: "static",
			container: '#MainController',templateUrl: 'SelectVehicleOEM.html'
		});
		asideVehicleOEM.$promise.then(function() {
			asideVehicleOEM.show();
			$(document).ready(function() {
			});
		})
	}
	$scope.oemOk = function(i) { 
		asideVehicleOEM.hide();
		let setp_length = $scope.step['oem'+i].length;
		let oem_length = Object.keys($scope.oem[i]).length;
		console.log(setp_length, oem_length);
		if(oem_length > setp_length) {
			let cha = oem_length - setp_length;
			for(j=0;j<cha;j++) {
				//delete $scope.oem[i][setp_length + j];
				$scope.oem[i].pop();
			}
		}
		console.log($scope.oem, $scope.step['oem'+i])
	}
	//
	let asideFactoryNum = {};
	$scope.factoryNum = [];
	$scope.ftI = 0;
	$scope.showFactoryNum = function(i) {
		$scope.ftI = i;
		asideFactoryNum = $aside({scope: $scope,title: $scope.action_nav_name,placement: 'center',animation: 'am-fade-and-slide-top',backdrop: "static",
			container: '#MainController',templateUrl: 'SelectFactoryNum.html'
		});
		asideFactoryNum.$promise.then(function() {
			asideFactoryNum.show();
			$(document).ready(function() {
			});
		})
	}
	$scope.factoryNumOk = function(i) {
		asideFactoryNum.hide();
		let setp_length = $scope.step['ft'+i].length;
		let fn_length = Object.keys($scope.factoryNum[i]).length;
		if(fn_length > setp_length) {
			let cha = fn_length - setp_length;
			for(j=0;j<cha;j++) {
				delete $scope.factoryNum[i][setp_length + j];
			}
		}
		console.log(setp_length+"==="+i+"===="+fn_length,$scope.factoryNum,$scope.step['ft'+i]);
	}
	//
	let asideEngineNum = {};
	$scope.enI = 0;
	$scope.engineNum = {};
	$scope.showEngineNum = function(i) {
		$scope.enI = i;
		asideEngineNum = $aside({scope: $scope,title: $scope.action_nav_name,placement: 'center',animation: 'am-fade-and-slide-top',backdrop: "static",
			container: '#MainController',templateUrl: 'SelectEngineNum.html'
		});
		asideEngineNum.$promise.then(function() {
			asideEngineNum.show();
			$(document).ready(function() {
			});
		})
	}
	$scope.engineNumOk = function(i) {
		asideEngineNum.hide();
		let setp_length = $scope.step['en'+i].length;
		let en_length = Object.keys($scope.engineNum[i]).length;
		if(en_length > setp_length) {
			let cha = en_length - setp_length;
			for(j=0;j<cha;j++) {
				delete $scope.engineNum[i][setp_length + j];
			}
		}
		console.log($scope.engineNum,$scope.step['en'+i]);
	}
	//
	$scope.pre_product_pause_reason = "";
	$scope.reasonHash = [];
	$scope.selectPauseReason = function() {
		if($scope.reasonHash == '') {
			let reason = $scope.dict_product_pause_reason;
			for(i in reason) {
				$scope.reasonHash[reason[i].dict_id] = reason[i];
			}
		}
		if(!angular.isDefined($scope.product.product_pause_reason)) {
			$scope.product.product_pause_reason = $scope.reasonHash[$scope.product.prereason].dict_val;
		} else {
			$scope.product.product_pause_reason += $scope.reasonHash[$scope.product.prereason].dict_val;
		}
	}
	//$scope.dict_product_dev_from
	$scope.dev_from = {};
	$scope.changeDefFrom = function() {
		//console.log($scope.product);
		let type = $scope.product.product_dev_type;
		if(angular.isDefined(type)) {
			let from = $scope.dictValHash[type].dict_val;
			if(from == "我司集样" || from == "我司收集产品信息") {
				if(!angular.isDefined($scope.dev_from[type])) {
					$scope.dev_from[type] = [];
					for(i in $scope.dict_product_dev_from) {
						let linkage = $scope.dict_product_dev_from[i].dict_linkage;
						if(linkage == from) {
							$scope.dev_from[type].push($scope.dict_product_dev_from[i]);
						}
					}
				}
			}
		}
	}
	$scope.dictFactory = {};
	$scope.pfactory = {};
	//$scope.pfactory.dict_id = {};
	//$scope.pfactory.factory_name = {};
	$scope.changeDictFactory = function(i) {
		//console.log($scope.pfactory.dict_id);
		let dict_id = $scope.pfactory[i].dict_id;
		if(angular.isDefined(dict_id)) {
			let from = $scope.dictValHash[dict_id].dict_val;
			if(from == "厂商1" || from == "厂商2" || from == "厂商3") {
				if(!angular.isDefined($scope.dictFactory[dict_id])) {
					$scope.dictFactory[dict_id] = [];
					for(i in $scope.dict_factory_name) {
						let linkage = $scope.dict_factory_name[i].dict_linkage;
						if(linkage == from) {
							$scope.dictFactory[dict_id].push($scope.dict_factory_name[i]);
						}
					}
				}
			}
		} 
	}
	$scope.setFactoryName = function(i) {
		let dict_id = $scope.pfactory[i].factory_dict_id;
		let name = $scope.dictValHash[dict_id].dict_val;
		$scope.pfactory[i].factory_name = name;
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
	$scope.step = {};
	$scope.addStep = function(key) {
		if(!angular.isDefined($scope.step[key])) {
			$scope.step[key] = [];
		} 
		$scope.step[key].push({"id":"0"});
		console.log($scope.step);
	}
	$scope.reduceStep = function(key) {
		if($scope.step[key].length < 1) {$alert({title: 'Notice', content: '最少需要1个', templateUrl: '/modal-warning.html', show: true});return;}
		$scope.step[key].pop();console.log($scope.step);
	}
});
