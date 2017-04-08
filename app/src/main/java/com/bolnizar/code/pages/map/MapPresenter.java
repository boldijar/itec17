package com.bolnizar.code.pages.map;

import com.bolnizar.code.data.api.responses.BaseResponse;
import com.bolnizar.code.data.model.LocationItem;
import com.bolnizar.code.data.model.PositionRecord;
import com.bolnizar.code.data.prefs.AppPrefsConstants;
import com.bolnizar.code.data.prefs.LongPreference;
import com.bolnizar.code.presenter.BasePresenter;
import com.bolnizar.code.utils.RxUtils;
import com.orm.SugarRecord;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import timber.log.Timber;

public class MapPresenter extends BasePresenter<AppMapView> {

    @Inject
    MapPresenter() {

    }

    @Inject
    BackendService mBackendService;

    @Inject
    @Named(AppPrefsConstants.LAST_SYNC_ID)
    LongPreference mLongPreference;

    public void init(AppMapView view, Context context) {
        super.init(view);
        Subscription subscription = Observable.interval(10, TimeUnit.SECONDS).compose(RxUtils.provideDefaultTransformer())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) {
                        sync(context);
                    }
                });
        addSubscription(subscription);
    }

    public void sync(Context context) {
        @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        List<LocationItem> locationItems = new ArrayList<>();
        long lastId = mLongPreference.get();
        Timber.d("lastId is " + lastId);
        List<PositionRecord> records = lastId != -1 ? SugarRecord.find(PositionRecord.class, "id > ?", lastId + "")
                : SugarRecord.listAll(PositionRecord.class);
        if (records.size() > 10) {
            records = records.subList(0, 10);
        }
        if (records.size() == 0) {
            return;
        }
        for (PositionRecord record : records) {
            locationItems.add(LocationItem.fromRecord(record, deviceId));
        }
        List<PositionRecord> finalRecords = records;
        mBackendService.sync(locationItems)
                .compose(RxUtils.provideDefaultTransformer())
                .subscribe(new Subscriber<BaseResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        mLongPreference.set(finalRecords.get(finalRecords.size() - 1).getId());
                    }
                });
    }
}
