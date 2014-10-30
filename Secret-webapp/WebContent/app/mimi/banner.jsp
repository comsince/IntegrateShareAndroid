<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html style="height: ${param.h}px;width: ${param.w}px;overflow: hidden;">
	<head>
		<link href="banner.css" rel="stylesheet" type="text/css"/>
		<script type="text/javascript" src="../../theme/jquery-1.8.3.min.js"></script>
		<style type="text/css">
		
		.info li {
	     float: left;
	     margin-left: ${param.w / 50}px;
     }
		
		
		
		
		
		</style>
		<script type="text/javascript">
		var lengthMove = ${param.w};
		 
		</script>
	</head> 
	
	<body style="height: ${param.h}px;width: ${param.w}px;overflow: hidden;">
				<div class="slide">
					<ul id="bigImgArr" class="clearfix slide-ul"  style="width:${param.w * 5}px">
						<li id="pic0b" class="moveBigImg">
							<img class="bannerImg" src="1.jpg"   height="${param.h}px" width="${param.w}px"/>
						</li>
						<li id="pic1b">
							<img src="2.jpg"  height="${param.h}px" width="${param.w}px"/>
						</li>
						<li id="pic2b">
							<img src="3.jpg"  height="${param.h}px" width="${param.w}px"/>
						</li>
						<li id="pic3b">
							<img src="4.jpg"  height="${param.h}px" width="${param.w}px"/>
						</li>
						<li id="pic4b">
							<img src="5.jpg"  height="${param.h}px" width="${param.w}px"/>
						</li>
					</ul>
				</div>
               <div class="smallDiv" style="width: ${param.w}px;top:${param.h-param.h / 5-14}px"> 
				<ul id="smallInfo" class="info clearfix" >
					<li class="current" id="pic0" sid="0">
						<img src="1.jpg"   height="${param.h / 5}px" width="${param.w / 6}px"/>
					</li>
					<li id="pic1" sid="1" class="">
						<img src="2.jpg"  height="${param.h / 5}px" width="${param.w / 6}px"/>
					</li>
					<li id="pic2" sid="2" class="">
						<img src="3.jpg"  height="${param.h / 5}px" width="${param.w / 6}px"/>
					</li>
					<li id="pic3" sid="3" class="">
						<img src="4.jpg" height="${param.h / 5}px" width="${param.w / 6}px"/>
					</li>
					<li id="pic4" sid="4" class="">
						<img src="5.jpg" height="${param.h / 5}px" width="${param.w /6}px"/>
					</li>
				</ul>
              </div> 





	

	<script type="text/javascript" src="banner.js"></script>
	</body>
</html>