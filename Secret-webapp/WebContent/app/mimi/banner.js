
var move = parseInt($(".info .current").attr("sid"));
var pmove = true;
var cmove = true;
var zmove = true;
var movepic = function movePic() {
	$("#pic" + move).addClass("current").siblings("li").removeClass("current");
	var moveLen = move * lengthMove;
	$("#bigImgArr").animate({left:"-" + moveLen + "px"}, 600);
	if (move == 4) {
		pmove = false;
	}
	if (move == 0) {
		pmove = true;
	}
	if (pmove) {
		move++;
	} else {
		move--;
	}
};
var ctime = setInterval(movepic, 5000);
$(".info li").hover(function () {
	move = parseInt($(this).attr("sid"));
	if (zmove == true) {
		clearInterval(ctime);
		zmove = false;
	}
	$("#pic" + move).addClass("current").siblings("li").removeClass("current");
	var moveLen = move * lengthMove;
	$("#bigImgArr").stop(true, true).animate({left:"-" + moveLen + "px"}, 600);
}, function () {
	if (zmove == false) {
		ctime = setInterval(movepic, 5000);
		zmove = true;
	}
});
$(".info li").click(function () {
	move = parseInt($(this).attr("sid"));
	if (zmove == true) {
		clearInterval(ctime);
		zmove = false;
	}
	$("#pic" + move).addClass("current").siblings("li").removeClass("current");
	var moveLen = move * lengthMove;
	$("#bigImgArr").stop(true, true).animate({left:"-" + moveLen + "px"}, 600);
}, function () {
	if (zmove == false) {
		ctime = setInterval(movepic, 5000);
		zmove = true;
	}
});

  
