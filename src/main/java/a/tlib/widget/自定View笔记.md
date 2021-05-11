
```
var gestureDetector: GestureDetector(context,object : GestureDetector.OnGestureListener {
     // 2. 用户轻触触摸屏，尚未松开或拖动
     // 与onDown()的区别：无松开 / 拖动
     // 即：当用户点击的时，onDown（）就会执行，在按下的瞬间没有松开 / 拖动时onShowPress就会执行
     override fun onShowPress(e: MotionEvent) {
  
     }
  
     // 4. 用户轻击屏幕后抬起
     override fun onSingleTapUp(e: MotionEvent): Boolean {
         return false
  
     }
  
     // 1. 用户轻触触摸屏
     override fun onDown(e: MotionEvent): Boolean {
         return false
     }
  
     // 6. 用户按下触摸屏、快速移动后松开，慢慢滑动不会触发。会伴随触发onScroll
     // 参数：
     // e1：第1个ACTION_DOWN MotionEvent
     // e2：最后一个ACTION_MOVE MotionEvent
     // velocityX：X轴上的移动速度，像素/秒
     // velocityY：Y轴上的移动速度，像素/秒
     override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
         return false
     }
  
     // 5. 用户按下触摸屏 & 拖动
     // e1：第1个ACTION_DOWN MotionEvent
     // e2：最后一个ACTION_MOVE MotionEvent
     // distanceX：距离上次产生onScroll事件后，X抽移动的距离
     // distanceY：距离上次产生onScroll事件后，Y抽移动的距离
     override fun onScroll(e1: MotionEvent?, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
         if (e2.action == MotionEvent.ACTION_MOVE) {
             if (Math.abs(distanceX) > Math.abs(distanceY)) {
                 return true
             }
         }
         return false
     }
  
     // 3. 用户长按触摸屏
     override fun onLongPress(e: MotionEvent?) {
  
     })
}
```