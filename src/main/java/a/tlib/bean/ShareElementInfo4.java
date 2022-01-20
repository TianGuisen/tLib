package a.tlib.bean;

import android.os.Parcel;

/**
 * Created by huangwei on 2018/9/19 0019.
 */
public class ShareElementInfo4 extends BaseData {
    
    public ShareElementInfo4(String url, int width, int height) {
        super(url, width, height);
    }
    
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte((byte) 0);
    }
    
    protected ShareElementInfo4(Parcel in) {
        super(in);
    }
    
    public static final Creator<ShareElementInfo4> CREATOR = new Creator<ShareElementInfo4>() {
        @Override
        public ShareElementInfo4 createFromParcel(Parcel source) {
            return new ShareElementInfo4(source);
        }
        
        @Override
        public ShareElementInfo4[] newArray(int size) {
            return new ShareElementInfo4[size];
        }
    };
}
