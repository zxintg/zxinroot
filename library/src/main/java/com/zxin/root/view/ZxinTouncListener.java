package com.zxin.root.view;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/*****
 * 手势事件处理
 *
 * by zxin 2019/06/25 15:08
 *
 */
public class ZxinTouncListener implements View.OnTouchListener, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
    private Context mContext;
    private GestureDetector mDetector;
    private View mView;

    private ZxinTouncListener(Context mContext) {
        this.mContext = mContext;
    }

    private void setView(View mView) {
        this.mView = mView;
    }

    /*****
     * 添加监听事件
     * @param mListener
     */
    private void setOnGestureListener(GestureListener mListener) {
        this.mListener = mListener;
    }

    /****
     * 绑定参数
     */
    public void bindGesture() {
        if (mView == null) {
            return;
        }
        mDetector = new GestureDetector(mContext, this);
        mDetector.setOnDoubleTapListener(this);
        mView.setOnTouchListener(this);
    }

    private GestureListener getListener() {
        return mListener;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        GestureListener listener = getListener();
        if (listener != null) {
            listener.onScroll(mView);
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        GestureListener listener = getListener();
        if (listener != null) {
            listener.onLongPress(mView, e);
        }
    }

    @Override
    public boolean onFling(MotionEvent startEvent, MotionEvent endEvent,
                           float velocityX, float velocityY) {
        GestureListener listener = getListener();
        if (listener == null) {
            return false;
        }
        listener.onFling(mView, startEvent, endEvent);

        if (startEvent.getY() - endEvent.getY() > 100) {
            listener.onFlingUp(mView, startEvent, endEvent, velocityX, velocityY);
            return true;
        } else if (startEvent.getY() - endEvent.getY() < -100) {
            listener.onFlingDown(mView, startEvent, endEvent, velocityX, velocityY);
            return true;
        } else if (startEvent.getX() - endEvent.getX() > 100) {
            listener.onFlingLeft(mView, startEvent, endEvent, velocityX, velocityY);
            return true;
        } else if (startEvent.getX() - endEvent.getX() < -100) {
            listener.onFlingRight(mView, startEvent, endEvent, velocityX, velocityY);
            return true;
        }
        return false;
    }

    private GestureListener mListener;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean isConsume = mDetector.onTouchEvent(event);
        if (!isConsume) {
            GestureListener listener = getListener();
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: {
                    if (listener != null) {
                        listener.onTouchDown(mView, event);
                    }
                }
                break;

                case MotionEvent.ACTION_UP: {
                    if (listener != null) {
                        listener.onTouchUp(mView, event);
                    }
                }
                break;

                case MotionEvent.ACTION_MOVE: {
                    if (listener != null) {
                        listener.onTouchMove(mView, event);
                    }
                }
                break;

                case MotionEvent.ACTION_CANCEL: {
                    if (listener != null) {
                        listener.onCancel(mView);
                    }
                }
                break;

                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;

                default:
                    return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        GestureListener listener = getListener();
        if (listener != null) {
            listener.onTap(mView, e);
        }
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        GestureListener listener = getListener();
        if (listener != null) {
            listener.onDoubleTap(mView);
        }
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    public interface GestureListener {
        void onFlingDown(View view, MotionEvent startEvent, MotionEvent endEvent,
                         float velocityX, float velocityY);

        void onFlingUp(View view, MotionEvent startEvent, MotionEvent endEvent,
                       float velocityX, float velocityY);

        void onFlingLeft(View view, MotionEvent startEvent, MotionEvent endEvent,
                         float velocityX, float velocityY);

        void onFlingRight(View view, MotionEvent startEvent, MotionEvent endEvent,
                          float velocityX, float velocityY);

        void onTouchDown(View view, MotionEvent e);

        void onTouchMove(View view, MotionEvent motionEvent);

        void onTouchUp(View view, MotionEvent motionEvent);

        void onLongPress(View view, MotionEvent e);

        void onFling(View view, MotionEvent e1, MotionEvent e2);

        void onDoubleTap(View view);

        void onScroll(View view);

        void onTap(View view, MotionEvent e);

        void onCancel(View view);
    }

    /*****
     * 建造者模式
     */
    public static class Build {
        private Context mContext;
        private View mView;
        private GestureListener mListener;

        public Build(Context mContext) {
            this.mContext = mContext;
        }

        public Build setView(View mView) {
            this.mView = mView;
            return this;
        }

        public Build setGestureListener(GestureListener mListener) {
            this.mListener = mListener;
            return this;
        }

        public ZxinTouncListener build(){
            ZxinTouncListener listener = new ZxinTouncListener(mContext);
            listener.setView(mView);
            listener.setOnGestureListener(mListener);
            return listener;
        }

    }

}
