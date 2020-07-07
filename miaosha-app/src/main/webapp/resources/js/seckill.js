// 存放主要交换逻辑js代码
// javascript 模块化
var seckill = {
	// 封装秒杀相关ajax的url
	URL : {
		basePath : function() {
			return $('#basePath').val();
		},
		now : function() {
			return seckill.URL.basePath() + 'seckill/time/now';
		},
		exposer : function(seckillId) {
			return seckill.URL.basePath() + 'seckill/' + seckillId + '/exposer';
		},
		execution : function(seckillId, md5) {
			return seckill.URL.basePath() + 'seckill/' + seckillId + '/' + md5 + '/execution';
		}
	},

	// 处理秒杀逻辑 https://www.imooc.com/video/11736 网课地址
	handleSeckill : function(seckillId, node) {//node为上层方法调用时候的【seckillBox = $('#seckillBox')】
		// 获取秒杀地址，控制显示逻辑，执行秒杀，先隐藏一下，后面再控制显示
		node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');//按钮，是先隐藏的
		console.log('exposerUrl=' + seckill.URL.exposer(seckillId));//TODO
		$.post(seckill.URL.exposer(seckillId), {}, function(result) {
			// 在回调函数中，执行交互流程
			if (result && result['success']) {
				var exposer = result['data'];
				if (exposer['exposed']) {//判断是否开启秒杀，开启秒杀需要显示按钮，没开启，放入当前系统时间
					// 开启秒杀
					var md5 = exposer['md5'];
					var killUrl = seckill.URL.execution(seckillId, md5);//md5，exposer中返回的md5,拿到url之后就给按钮注册一个秒杀事件
					console.log('killUrl=' + killUrl);//TODO
					//通过id选择器，click一致是绑定，one只绑定一次点击事件，防止高秒杀作多次点击发送url给服务器
					$('#killBtn').one('click', function() {
						// 执行秒杀请求
						// 1.先禁用按钮，this在哪执行就指向哪个对象，指向按钮对象
						$(this).addClass('disabled');//禁用按钮
						// 2.发送秒杀请求
						$.post(killUrl, {}, function(result) {
							if (result && result['success']) {
								var killResult = result['data'];
								var state = killResult['state'];
								var stateInfo = killResult['stateInfo'];
								// 3.显示秒杀结果
								node.html('<span class="label label-success">' + stateInfo + '</span>');
							}
						});
					});
					//显示之前隐藏的按钮
					node.show();
				} else {
					// 未开启秒杀！！！！！！！！！，防止用户浏览器时间偏差，重新走一遍计时逻辑
					var now = exposer['now'];
					var start = exposer['start'];
					var end = exposer['end'];
					// 重新计算计时逻辑 ！！！！！
					seckill.countdown(seckillId, now, start, end);
				}
			} else {
				console.log('result=' + result);
			}
		});
	},
	// 验证手机号
	validatePhone : function(phone) {
		if (phone && phone.length == 11 && !isNaN(phone)) {
			return true;
		} else {
			return false;
		}
	},
	// 倒计时
	countdown : function(seckillId, nowTime, startTime, endTime) {
		// 时间判断
		var seckillBox = $('#seckillBox');
		if (nowTime > endTime) {
			// 1-秒杀结束
			seckillBox.html('秒杀结束!');//通过id选择器# 拿到节点进行输出，在detail中有专门的span Id 盒子节点
		} else if (nowTime < startTime) {
			// 2-秒杀未开始，计时事件绑定，使用插件
			var killTime = new Date(startTime + 1000);//防止计时间偏移
			seckillBox.countdown(killTime, function(event) {//
				// 时间格式
				var format = event.strftime('秒杀倒计时：%D天 %H时 %M分 %S秒');
				seckillBox.html(format);
				// 时间完成后回调事件
			}).on('finish.countdown', function() {//时间名字为finish,countdown
				// 获取秒杀地址，控制显示逻辑，执行秒杀（点击秒杀按钮）
				seckill.handleSeckill(seckillId, seckillBox);//往seckiddId里面写
			});
		} else {
			// 3-秒杀开始
			seckill.handleSeckill(seckillId ,seckillBox);
		}
	},
	// 详情页秒杀逻辑
	detail : {
		// 详情页初始化
		init : function(params) {
			// 用户手机验证和登录，计时交互
			// 规划我们的交互流程
			// 在cookie中查找手机号
			var killPhone = $.cookie('killPhone');//之前已经通过jsp传递过来三个参数了
			var startTime = params['startTime'];
			var endTime = params['endTime'];
			var seckillId = params['seckillId'];
			// 验证手机号
			if (!seckill.validatePhone(killPhone)) {
				// 绑定phone
				// 控制输出
				var killPhoneModal = $('#killPhoneModal');//选中节点
				killPhoneModal.modal({//bootstrap组件，传输一个json数据
					show : true,// 显示弹出层
					backdrop : 'static',// 禁止位置关闭，
					keyboard : false    //防止点击其他区域关闭弹出层
				// 关闭键盘事件
				})
				$('#killPhoneBtn').click(function() {
					var inputPhone = $('#killphoneKey').val();
					console.log('inputPhone='+inputPhone);//TODO
					if (seckill.validatePhone(inputPhone)) {//如果验证通过
						// 电话写入cookie
						$.cookie('killPhone', inputPhone, {
							expires : 7,//有效期
							path : '/seckill' //只在此路径下有效
						});
						// 刷新页面
						window.location.reload();//会重新走一遍init
					} else {
						$('#killphoneMessage').hide().html('<label class="label label-danger">手机号错误!</label>').show(300);
					}
				});
			}
			// 已经登录
			// 计时交互,秒杀未开始
			var startTime = params['startTime'];
			var endTime = params['endTime'];
			var seckillId = params['seckillId'];
			$.get(seckill.URL.now(), {}, function(result) {//2-参数，3-回调函数，将返回的结果result传入此方法中
				if (result && result['success']) {
					var nowTime = result['data'];
					// 时间判断，计时交互
					seckill.countdown(seckillId, nowTime, startTime, endTime);
				} else {
					console.log(result['reult:'] + result);//输出到浏览器的console中
				}
			});
		}
	}
}
