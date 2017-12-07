package zp.com.zpnotebook.home;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import zp.com.zpbase.activity.ZpBaseActivity;
import zp.com.zpbase.fragment.ZpBaseFragment;
import zp.com.zpnotebook.R;
import zp.com.zpnotebook.account.AccountFragment;
import zp.com.zpnotebook.center.fragment.CenterFragment;
import zp.com.zpnotebook.home.fragment.HomeFragment;

public class MainActivity extends ZpBaseActivity {

    private BottomNavigationView mBottomNav;

    @Override
    protected int exInitLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void exInitView() {
        super.exInitView();
        mBottomNav = (BottomNavigationView) findViewById(R.id.main_bottom_menu);

        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        HomeFragment homeFragment = new HomeFragment();
                        setMainFragment(homeFragment);
                        break;
                    case R.id.action_explore:
                        AccountFragment accountFragment = new AccountFragment();
                        setMainFragment(accountFragment);
                        break;
                    case R.id.action_me:
                        CenterFragment centerFragment = new CenterFragment();
                        setMainFragment(centerFragment);
                        break;
                }
                return true;
            }
        });
    }

    // 设置Fragment
    private void setMainFragment(ZpBaseFragment baseFragment){
        FragmentManager fm = this.getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_main_content, baseFragment);
        transaction.commit();
    }


}
