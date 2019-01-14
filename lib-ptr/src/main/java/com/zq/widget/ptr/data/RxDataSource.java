package com.zq.widget.ptr.data;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

/**
 * Author：zhangqiang
 * Date：2019/1/14 21:14:00
 * Email:852286406@qq.com
 * Github:https://github.com/holleQiang
 */
public abstract class RxDataSource <T> implements DataSource<T>{

    private Disposable disposable;

    @Override
    public void loadData(int pageIndex, int pageSize, int startIndex, int endIndex, final Callback<T> callback) {
        getDataSource(pageIndex,pageSize,startIndex,endIndex)
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        callback.onComplete();
                    }
                })
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(T t) {
                        callback.onNextData(t);
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e);
                    }

                    @Override
                    public void onComplete() {
                        callback.onComplete();
                    }
                });
    }

    @Override
    public void dispose() {
        if (disposable == null || disposable.isDisposed()) {
            return;
        }
        disposable.dispose();
    }

    public abstract Observable<T> getDataSource(int pageIndex, int pageSize, int startIndex, int endIndex);
}