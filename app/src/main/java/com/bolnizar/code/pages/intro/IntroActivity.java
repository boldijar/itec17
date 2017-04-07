package com.bolnizar.code.pages.intro;

import com.bolnizar.code.R;
import com.bolnizar.code.data.model.TestModel;
import com.bolnizar.code.view.activities.BaseFragmentActivity;
import com.orm.SugarRecord;

import android.os.Bundle;
import android.widget.Toast;

import java.util.Iterator;

import butterknife.ButterKnife;

public class IntroActivity extends BaseFragmentActivity implements IntroNavigation {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        TestModel testModel = new TestModel();
        testModel.time = System.currentTimeMillis();
        testModel.save();

        Iterator<TestModel> list = SugarRecord.findAll(TestModel.class);
        int count = 0;
        while (list.hasNext()) {
            count++;
            list.next();
        }
        Toast.makeText(this, count + " a", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected int getContainerId() {
        return R.id.intro_container;
    }

}
