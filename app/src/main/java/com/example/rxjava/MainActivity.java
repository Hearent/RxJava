package com.example.rxjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    private String text = "Hello from RxJava!";
    private Observable<String> observable;
    private DisposableObserver<String> observer1;
    private DisposableObserver<String> observer2;
    private CompositeDisposable compositeDisposable;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        compositeDisposable = new CompositeDisposable();

        observable = Observable.just(text);

        compositeDisposable.add(
                observable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(getObserver1())
        );

        compositeDisposable.add(
                observable
                        .subscribeWith(getObserver2())
        );
    }

    private DisposableObserver getObserver1() {
        observer1 = new DisposableObserver<String>() {
            @Override
            public void onNext(@NonNull String s) {
                Log.d(TAG, "onNext()");
                textView.setText(s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError()");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete()");
            }
        };
        return observer1;
    }

    private DisposableObserver getObserver2() {
        observer2 = new DisposableObserver<String>() {
            @Override
            public void onNext(@NonNull String s) {
                Log.d(TAG, "onNext()");
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError()");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete()");
            }
        };
        return observer2;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}