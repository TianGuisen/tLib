package a.tlib.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel

/**
 * @author 田桂森 2021/6/25 0025
 * LiveData在实体类里可以通知指定某个字段的数据更新
 * MutableLiveData则是完全是整个实体类或者数据类型变化后才通知.不会细节到某个字段
 */
open class BaseVM(app: Application)  : AndroidViewModel(app) {
    
}