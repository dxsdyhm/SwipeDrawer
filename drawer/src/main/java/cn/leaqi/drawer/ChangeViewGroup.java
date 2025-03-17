package cn.leaqi.drawer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class ChangeViewGroup extends ViewGroup {
    private View currentView;

    public ChangeViewGroup(Context context) {
        super(context);
    }

    public ChangeViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2) {
            throw new IllegalStateException("必须包含两个子视图");
        }
        // 新增tag校验
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i).getTag() == null) {
                throw new IllegalStateException("子视图必须设置android:tag属性");
            }
        }
        updateDisplay();
    }

    @Override
    public void setTag(Object tag) {
        if(tag!=null && !tag.equals(getTag())){
            requestLayout();
        }
        super.setTag(tag);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        updateDisplay();
        int maxWidth = 0;
        int maxHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth());
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
            }
        }
        setMeasuredDimension(maxWidth, maxHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (currentView != null) {
            currentView.layout(0, 0, currentView.getMeasuredWidth(), currentView.getMeasuredHeight());
        }
    }

    // 根据容器tag更新显示
    private void updateDisplay() {
        Object tag = getTag();

        // 新增默认显示逻辑
        if (tag == null) {
            Log.e("dxs", "tag is null");
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.setVisibility(i == 0 ? VISIBLE : GONE);
            }
            currentView = getChildAt(0);
            return;
        }

        Log.e("dxs", "updateDisplay:"+tag.toString());

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            Object childTag = child.getTag();

            if (childTag != null && java.util.Objects.equals(childTag, tag)) {
                child.setVisibility(VISIBLE);
                currentView = child;
            } else {
                child.setVisibility(GONE);
            }
        }
    }
}
