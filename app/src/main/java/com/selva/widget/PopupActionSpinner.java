package com.selva.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.Spinner;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class PopupActionSpinner extends Spinner {

    private static final String M_POPUP = "mPopup";
    private static final String DROPDOWN_POPUP = "DropdownPopup";
    private static final String IS_SHOWING = "isShowing";
    private static final String DIALOG_POPUP = "DialogPopup";
    private static final int MODE_UNKNOWN = -1;

    private PopupTouchInterceptor popupTouchInterceptor;

    private Field mFieldSpinnerPopup = null;
    private PopupActionSpinner[] mPopupActionSpinnersArr = null;

    /**
     * Construct a new spinner with the given context's theme and the supplied attribute set.
     *
     * @param context The Context the view is running in, through which it can
     *                access the current theme, resources, etc.
     * @param attrs   The attributes of the XML tag that is inflating the view.
     */
    public PopupActionSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Define your own Interface to control what happens when the Spinner dropdown popup is open.
     * @param mpopupTouchInterceptor
     */
    public void setPopupTouchInterceptor(PopupTouchInterceptor mpopupTouchInterceptor,
                                         PopupActionSpinner[] allSpinners  )
    {
        this.popupTouchInterceptor = mpopupTouchInterceptor;
        this.mPopupActionSpinnersArr = allSpinners;
    }


    @Override
    public boolean performClick() {
        boolean handled = true;

        try {
            handled = super.performClick();

            // reflecting Spinner.mPopup field
            if (isFieldSpinnerPopupNull()) {
                mFieldSpinnerPopup = this.getClass().getSuperclass().getDeclaredField(M_POPUP);
            }

            // disable access checks to Spinner.mPopup
            mFieldSpinnerPopup.setAccessible(true);

            // get value of mPopup field
            Object spinnerPopup = mFieldSpinnerPopup.get(this);

            // reflecting SpinnerPopup.isShowing()
            Method isShowing = mFieldSpinnerPopup.getType()
                    .getDeclaredMethod(IS_SHOWING, (Class[]) null);

            // calling Spinner.mPopup.isShowing()
            boolean isShowingResult = (boolean) isShowing.invoke(spinnerPopup, (Object[]) null);

            if (isShowingResult) {

                // check if mFieldSpinnerPopup is a dialog popup
                if (getSpinnerMode() == MODE_DIALOG) {
                    //Do Nothing
                } else if (getSpinnerMode() == MODE_DROPDOWN) {
                    // reflecting Spinner.mPopup.mPopup
                    Field fieldPopupWindow = ListPopupWindow.class.getDeclaredField(M_POPUP);
                    fieldPopupWindow.setAccessible(true);

                    ((PopupWindow) fieldPopupWindow.get(spinnerPopup)).setTouchInterceptor(new OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent event) {


                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN: {
                                    if(!isViewInBounds(view,event.getRawX(),event.getRawY())) {
                                        for (View spinnerView : mPopupActionSpinnersArr)

                                            if (isPointInsideView(event.getRawX(), event.getRawY(), spinnerView)) {
                                                popupTouchInterceptor.onTouchIntercepted(spinnerView);
                                                break;
                                            }
                                    }
                                    break;
                                }

                            }

                            return false;
                        }
                    });
                    fieldPopupWindow.setAccessible(false);
                }
            }

            // enable access checks to Spinner.mPopup
            mFieldSpinnerPopup.setAccessible(false);

        } catch (Exception exception) {
        }
        return handled;
    }

    public static boolean isPointInsideView(float x, float y, View view){
        int location[] = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];

        //point is inside view bounds
        if(( x > viewX && x < (viewX + view.getWidth())) &&
                ( y > viewY && y < (viewY + view.getHeight()))){
            return true;
        } else {
            return false;
        }
    }

    private boolean isViewInBounds(View view, float x, float y) {
        Rect outRect = new Rect();
        int[] location = new int[2];
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains((int)x, (int)y);
    }

    /**
     * Returns a constant describing how the user selects choices from the spinner.
     *
     * @return the choosing mode of this <code>{@link Spinner}</code>
     */
    public int getSpinnerMode() {
        int result = MODE_UNKNOWN;

        try {
            // reflecting Spinner.mPopup field
            if (isFieldSpinnerPopupNull()) {
                mFieldSpinnerPopup = this.getClass().getSuperclass().getDeclaredField(M_POPUP);
            }

            // get Spinner.DropdownPopup class name
            mFieldSpinnerPopup.setAccessible(true);
            String spinnerPopupClassName = mFieldSpinnerPopup.get(this).getClass().getSimpleName();
            mFieldSpinnerPopup.setAccessible(false);

            switch (spinnerPopupClassName) {
                case DIALOG_POPUP:
                    result = MODE_DIALOG;
                    break;
                case DROPDOWN_POPUP:
                    result = MODE_DROPDOWN;
                    break;
                default:
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return result;
    }

    public boolean isFieldSpinnerPopupNull() {
        return mFieldSpinnerPopup == null;
    }

    @Override
    public int getId() {
        return super.getId();
    }
}
