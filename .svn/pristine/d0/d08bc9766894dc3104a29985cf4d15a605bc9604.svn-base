package in.srain.cube.views.ptr;


import in.srain.cube.views.ptr.indicator.PtrIndicator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AbsListView.OnScrollListener;

public class PtrClassicFrameLayout extends PtrFrameLayout {

    private PtrClassicDefaultHeader mPtrClassicHeader;

    public PtrClassicFrameLayout(Context context) {
        super(context);
        initViews();
    }

    public PtrClassicFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public PtrClassicFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    private void initViews() {
        mPtrClassicHeader = new PtrClassicDefaultHeader(getContext());
        setHeaderView(mPtrClassicHeader);
        addPtrUIHandler(mPtrClassicHeader);
    }

    public PtrClassicDefaultHeader getHeader() {
        return mPtrClassicHeader;
    }

    /**
     * Specify the last update time by this key string
     *
     * @param key
     */
    public void setLastUpdateTimeKey(String key) {
        if (mPtrClassicHeader != null) {
            mPtrClassicHeader.setLastUpdateTimeKey(key);
        }
    }

    /**
     * Using an object to specify the last update time.
     *
     * @param object
     */
    public void setLastUpdateTimeRelateObject(Object object) {
        if (mPtrClassicHeader != null) {
            mPtrClassicHeader.setLastUpdateTimeRelateObject(object);
        }
    }
    @Override
    protected void onPositionChange(boolean isInTouching, byte status,
    		PtrIndicator mPtrIndicator) {
    	super.onPositionChange(isInTouching, status, mPtrIndicator);
    	if (mListener!=null) {
    		mListener.onScrollChanged(mPtrIndicator.getCurrentPosY(), mPtrIndicator.getLastPosY());
		}
    }
    public interface MyScrollViewListener {  
		  
	    void onScrollChanged(int curY, int oldy);  
	  
	} 
    private MyScrollViewListener mListener;
    public void setMyOnScrollListener(MyScrollViewListener listener)
    {
    	mListener=listener;
    }
}
