package com.rejuvee.smartelectric.family.activity.mine;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.rejuvee.smartelectric.family.R;
import com.rejuvee.smartelectric.family.common.BaseActivity;
import com.rejuvee.smartelectric.family.databinding.ActivitySetnicknameBinding;
import com.rejuvee.smartelectric.family.model.viewmodel.SetInfoViewModel;

/**
 * 设置用户信息
 */
public class SetInfoActivity extends BaseActivity {
    //    private static final int RESULT_CODE = 001;
//    private EditText edit_changname;
    //    private SharedPreferences.Editor edit;
    private int position;
//    private AccountInfo cacheAccount;
//    private String userName;

    //    @Override
//    protected int getLayoutResId() {
//        return R.layout.activity_setnickname;
//    }
//
//    @Override
//    protected int getMyTheme() {
//        return 0;
//    }
    private SetInfoViewModel mViewModel;

    @Override
    protected void initView() {
        ActivitySetnicknameBinding mBinding = DataBindingUtil.setContentView(this, R.layout.activity_setnickname);
        mViewModel = ViewModelProviders.of(this).get(SetInfoViewModel.class);
        mBinding.setVm(mViewModel);
        mBinding.setPresenter(new Presenter());
        mBinding.setLifecycleOwner(this);
//        getCacheAccount();
//        setToolbarHide(true);
//        SharedPreferences spinformation = getSharedPreferences("INFORMATION" + userName, MODE_PRIVATE);
//        edit = spinformation.edit();

        Intent intent = getIntent();
        String nickname = intent.getStringExtra("nickname");
        String username = intent.getStringExtra("username");
        String phone_num = intent.getStringExtra("phone_num");
        position = intent.getIntExtra("position", -1);

//        findViewById(R.id.img_cancel).setOnClickListener(this);
//        findViewById(R.id.text_save).setOnClickListener(this);

//        TextView txt_title = findViewById(R.id.txt_title);
//        edit_changname = findViewById(R.id.edit_changname);
        if (position == 0) {
//            txt_title.setText(getString(R.string.vs165));
            mViewModel.setTxtTitle(getString(R.string.vs165));
//            edit_changname.setText(phone_num);
            mViewModel.setTxtString(phone_num);
        } else if (position == 1) {
//            txt_title.setText(getString(R.string.vs164));
            mViewModel.setTxtTitle(getString(R.string.vs164));
//            edit_changname.setText(nickname);
            mViewModel.setTxtString(nickname);
        } else if (position == 2) {
//            txt_title.setText(getString(R.string.vs127));
            mViewModel.setTxtTitle(getString(R.string.vs127));
//            edit_changname.setText(username);
            mViewModel.setTxtString(username);

        }
    }

//    private void getCacheAccount() {
//        AccountInfoRealm accountInfoRealm = new AccountInfoRealm();
//        accountInfoRealm.open();
//        List<AccountInfo> listAccount = accountInfoRealm.getAccountInfoList();
//        if (listAccount != null && listAccount.size() > 0) {
//            cacheAccount = listAccount.get(0);
//            userName = cacheAccount.getUserName();
//        }
//        accountInfoRealm.close();
//    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.img_cancel:
//                finish();
//                break;
//            case R.id.text_save:
//                if (edit_changname.getText().length() == 0) {
//                    Toast.makeText(this, R.string.prompt, Toast.LENGTH_LONG).show();
//                } else {
//                    setResultChange();
//                    finish();
//                }
//                break;
//            case R.id.txt_cancel:
////                setResultChange();
//                finish();
//                break;
//        }
//    }

//    @Override
//    protected String getToolbarTitle() {
//        return null;
//    }
//
//    @Override
//    protected boolean isDisplayHomeAsUpEnabled() {
//        return false;
//    }

    private void setResultChange() {
        String value = mViewModel.getTxtString().getValue();
        if (position == 0) {
            Intent intent = new Intent();
            intent.putExtra("exchangphone", value);
            setResult(RESULT_OK, intent);
        } else if (position == 1) {
            Intent intent = new Intent();
            intent.putExtra("exchangnick", value);
            setResult(RESULT_OK, intent);
        } else if (position == 2) {
            Intent intent = new Intent();
            intent.putExtra("exchangname", value);
            setResult(RESULT_OK, intent);
        }
    }

    public class Presenter {
        public void onCancel(View view) {
            finish();
        }

        public void onOk(View view) {
            String value = mViewModel.getTxtString().getValue();
            if (value == null || value.isEmpty()) {
                Toast.makeText(view.getContext(), R.string.prompt, Toast.LENGTH_LONG).show();
            } else {
                setResultChange();
                finish();
            }
        }
    }

    @Override
    protected void dealloc() {
        setResultChange();
    }

}
