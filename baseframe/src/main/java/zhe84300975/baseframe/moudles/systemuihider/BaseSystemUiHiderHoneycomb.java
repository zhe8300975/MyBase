package zhe84300975.baseframe.moudles.systemuihider;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by zhaowencong on 16/8/24.
 * Describe: 控制标题栏和状态栏
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BaseSystemUiHiderHoneycomb extends BaseSystemUiHider{

    private int mShowFlags;

    private int mHideFlags;

    private int mTestFlags;

    private boolean mVisible = true;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected BaseSystemUiHiderHoneycomb(AppCompatActivity activity, View anchorView, int flags) {
        super(activity, anchorView, flags);

        mShowFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        mHideFlags = View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        mTestFlags = View.SYSTEM_UI_FLAG_LOW_PROFILE;

        if ((mFlags & FLAG_FULLSCREEN) == FLAG_FULLSCREEN) {
            // If the client requested fullscreen, add flags relevant to hiding
            // the status bar. Note that some of these constants are new as of
            // API 16 (Jelly Bean). It is safe to use them, as they are inlined
            // at compile-time and do nothing on pre-Jelly Bean devices.
            mShowFlags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            mHideFlags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        if ((mFlags & FLAG_HIDE_NAVIGATION) == FLAG_HIDE_NAVIGATION) {
            // If the client requested hiding navigation, add relevant flags.
            mShowFlags |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            mHideFlags |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            mTestFlags |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }
    }


    /*
    * {@inheritDoc}
    */
    @Override
    public void setup() {
        mAnchorView.setOnSystemUiVisibilityChangeListener(mSystemUiVisibilityChangeListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide() {
        mAnchorView.setSystemUiVisibility(mHideFlags);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        mAnchorView.setSystemUiVisibility(mShowFlags);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isVisible() {
        return mVisible;
    }

    private View.OnSystemUiVisibilityChangeListener mSystemUiVisibilityChangeListener = new View.OnSystemUiVisibilityChangeListener() {

        @Override
        public void onSystemUiVisibilityChange(int vis) {
            // Test
            // against
            // mTestFlags
            // to
            // see
            // if
            // the
            // system
            // UI
            // is
            // visible.
            if ((vis & mTestFlags) != 0) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    // Pre-Jelly
                    // Bean,
                    // we
                    // must
                    // use
                    // the
                    // old
                    // window
                    // flags
                    // API.
                    mActivity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }

                // As
                // we
                // use
                // the
                // appcompat
                // toolbar
                // as
                // an
                // action
                // bar,
                // we
                // must
                // manually
                // hide
                // it
                if (mActivity.getSupportActionBar() != null) {
                    mActivity.getSupportActionBar().hide();
                }

                // Trigger
                // the
                // registered
                // listener
                // and
                // cache
                // the
                // visibility
                // state.
                mOnVisibilityChangeListener.onVisibilityChange(false);
                mVisible = false;

            } else {
                mAnchorView.setSystemUiVisibility(mShowFlags);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    // Pre-Jelly
                    // Bean,
                    // we
                    // must
                    // use
                    // the
                    // old
                    // window
                    // flags
                    // API.
                    mActivity.getWindow().setFlags(0, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }

                // As
                // we
                // use
                // the
                // appcompat
                // toolbar
                // as
                // an
                // action
                // bar,
                // we
                // must
                // manually
                // show
                // it
                if (mActivity.getSupportActionBar() != null) {
                    mActivity.getSupportActionBar().show();
                }

                // Trigger
                // the
                // registered
                // listener
                // and
                // cache
                // the
                // visibility
                // state.
                mOnVisibilityChangeListener.onVisibilityChange(true);
                mVisible = true;
            }
        }
    };


}
