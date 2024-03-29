package com.rejuvee.smartelectric.family.common.widget.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.StyleRes;

import com.rejuvee.smartelectric.family.R;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;

import static android.app.ProgressDialog.STYLE_SPINNER;

public class DownloadProgressDialog extends AlertDialog {
    /**
     * Creates a ProgressDialog with a horizontal progress bar.
     */
    private static final int STYLE_HORIZONTAL = 1;
    private String mProgressNumberFormat;
    private NumberFormat mProgressPercentFormat;
    private ProgressBar mProgress;
    private Handler mViewUpdateHandler;
    private TextView mProgressNumber;
    private TextView mProgressPercent;
    private TextView cancel_dialog;
    private TextView mMessageView;
    private int mProgressStyle = STYLE_SPINNER;
    private int mMax;
    private int mProgressVal;
    private int mSecondaryProgressVal;
    private int mIncrementBy;
    private int mIncrementSecondaryBy;
    private Drawable mProgressDrawable;
    private Drawable mIndeterminateDrawable;
    private CharSequence mMessage;
    private boolean mIndeterminate;

    private boolean mHasStarted;

    public DownloadProgressDialog(Context context) {
        super(context, R.style.MyDialogStyle);
        initFormats();
    }

    protected DownloadProgressDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initFormats();
    }

    protected DownloadProgressDialog(Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        initFormats();
    }

    private void initFormats() {
        mProgressNumberFormat = "%1.2fM/%2.2fM";
        mProgressPercentFormat = NumberFormat.getPercentInstance();
        mProgressPercentFormat.setMaximumFractionDigits(0);
    }

    public static DownloadProgressDialog show(Context context, CharSequence title,
                                              CharSequence message) {
        return show(context, title, message, false);
    }

    public static DownloadProgressDialog show(Context context, CharSequence title,
                                              CharSequence message, boolean indeterminate) {
        return show(context, title, message, indeterminate, false, null);
    }

    public static DownloadProgressDialog show(Context context, CharSequence title,
                                              CharSequence message, boolean indeterminate, boolean cancelable) {
        return show(context, title, message, indeterminate, cancelable, null);
    }

    public static DownloadProgressDialog show(Context context, CharSequence title,
                                              CharSequence message, boolean indeterminate,
                                              boolean cancelable, OnCancelListener cancelListener) {
        DownloadProgressDialog dialog = new DownloadProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setIndeterminate(indeterminate);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.show();
        return dialog;
    }

    private static class MyHandler extends Handler {
        WeakReference<DownloadProgressDialog> activityWeakReference;

        MyHandler(DownloadProgressDialog activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            DownloadProgressDialog progressDialog = activityWeakReference.get();
            /* Update the number and percent */
            double progress = (double) progressDialog.mProgress.getProgress() / (double) (1024 * 1024);
            double max = (double) progressDialog.mProgress.getMax() / (double) (1024 * 1024);
            if (progressDialog.mProgressNumberFormat != null) {
                String format = progressDialog.mProgressNumberFormat;
                progressDialog.mProgressNumber.setText(String.format(format, progress, max));
            } else {
                progressDialog.mProgressNumber.setText("");
            }
            if (progressDialog.mProgressPercentFormat != null) {
                double percent = progress / max;
                SpannableString tmp = new SpannableString(progressDialog.mProgressPercentFormat.format(percent));
                tmp.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                        0, tmp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                progressDialog.mProgressPercent.setText(tmp);
            } else {
                progressDialog.mProgressPercent.setText("");
            }
        }
    }

    private ICancel cancel;

    public void setCancel(ICancel cancel) {
        this.cancel = cancel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (mProgressStyle == STYLE_HORIZONTAL) {
            mViewUpdateHandler = new MyHandler(this);
            View view = inflater.inflate(R.layout.alert_dialog_progress, null);
            mProgress = view.findViewById(R.id.progress);
            mProgressNumber = view.findViewById(R.id.progress_number);
            mProgressPercent = view.findViewById(R.id.progress_percent);
            cancel_dialog = view.findViewById(R.id.cancel_dialog);
            cancel_dialog.setOnClickListener(v -> cancel.onCancel());
            setView(view);
        } else {
            View view = inflater.inflate(R.layout.progress_dialog, null);
            mProgress = view.findViewById(R.id.progress);
            mMessageView = view.findViewById(R.id.message);
            setView(view);
        }
        if (mMax > 0) {
            setMax(mMax);
        }
        if (mProgressVal > 0) {
            setProgress(mProgressVal);
        }
        if (mSecondaryProgressVal > 0) {
            setSecondaryProgress(mSecondaryProgressVal);
        }
        if (mIncrementBy > 0) {
            incrementProgressBy(mIncrementBy);
        }
        if (mIncrementSecondaryBy > 0) {
            incrementSecondaryProgressBy(mIncrementSecondaryBy);
        }
        if (mProgressDrawable != null) {
            setProgressDrawable(mProgressDrawable);
        }
        if (mIndeterminateDrawable != null) {
            setIndeterminateDrawable(mIndeterminateDrawable);
        }
        if (mMessage != null) {
            setMessage(mMessage);
        }
        setIndeterminate(mIndeterminate);
        onProgressChanged();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mHasStarted = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHasStarted = false;
    }

//    public void setCancel(View.OnClickListener clickListener) {
//        cancel_dialog.setOnClickListener(clickListener);
//    }

    public void setProgress(int value) {
        if (mHasStarted) {
            mProgress.setProgress(value);
            onProgressChanged();
        } else {
            mProgressVal = value;
        }
    }

    private void setSecondaryProgress(int secondaryProgress) {
        if (mProgress != null) {
            mProgress.setSecondaryProgress(secondaryProgress);
            onProgressChanged();
        } else {
            mSecondaryProgressVal = secondaryProgress;
        }
    }

    public int getProgress() {
        if (mProgress != null) {
            return mProgress.getProgress();
        }
        return mProgressVal;
    }

    public int getSecondaryProgress() {
        if (mProgress != null) {
            return mProgress.getSecondaryProgress();
        }
        return mSecondaryProgressVal;
    }

    public int getMax() {
        if (mProgress != null) {
            return mProgress.getMax();
        }
        return mMax;
    }

    public void setMax(int max) {
        if (mProgress != null) {
            mProgress.setMax(max);
            onProgressChanged();
        } else {
            mMax = max;
        }
    }

    private void incrementProgressBy(int diff) {
        if (mProgress != null) {
            mProgress.incrementProgressBy(diff);
            onProgressChanged();
        } else {
            mIncrementBy += diff;
        }
    }

    private void incrementSecondaryProgressBy(int diff) {
        if (mProgress != null) {
            mProgress.incrementSecondaryProgressBy(diff);
            onProgressChanged();
        } else {
            mIncrementSecondaryBy += diff;
        }
    }

    private void setProgressDrawable(Drawable d) {
        if (mProgress != null) {
            mProgress.setProgressDrawable(d);
        } else {
            mProgressDrawable = d;
        }
    }

    private void setIndeterminateDrawable(Drawable d) {
        if (mProgress != null) {
            mProgress.setIndeterminateDrawable(d);
        } else {
            mIndeterminateDrawable = d;
        }
    }

    public void setIndeterminate(boolean indeterminate) {
        if (mProgress != null) {
            mProgress.setIndeterminate(indeterminate);
        } else {
            mIndeterminate = indeterminate;
        }
    }

    public boolean isIndeterminate() {
        if (mProgress != null) {
            return mProgress.isIndeterminate();
        }
        return mIndeterminate;
    }

    @Override
    public void setMessage(CharSequence message) {
        if (mProgress != null) {
            if (mProgressStyle == STYLE_HORIZONTAL) {
                super.setMessage(message);
            } else {
                mMessageView.setText(message);
            }
        } else {
            mMessage = message;
        }
    }

    public void setProgressStyle(int style) {
        mProgressStyle = style;
    }

    /**
     * Change the format of the small text showing current and maximum units
     * of progress.  The default is "%1d/%2d".
     * Should not be called during the number is progressing.
     *
     * @param format A string passed to {@link String#format String.format()};
     *               use "%1d" for the current number and "%2d" for the maximum.  If null,
     *               nothing will be shown.
     */
    public void setProgressNumberFormat(String format) {
        mProgressNumberFormat = format;
        onProgressChanged();
    }

    /**
     * Change the format of the small text showing the percentage of progress.
     * The default is
     * {@link NumberFormat#getPercentInstance() NumberFormat.getPercentageInstnace().}
     * Should not be called during the number is progressing.
     *
     * @param format An instance of a {@link NumberFormat} to generate the
     *               percentage text.  If null, nothing will be shown.
     */
    public void setProgressPercentFormat(NumberFormat format) {
        mProgressPercentFormat = format;
        onProgressChanged();
    }

    private void onProgressChanged() {
        if (mProgressStyle == STYLE_HORIZONTAL) {
            if (mViewUpdateHandler != null && !mViewUpdateHandler.hasMessages(0)) {
                mViewUpdateHandler.sendEmptyMessage(0);
            }
        }
    }

    public interface ICancel {
        void onCancel();
    }
}
