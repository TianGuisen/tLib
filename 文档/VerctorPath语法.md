  M = moveto(M X,Y) ：将画笔移动到指定的坐标位置，相当于 android Path 里的moveTo()
  L = lineto(L X,Y) ：画直线到指定的坐标位置，相当于 android Path 里的lineTo()
  H = horizontal lineto(H X)：画水平线到指定的X坐标位置
  V = vertical lineto(V Y)：画垂直线到指定的Y坐标位置
  C = curveto(C X1,Y1,X2,Y2,ENDX,ENDY)：三次贝赛曲线
  S = smooth curveto(S X2,Y2,ENDX,ENDY) 同样三次贝塞尔曲线，更平滑
  Q = quadratic Belzier curve(Q X,Y,ENDX,ENDY)：二次贝赛曲线
  T = smooth quadratic Belzier curveto(T ENDX,ENDY)：映射 同样二次贝塞尔曲线，更平滑
  A = elliptical Arc(A RX,RY,XROTATION,FLAG1,FLAG2,X,Y)：弧线 ，相当于arcTo()
  Z = closepath()：关闭路径（会自动绘制链接起点和终点）
  
  A=rx,ry x-axis-rotation large-arc-flag,sweep-flag x,y：ellipse arc圆弧曲线,
  rx和ry表示椭圆的两个半径，
  x-axis-rotation表示x轴的旋转角度，
  x和y表示绘制椭圆弧线的终点,
  large-arc-flag决定是大弧线还是小弧线，1大0小，sweep-flag决定是顺时针弧线还是逆时针弧线，1顺0逆。
  
  
  
    android:name：定义路径的名称
    android:pathData：定义路径的数据，路径由多条命令组成，格式与SVG标准的path data的d属性完全相同，路径命令的参数定义在viewport视图的坐标系。
    android:fillColor：指定填充路径的颜色，一般是一个颜色值，在SDK24及以上，可以指定一个颜色状态列表或者一个渐变的颜色。
    android:strokeColor：指定路径线条的颜色，一般是一个颜色值，在SDK24及以上，可以指定一个颜色状态列表或者一个渐变的颜色
    android:strokeWidth：指定路径线条的宽度，基于viewport视图的坐标系（不要dp/px之类的结尾）。
    android:strokeAlpha：指定路径线条的透明度。
    android:fillAlpha：指定填充区域的透明度。
    android:trimPathStart：取值从0到1，表示路径从哪里开始绘制。0~trimPathStart区间的路径不会被绘制出来。
    android:trimPathEnd：取值从0到1，表示路径绘制到哪里。trimPathEnd~1区间的路径不会被绘制出来。
    android:trimPathOffset：平移可绘制区域，取值从0到1，线条从(trimPathOffset+trimPathStart绘制到trimPathOffset+trimPathEnd)，注意：trimPathOffset+trimPathEnd如果超过1，其实也是绘制的的，绘制的是0～trimPathOffset+trimPathEnd-1的位置。
    android:strokeLineCap：设置线条首尾的外观，三个值：butt（默认，向线条的每个末端添加平直的边缘）, round（向线条的每个末端添加圆形线帽）, square（向线条的每个末端添加正方形线帽。）。
    android:strokeLineJoin：设置当两条线条交汇时，创建什么样的边角（线段连接类型）：三个值：miter（默认，创建尖角）,round（创建圆角）,bevel（创建斜角） 。
    android:strokeMiterLimit：设置设置最大斜接长度，斜接长度指的是在两条线交汇处内角和外角之间的距离。只有当 lineJoin 属性为 "miter" 时，miterLimit 才有效。