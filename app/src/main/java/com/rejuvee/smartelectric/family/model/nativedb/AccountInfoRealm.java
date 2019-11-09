package com.rejuvee.smartelectric.family.model.nativedb;

import com.base.frame.log.LogUtil;

import java.util.ArrayList;
import java.util.List;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmResults;
import io.realm.annotations.RealmModule;

/**
 * Created by liuchengran on 2017/12/22.
 */
public class AccountInfoRealm {
    private static final String TAG = LogUtil.makeLogTag(AccountInfoRealm.class);
    private final RealmConfiguration realmConfig;
    private Realm realm;


    @RealmModule(classes = AccountInfo.class)
    public class AccountInfoModule {
    }

    public AccountInfoRealm() {
        realmConfig = new RealmConfiguration.Builder() // The app is responsible for calling `Realm.init(Context)`
                .name("account_info.realm")// So always use a unique name
                .schemaVersion(0)
                .modules(new AccountInfoModule())           // Always use explicit modules in library projects
                .build();

    }

    private RealmMigration realmMigration = new RealmMigration() {
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

        }

        @Override
        public int hashCode() {
            return 1;
        }

        @Override
        public boolean equals(Object o) {
            return (o instanceof RealmMigration);
        }
    };


    public void deleteRealm() {
        Realm.deleteRealm(realmConfig);
    }

    public void open() {
        // Don't use Realm.setDefaultInstance() in library projects. It is unsafe as app developers can override the
        // default configuration. So always use explicit configurations in library projects.
        realm = Realm.getInstance(realmConfig);
    }


    public Realm getRealm() {
        return realm;
    }

    /**
     * 添加用户
     * @param
     */
    public void addAccount(final AccountInfo accountInfo) {
        realm.executeTransactionAsync(realm -> realm.insertOrUpdate(accountInfo));
    }

    /**
     * 删除某个用户
     *
     * @param
     */
    public void removeAccountInfo(final AccountInfo accountInfo) {
        realm.executeTransactionAsync(realm -> accountInfo.deleteFromRealm());
    }

    /**
     * 获区所有用户信息
     *
     * @return
     */
    public List<AccountInfo> getAccountInfoList() {
        List<AccountInfo> accountInfos = new ArrayList<>();
        RealmResults<AccountInfo> accountInfoRealmList = realm.where(AccountInfo.class).findAll();
        for (int i =0;i< accountInfoRealmList.size();i++){
            accountInfos.add(realm.copyFromRealm(accountInfoRealmList.get(i)));

        }
        return accountInfos;
    }

    public void close() {
        realm.close();
    }
}
