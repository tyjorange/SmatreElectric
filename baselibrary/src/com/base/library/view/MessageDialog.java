package com.base.library.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.base.library.R;

import java.util.ArrayList;

/**
 * 提示框
 * 
 * @author shaxiaoning
 *
 */
public class MessageDialog extends Dialog implements
		DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {

	private Context mContext;
	private LayoutInflater inflater;
	private LayoutParams lp;
	private int percentageH = 4;
	private int percentageW = 8;

	private TextView txtTitle = null;
	private TextView txtConfim = null;
	private TextView txtCancel = null;
	// private AnimationDrawable loadingAnimation;
	private String title;
	private String btnConfim;
	private String btnCancel;
	ArrayList<OnCancelListener> m_arrCancelListeners = new ArrayList<OnCancelListener>();
	ArrayList<OnDismissListener> m_arrDismissListeners = new ArrayList<OnDismissListener>();
	private OnInputMileageChanged onChanged = null;
	private Spanned spanned;

	/**
	 *
	 * @param context
	 * @param title
	 *            标题内容
	 * @param btnConfim
	 *            确定按钮文字
	 * @param btnCancel
	 *            取消息按钮文字
	 * @param onChanged
	 */
	public MessageDialog(Context context, String title, String btnConfim,
						 String btnCancel, OnInputMileageChanged onChanged) {
		super(context, R.style.Dialog);

		this.mContext = context;
		this.onChanged = onChanged;
		this.title = title;
		this.btnConfim = btnConfim;
		this.btnCancel = btnCancel;

	}

	public MessageDialog(Context context, Spanned spanned, String btnConfim,
						 String btnCancel, OnInputMileageChanged onChanged) {
		super(context, R.style.Dialog);

		this.mContext = context;
		this.onChanged = onChanged;
		this.spanned = spanned;
		this.btnConfim = btnConfim;
		this.btnCancel = btnCancel;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View mView = inflater.inflate(R.layout.base_dialog_message, null);
		setContentView(mView);
		// 设置window属性
		lp = getWindow().getAttributes();
		lp.gravity = Gravity.CENTER;
		lp.dimAmount = 0.6f; // 去背景遮盖
		lp.alpha = 1.0f;
		int[] wh = initWithScreenWidthAndHeight(mContext);
		lp.width = wh[0] - wh[0] / percentageW;
		lp.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
		getWindow().setAttributes(lp);
		setCanceledOnTouchOutside(false);
		setOnDismissListener(this);
		setOnCancelListener(this);
		initView(mView);

	}

	private void initView(View mView) {
		txtTitle = (TextView) mView.findViewById(R.id.dialog_message_title);

		txtConfim = (TextView) mView
				.findViewById(R.id.dialog_message_btn_confim);
		txtCancel = (TextView) mView
				.findViewById(R.id.dialog_message_btn_cancel);
		// inputEdit.setText(curMileage+"");
		txtTitle.setText(title == null || title.equals("") ? spanned : title);
		txtConfim.setText(btnConfim);
		txtCancel.setText(btnCancel);
		txtConfim.setOnClickListener(confimListener);
		txtCancel.setOnClickListener(cancelListener);

	}

	private View.OnClickListener confimListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			onDismiss();
			onChanged.onConfirm();

		}

	};

	private View.OnClickListener cancelListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			onDismiss();
			onChanged.onCancel();

		}
	};

	private void ondismiss() {

	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		if (m_arrDismissListeners != null) {
			for (int x = 0; x < m_arrDismissListeners.size(); x++)
				m_arrDismissListeners.get(x).onDismiss(dialog);
		}
		ondismiss();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		if (m_arrCancelListeners != null) {
			for (int x = 0; x < m_arrDismissListeners.size(); x++)
				m_arrCancelListeners.get(x).onCancel(dialog);
		}
	}

	public void addListeners(OnCancelListener c, OnDismissListener d) {
		m_arrDismissListeners.add(d);
		m_arrCancelListeners.add(c);
	}

	public void removeListeners(OnCancelListener c, OnDismissListener d) {
		m_arrDismissListeners.remove(d);
		m_arrCancelListeners.remove(c);
	}

	private void onDismiss() {
		if (this.isShowing()) {
			this.dismiss();
		}

	}

	/**
	 * 获取当前window width,height
	 * 
	 * @param context
	 * @return
	 */
	private static int[] initWithScreenWidthAndHeight(Context context) {
		int[] wh = new int[2];
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		wh[0] = dm.widthPixels;
		wh[1] = dm.heightPixels;
		return wh;
	}

	public interface OnInputMileageChanged {
		public void onConfirm();

		public void onCancel();

	}
}