app.controller('SupplierController', function($rootScope, $scope, $httpService, $location, $translate, $aside, 
	$ocLazyLoad, $alert, $stateParams) {
		$ocLazyLoad.load([__RESOURCE+"vendor/libs/daterangepicker.js",__RESOURCE+"vendor/libs/papaparse.min.js",__RESOURCE+"styles/booking.css",
						__RESOURCE+"editor/kindeditor/kindeditor-all.js",
					  	__RESOURCE+"editor/kindeditor/themes/default/default.css"]);
		$rootScope._self_module = $scope.hashEmployeeModule[$stateParams.id];
		var urlParam = __WEB + 'app.do?channel=' + $stateParams.channel;
	$scope.param = {}; $scope.edit_id = 0;$scope.edit_index = 0;$scope.editType = "";
	let channel_id = $stateParams.id;
	//定义变量
	$scope.supplier = {};
	$scope.supplier.supplier_contact = {};$scope.supplier.supplier_lawsuit = {};
	$scope.supplier.supplier_payment = {};$scope.supplier.supplier_bank = {};$scope.supplier.supplier_upload = {};
	$scope.supplierList = {};
	$scope.supplier_file_types = [];
	$scope.checkboxData = {};
	$scope.dict_engine = $scope.commDictHash[channel_id]['engine'];$scope.dict_crafting_type = $scope.commDictHash[channel_id]['crafting_type'];
	$scope.dict_operate_market = $scope.commDictHash[channel_id]['operate_market'];$scope.dict_certification_system = $scope.commDictHash[channel_id]['certification_system'];
	//增加项相关
	$scope.step = {};$scope.tab = 1;
	let aside;
	$httpService.header('method', 'getSupplier');
	$httpService.post(urlParam, $scope, function(result){
		$scope.loading.hide();
		$httpService.deleteHeader('method'); 
		if(result.data.success == false) {
			return;
		} 
		$scope.supplierList = result.data.item.supplierList;//
		//$scope.$apply();//刷新数据
	})
	
	$scope.addEdit = function(editType, supplier, i) {
		$scope.editType = editType;
		if(editType == 'edit' && typeof(supplier) != 'undefined') {
			$scope.supplier = supplier;
			$scope.edit_id = angular.copy(supplier.supplier_id);
			$scope.edit_index = i;
			if(typeof(supplier.supplier_contact) == "object") {
				for (let c in supplier.supplier_contact) {$scope.addStep('c');}
			}
			if(typeof(supplier.supplier_upload) == "object") {
				for (let c in supplier.supplier_upload) {$scope.addStep('wj');}
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
		if($scope.supplier_file_types.length == 0) {
			Papa.parse(__RESOURCE+"file/SupplierFiles.csv", {
				delimiter: ',',
				download: true,
				header: false,
				worker: true, // 使用Worker线程
				step: function(row) {
					//console.log("Row:", row.data); // 每次处理一行数据
				},
				complete: function() {
					
				}
			});
		}
		//工作流审核相关
		$rootScope.param = {};$rootScope.param.auditing = $scope.supplier;$rootScope.param.id = $scope.supplier.supplier_id;
	}

	$scope.saveData = function() {
		if(this.supplier == null || this.supplier == '') {
			$alert({title: 'Notice', content: '没有数据保存！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		if(!angular.isDefined(this.supplier.supplier_name)) {
			$alert({title: 'Notice', content: '名称必须填写！', templateUrl: '/modal-warning.html', show: true});
			return;
		}
		$scope.loading.show();
		$scope.param.supplier = angular.copy(this.supplier);
		$scope.param.supplier_business = angular.copy($scope.supplier_business);
		$scope.param.images = angular.copy($scope.images);
		$scope.param.supplier_upload = angular.copy($scope.supplier.supplier_upload);
		$httpService.header('method', 'saveSupplier');
		$httpService.post(urlParam+"&edit_id="+$scope.edit_id, $scope, function(result){
			$scope.loading.percent();
		    $httpService.deleteHeader('method');
			if(result.data.success == false) { 
				return; 
			} 
			
			if($scope.editType == "add") {
				$scope.param.supplier.supplier_id = result.data.item.supplier_id;
				$scope.supplierList.push(angular.copy($scope.param.supplier));
			}
			if($scope.editType == "edit") {
				$scope.supplierList[$scope.edit_index] = angular.copy($scope.param.supplier);
			}
			$scope.supplier = {};
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
			$httpService.header('method', 'deleteSupplier');
			$httpService.post(urlParam, $scope, function(result) {
				$scope.loading.percent();
				$httpService.deleteHeader('method');
				if (result.data.success == false) {
					return;
				}
				delete $scope.supplierList[i]; 				
			});
		}
	}
	//////////////////////选择付款方式
	let asidePayment = {};
	$scope.paymentList = [];$scope.checkPayment = {};
	$scope.selectPayment = function() {
		if($scope.paymentList.length == 0) {
			$httpService.header('method', 'getPayment');
			$httpService.post(urlParam, $scope, function(result){
				$scope.loading.hide();
				$httpService.deleteHeader('method'); 
				if(result.data.success == false) {
					return;
				} 
				$scope.paymentList = result.data.item.paymentList;//
				//$scope.$apply();//刷新数据
				$(document).ready(function() {
					$scope.showPayment();
				});
			})
		} else {
			$scope.showPayment();
		}
	}
	$scope.showPayment = function() {
		asidePayment = $aside({scope: $scope,title: $scope.action_nav_name,placement: 'center',animation: 'am-fade-and-slide-top',backdrop: "static",
			container: '#MainController',templateUrl: 'SelectPayment.html'
		});
		asidePayment.$promise.then(function() {
			asidePayment.show();
			$(document).ready(function() {
			});
		})
	}
	$scope.choicePayment = function(payment) {
		let payment_id = payment.payment_id;
		if($scope.checkPayment[payment_id]) {
			$scope.supplier.supplier_payment[payment.payment_id] = payment;
		} else {
			delete $scope.supplier.supplier_payment[payment.payment_id];
		}
	}
	//////////经营于业务信息
	//////////////////////选择品名
	$scope.supplier_business = {};$scope.supplier_business['commodity'] = {};$scope.supplier_business['category'] = {};
	$scope.supplier_business['classify'] = {};$scope.supplier_business['engine'] = {};$scope.supplier_business['crafting_type'] = {};
	$scope.supplier_business['operate_market'] = {};$scope.supplier_business['country'] = {};$scope.supplier_business['certification_system'] = {};
	$scope.asideType = "";
	$scope.commodityList = [];$scope.systypeList = {};$scope.systypeHash = {};
	$scope.categoryList = [];$scope.classifyList = [];
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
				$scope.systypeList = result.data.item.systypeList;//
				for(let i=0; i<$scope.systypeList.length; i++) {
					$scope.systypeHash[$scope.systypeList[i].systype_id] = $scope.systypeList[i];
				}
				//$scope.$apply();//刷新数据
				$(document).ready(function() {
					$scope.showBussiness('commodity');
				});
			})
		} else {
			$scope.showBussiness('commodity');
		}
	}
	$scope.selectCategory = function() {
		if($scope.categoryList.length == 0) {
			$httpService.header('method', 'getCategory');
			$httpService.post(urlParam, $scope, function(result){
				$scope.loading.hide();
				$httpService.deleteHeader('method');	
				if(result.data.success == false) {
					return;
				}
				$scope.categoryList = result.data.item.categoryList;//
				//$scope.$apply();//刷新数据
				$scope.showBussiness('category');
			});
		} else {
			$scope.showBussiness('category');
		}
	}
	$scope.selectClassify = function() {
		//获取车种
		let category = $scope.supplier_business['category'];
		if(Object.keys(category).length <= 0) {return $alert({title: 'Notice', content: '请先选择主营车种！', templateUrl: '/modal-warning.html', show: true});}
		let category_ids = Object.keys(category).toString();
		$scope.param.category_ids = category_ids;
		$httpService.header('method', 'getClassify');
		$httpService.post(urlParam, $scope, function(result){
			$scope.loading.hide();
			$httpService.deleteHeader('method');	
			if(result.data.success == false) {
				return;
			}
			$scope.classifyList = result.data.item.classifyList;//
			//$scope.$apply();//刷新数据
			$scope.showBussiness('classify');
		});
	}
	$scope.selectCrafting_type = function() {
		$scope.showBussiness('crafting_type');
	}
	$scope.selectOperate_market = function() {
		$scope.showBussiness('operate_market');
	}
	$scope.selectCountry = function() {
		$scope.showBussiness('country');
	}
	$scope.selectCertification_system = function() {
		$scope.showBussiness('certification_system');
	}
	//
	let asideBussiness = {};
	$scope.showBussiness = function(type) {
		$scope.asideType = type;
		asideBussiness = $aside({scope: $scope,title: $scope.action_nav_name,placement: 'center',animation: 'am-fade-and-slide-top',backdrop: "static",
			container: '#MainController',templateUrl: 'SupplierBussiness.html'
		});
		asideBussiness.$promise.then(function() {
			asideBussiness.show();
			$(document).ready(function() {
			});
		})
	}
	//选择CheckboxData
	$scope.choiceCheckboxData = function(type, id, object) {
		if(!angular.isDefined($scope.checkboxData[type])) {$scope.checkboxData[type] = {};}
		//if(!angular.isDefined($scope.checkboxData[type][id])) {$scope.checkboxData[type][id] = false;}
		if($scope.checkboxData[type][id]) {
			$scope.supplier_business[type][id] = object;
		} else {
			delete $scope.supplier_business[type][id];
		}
		console.log($scope.supplier_business);
	}
	//删除已选CheckboxData
	$scope.deleteCheckboxData = function(type, id) {
		delete $scope.supplier_business[type][id];
		$scope.checkboxData[type][id] = false;
	}
	//
	$scope.addStep = function(key) {
		if(!angular.isDefined($scope.step[key])) {$scope.step[key] = {};} 
		let obj = {};let _key = new Date().getTime();obj[_key] = "0";
		Object.assign($scope.step[key],obj);
		if(key == "imgs") {
			$(document).ready(function() {
				$scope.setImage(_key);
			});
		}
		if(key == "file") {
			$(document).ready(function() {
				$scope.setUpload(_key);
			});
		}
	}
	$scope.reduceStep = function(key, delete_id) {
		if(!angular.isDefined($scope.step[key])) return
		let jsonLen = Object.keys($scope.step[key]).length;
		if(jsonLen < 1) {$alert({title: 'Notice', content: '最少需要1个', templateUrl: '/modal-warning.html', show: true});return;}
		if(angular.isDefined(delete_id)) {
			delete $scope.step[key][delete_id];
		} else {
			let lastKey = Object.keys($scope.step[key]).pop();
			if (lastKey) {delete $scope.step[key][lastKey];}
		}
		console.log($scope.step);
	}
	//<!--上传及操作-->
	$scope.setUpload = function(key) {
		var uploadJsonUrl = $scope.__WEB+'index/uploadFile?channel='+__ImagesUploadUrl+"&UseType=supplier";
		var fileManagerJsonUrl = $scope.__WEB+'index/fileManager?channel='+__ImagesManagerUrl+"&UseType=supplier";
		window.K = KindEditor;
		var editor = K.editor({
			uploadJson : uploadJsonUrl,fileManagerJson : fileManagerJsonUrl,allowFileManager : true,formatUploadUrl: false
		});
		//K('.insertfile').click(function() {
		K('#insertfile'+key).click(function() {
			let _this = this;
			editor.loadPlugin('insertfile', function() {
				editor.plugin.fileDialog({
					//clickFn : function(url, title, width, height, border, align) {
					clickFn : function(url) {
						editor.hideDialog();
						$(_this).src = url;
						let node = $(_this).attr('node');
						if(typeof($scope.supplier.supplier_upload[node]) == 'undefined') $scope.supplier.supplier_upload[node] = {};
						$scope.supplier.supplier_upload[node].file_url = url;
						$scope.$apply();//刷新数据
					}
				});
			});
		});
	} 
	//images
	$scope.images = {};
	$scope.setImage = function(key) {
		//$scope.__WEB+'app.do?method=uploadCompanyLogo&channel='+$rootScope.__ImagesUploadUrl;
		var uploadJsonUrl = $scope.__WEB+'index/uploadCompanyLogo?channel='+__ImagesUploadUrl+"&UseType=supplier";
		var fileManagerJsonUrl = $scope.__WEB+'index/fileManager?channel='+__ImagesManagerUrl+"&UseType=supplier";
		window.K = KindEditor;
		var editor = K.editor({
			uploadJson : uploadJsonUrl,fileManagerJson : fileManagerJsonUrl,allowFileManager : true,formatUploadUrl: false
		});
		//let keditor = K('.set_image_src');
		let keditor = K('#set_image_src'+key);
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