package com.base.library.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.base.library.R;
import com.base.library.utils.SizeUtils;

import java.util.LinkedList;
import java.util.List;


/**
 * @blog http://blog.csdn.net/xiaanming 
 * 
 * @author xiaanming
 *
 */
public class DragGridView extends GridView{
	/**
	 * DragGridView��item������Ӧ��ʱ�䣬 Ĭ����1000���룬Ҳ������������
	 */
	private long dragResponseMS = 1000;
	
	/**
	 * �Ƿ������ק��Ĭ�ϲ�����
	 */
	private boolean isDrag = false;
	
	private int mDownX;
	private int mDownY;
	private int moveX;
	private int moveY;
	/**
	 * �տ�ʼ��ק��item��Ӧ��View
	 */
	private int mDragPosition;
	
	
	private View mStartDragItemView = null;
	
	/**
	 * ������ק�ľ�������ֱ����һ��ImageView
	 */
	private ImageView mDragImageView;
	
	/**
	 * ����
	 */
	private Vibrator mVibrator;
	
	private WindowManager mWindowManager;
	/**
	 * item����Ĳ��ֲ���
	 */
	private WindowManager.LayoutParams mWindowLayoutParams;
	
	/**
	 * ������ק��item��Ӧ��Bitmap
	 */
	private Bitmap mDragBitmap;
	
	/**
	 * ���µĵ㵽����item���ϱ�Ե�ľ���
	 */
	private int mPoint2ItemTop ; 
	
	/**
	 * ���µĵ㵽����item�����Ե�ľ���
	 */
	private int mPoint2ItemLeft;
	
	/**
	 * DragGridView������Ļ������ƫ����
	 */
	private int mOffset2Top;
	
	/**
	 * DragGridView������Ļ��ߵ�ƫ����
	 */
	private int mOffset2Left;
	
	/**
	 * ״̬���ĸ߶�
	 */
	private int mStatusHeight; 
	
	/**
	 * DragGridView�Զ����¹����ı߽�ֵ
	 */
	private int mDownScrollBorder;
	
	/**
	 * DragGridView�Զ����Ϲ����ı߽�ֵ
	 */
	private int mUpScrollBorder;
	
	/**
	 * DragGridView�Զ��������ٶ�
	 */
	private static final int speed = 20;
	
	private boolean mAnimationEnd = true;
	
	private DragGridBaseAdapter mDragAdapter;
	private int mNumColumns;
	private int mColumnWidth;
	private boolean mNumColumnsSet;
	private int mHorizontalSpacing;
	
	
	public DragGridView(Context context) {
		this(context, null);
	}
	
	public DragGridView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DragGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		mStatusHeight = getStatusHeight(context); //???????????
		
		if(!mNumColumnsSet){
			mNumColumns = AUTO_FIT;
		}
		
	}
	
	private Handler mHandler = new Handler();
	
	//���������Ƿ�Ϊ������Runnable
	private Runnable mLongClickRunnable = new Runnable() {
		
		@Override
		public void run() {
			isDrag = true; //���ÿ�����ק
			mVibrator.vibrate(50); //��һ��
			mStartDragItemView.setVisibility(View.INVISIBLE);//���ظ�item
			
			//�������ǰ��µĵ���ʾitem����
			createDragImage(mDragBitmap, mDownX, mDownY);
		}
	};
	
	
	@Override
	public void setAdapter(ListAdapter adapter) {
		super.setAdapter(adapter);
		
		if(adapter instanceof DragGridBaseAdapter){
			mDragAdapter = (DragGridBaseAdapter) adapter;
		}else{
			throw new IllegalStateException("the adapter must be implements DragGridAdapter");
		}
	}
	

	@Override
	public void setNumColumns(int numColumns) {
		super.setNumColumns(numColumns);
		mNumColumnsSet = true;
		this.mNumColumns = numColumns;
	}
	
	
	@Override
	public void setColumnWidth(int columnWidth) {
	    super.setColumnWidth(columnWidth);
	    mColumnWidth = columnWidth;
	}
	
	
    @Override
	public void setHorizontalSpacing(int horizontalSpacing) {
		super.setHorizontalSpacing(horizontalSpacing);
		this.mHorizontalSpacing = horizontalSpacing;
	}
    

    /**
     * ����������ΪAUTO_FIT�������������������������
     */
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mNumColumns == AUTO_FIT) {
            int numFittedColumns;
            if (mColumnWidth > 0) {
                int gridWidth = Math.max(MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
                        - getPaddingRight(), 0);
                numFittedColumns = gridWidth / mColumnWidth;
                if (numFittedColumns > 0) {
                    while (numFittedColumns != 1) {
                        if (numFittedColumns * mColumnWidth + (numFittedColumns - 1)
                                * mHorizontalSpacing > gridWidth) {
                            numFittedColumns--;
                        } else {
                            break;
                        }
                    }
                } else {
                    numFittedColumns = 1;
                }
            } else {
                numFittedColumns = 2;
            }
            mNumColumns = numFittedColumns;
        } 
        mbInOnMeasure = true;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
	
	private boolean mbInOnMeasure = false;
	
	public boolean isOnMeasure() {
		return mbInOnMeasure;
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mbInOnMeasure = false;
		super.onLayout(changed, l, t, r, b);
	}
	
	/**
	 * ????????????????????????1000????
	 * @param dragResponseMS
	 */
	public void setDragResponseMS(long dragResponseMS) {
		this.dragResponseMS = dragResponseMS;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch(ev.getAction()){
		case MotionEvent.ACTION_DOWN:
			mDownX = (int) ev.getX();
			mDownY = (int) ev.getY();
			
			//���ݰ��µ�X,Y�����ȡ�����item��position
			mDragPosition = pointToPosition(mDownX, mDownY);
			
			
			if(mDragPosition == AdapterView.INVALID_POSITION){
				return super.dispatchTouchEvent(ev);
			}
			
			//ʹ��Handler�ӳ�dragResponseMSִ��mLongClickRunnable
			mHandler.postDelayed(mLongClickRunnable, dragResponseMS);
			
			//����position��ȡ��item����Ӧ��View
			mStartDragItemView = getChildAt(mDragPosition - getFirstVisiblePosition());
			
			//?????????????????��????????????????????
			mPoint2ItemTop = mDownY - mStartDragItemView.getTop();
			mPoint2ItemLeft = mDownX - mStartDragItemView.getLeft();
			
			mOffset2Top = (int) (ev.getRawY() - mDownY);
			mOffset2Left = (int) (ev.getRawX() - mDownX);
			
			//???DragGridView???????????????????��????????DragGridView???????
			mDownScrollBorder = getHeight() / 5;
			//???DragGridView?????????????????????????????DragGridView???????
			mUpScrollBorder = getHeight() * 4/5;
			
			
			
			//????mDragItemView???????
			mStartDragItemView.setDrawingCacheEnabled(true);
			//???mDragItemView??????��?Bitmap????
			mDragBitmap = Bitmap.createBitmap(mStartDragItemView.getDrawingCache());
			//???????????????????��???????????????
			mStartDragItemView.destroyDrawingCache();
			
			
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int)ev.getX();
			int moveY = (int) ev.getY();
			
			//?????????????item?????????????????item?????????????mRunnable
			if(!isTouchInItem(mStartDragItemView, moveX, moveY)){
				mHandler.removeCallbacks(mLongClickRunnable);
			}
			break;
		case MotionEvent.ACTION_UP:
			mHandler.removeCallbacks(mLongClickRunnable);
			mHandler.removeCallbacks(mScrollRunnable);
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	
	/**
	 * ???????GridView??item????
	 * @param dragView
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isTouchInItem(View dragView, int x, int y){
		if(dragView == null){
			return false;
		}
		int leftOffset = dragView.getLeft();
		int topOffset = dragView.getTop();
		if(x < leftOffset || x > leftOffset + dragView.getWidth()){
			return false;
		}

        return y >= topOffset && y <= topOffset + dragView.getHeight();
    }
	
	

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(isDrag && mDragImageView != null){
			switch(ev.getAction()){
			case MotionEvent.ACTION_MOVE:
				moveX = (int) ev.getX();
				moveY = (int) ev.getY();
				
				//???item
				onDragItem(moveX, moveY);
				break;
			case MotionEvent.ACTION_UP:
				onStopDrag();
				isDrag = false;
				break;
			}
			return true;
		}
		return super.onTouchEvent(ev);
	}
	
	@Override
    protected void dispatchDraw(Canvas canvas){
        super.dispatchDraw(canvas);
        int childCount = getChildCount();
        if (childCount == 0)
        	return;
        
        View localView1 = getChildAt(0);
        int column = getWidth() / localView1.getWidth();
       
        Paint localPaint;
        localPaint = new Paint();
        localPaint.setStyle(Paint.Style.STROKE);
        localPaint.setStrokeWidth(SizeUtils.dp2px(1));
        localPaint.setColor(getContext().getResources().getColor(R.color.grid_line));
        for(int i = 0;i < childCount;i++){
            View cellView = getChildAt(i);
            if((i + 1) % column == 0){
                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
            }else if((i + 1) > (childCount - (childCount % column))){
                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
            }else{
                canvas.drawLine(cellView.getRight(), cellView.getTop(), cellView.getRight(), cellView.getBottom(), localPaint);
                canvas.drawLine(cellView.getLeft(), cellView.getBottom(), cellView.getRight(), cellView.getBottom(), localPaint);
            }
        }
        if(childCount % column != 0){
            for(int j = 0 ;j < (column-childCount % column) ; j++){
                View lastView = getChildAt(childCount - 1);
                canvas.drawLine(lastView.getRight() + lastView.getWidth() * j, lastView.getTop(), lastView.getRight() + lastView.getWidth()* j, lastView.getBottom(), localPaint);
            }
            
            View lastView = getChildAt(childCount - childCount % column);
            canvas.drawLine(lastView.getLeft(), lastView.getBottom(), getRight(), lastView.getBottom(), localPaint);
            
        }
    }

	
	
	/**
	 * ????????????
	 * @param bitmap 
	 * @param downX
	 * 			???????????????X????
	 * @param downY
	 * 			???????????????X????
	 */
	private void createDragImage(Bitmap bitmap, int downX , int downY){
		mWindowLayoutParams = new WindowManager.LayoutParams();
		mWindowLayoutParams.format = PixelFormat.TRANSLUCENT; //????????????????
		mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
		mWindowLayoutParams.x = downX - mPoint2ItemLeft + mOffset2Left;
		mWindowLayoutParams.y = downY - mPoint2ItemTop + mOffset2Top - mStatusHeight;
		mWindowLayoutParams.alpha = 0.55f; //?????
		mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;  
		mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;  
		mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  
	                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ;
		  
		mDragImageView = new ImageView(getContext());  
		mDragImageView.setImageBitmap(bitmap);  
		mWindowManager.addView(mDragImageView, mWindowLayoutParams);  
	}
	
	/**
	 * ???????????????????
	 */
	private void removeDragImage(){
		if(mDragImageView != null){
			mWindowManager.removeView(mDragImageView);
			mDragImageView = null;
		}
	}
	
	/**
	 * ???item?????????????item?????��??????item???????????GridView?????��???
	 * @param moveX
	 * @param moveY
	 */
	private void onDragItem(int moveX, int moveY){
		mWindowLayoutParams.x = moveX - mPoint2ItemLeft + mOffset2Left;
		mWindowLayoutParams.y = moveY - mPoint2ItemTop + mOffset2Top - mStatusHeight;
		mWindowManager.updateViewLayout(mDragImageView, mWindowLayoutParams); //????????��??
		onSwapItem(moveX, moveY);
		
		//GridView???????
		mHandler.post(mScrollRunnable);
	}
	
	
	/**
	 * ??moveY?????????????????????????GridView??????????
	 * ??moveY???��????????????????????GridView??????????
	 * ??????��???
	 */
	private Runnable mScrollRunnable = new Runnable() {
		
		@Override
		public void run() {
			int scrollY;
			if(getFirstVisiblePosition() == 0 || getLastVisiblePosition() == getCount() - 1){
				mHandler.removeCallbacks(mScrollRunnable);
			}
			
			if(moveY > mUpScrollBorder){
				 scrollY = speed;
				 mHandler.postDelayed(mScrollRunnable, 25);
			}else if(moveY < mDownScrollBorder){
				scrollY = -speed;
				 mHandler.postDelayed(mScrollRunnable, 25);
			}else{
				scrollY = 0;
				mHandler.removeCallbacks(mScrollRunnable);
			}
			
			smoothScrollBy(scrollY, 10);
		}
	};
	
	
	/**
	 * ????item,???????item?????????????��??
	 * @param moveX
	 * @param moveY
	 */
	private void onSwapItem(int moveX, int moveY){
		//????????????????????item??position
		final int tempPosition = pointToPosition(moveX, moveY);
		
		//????tempPosition ????????tempPosition??????-1,????��???
		if(tempPosition != mDragPosition && tempPosition != AdapterView.INVALID_POSITION && mAnimationEnd){
			mDragAdapter.reorderItems(mDragPosition, tempPosition);
			mDragAdapter.setHideItem(tempPosition);
			
			final ViewTreeObserver observer = getViewTreeObserver();
			observer.addOnPreDrawListener(new OnPreDrawListener() {
				
				@Override
				public boolean onPreDraw() {
					observer.removeOnPreDrawListener(this);
					animateReorder(mDragPosition, tempPosition);
					mDragPosition = tempPosition;
					return true;
				}
			} );
			
		}
	}
	
	/**
	 * ???????????
	 * @param view
	 * @param startX
	 * @param endX
	 * @param startY
	 * @param endY
	 * @return
	 */
	private AnimatorSet createTranslationAnimations(View view, float startX,
													float endX, float startY, float endY) {
		ObjectAnimator animX = ObjectAnimator.ofFloat(view, "translationX",
				startX, endX);
		ObjectAnimator animY = ObjectAnimator.ofFloat(view, "translationY",
				startY, endY);
		AnimatorSet animSetXY = new AnimatorSet();
		animSetXY.playTogether(animX, animY);
		return animSetXY;
	}

	
	/**
	 * item?????????��??
	 * @param oldPosition
	 * @param newPosition
	 */
	private void animateReorder(final int oldPosition, final int newPosition) {
		boolean isForward = newPosition > oldPosition;
		List<Animator> resultList = new LinkedList<>();
		if (isForward) {
			for (int pos = oldPosition; pos < newPosition; pos++) {
				View view = getChildAt(pos - getFirstVisiblePosition());
				System.out.println(pos);
				
				if ((pos + 1) % mNumColumns == 0) {
					resultList.add(createTranslationAnimations(view,
							- view.getWidth() * (mNumColumns - 1), 0,
							view.getHeight(), 0));
				} else {
					resultList.add(createTranslationAnimations(view,
							view.getWidth(), 0, 0, 0));
				}
			}
		} else {
			for (int pos = oldPosition; pos > newPosition; pos--) {
				View view = getChildAt(pos - getFirstVisiblePosition());
				if ((pos + mNumColumns) % mNumColumns == 0) {
					resultList.add(createTranslationAnimations(view,
							view.getWidth() * (mNumColumns - 1), 0,
							-view.getHeight(), 0));
				} else {
					resultList.add(createTranslationAnimations(view,
							-view.getWidth(), 0, 0, 0));
				}
			}
		}

		AnimatorSet resultSet = new AnimatorSet();
		resultSet.playTogether(resultList);
		resultSet.setDuration(300);
		resultSet.setInterpolator(new AccelerateDecelerateInterpolator());
		resultSet.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				mAnimationEnd = false;
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				mAnimationEnd = true;
			}
		});
		resultSet.start();
	}
	
	/**
	 * ?????????????????item????????????????????
	 */
	private void onStopDrag(){
		View view = getChildAt(mDragPosition - getFirstVisiblePosition());
		if(view != null){
			view.setVisibility(View.VISIBLE);
		}
		mDragAdapter.setHideItem(-1);
		removeDragImage();
	}
	
	/**
	 * ???????????
	 * @param context
	 * @return
	 */
	private static int getStatusHeight(Context context){
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            } 
        }
        return statusHeight;
    }
	
}
