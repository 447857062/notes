package kelijun.com.notes.demo;

import android.os.AsyncTask;

import java.net.URL;

/**
 * Created by ${kelijun} on 2018/6/20.
 * 线程池执行任务
 * new DownloadAsyncTask().execute(url1, url2, url3);
 */

public class DownloadAsyncTask extends AsyncTask<URL,Integer,Long> {
    @Override
    protected Long doInBackground(URL... urls) {
        return 0L;
    }
    //显示结果
    @Override
    protected void onPostExecute(Long l) {
        super.onPostExecute(l);
    }
    //进度更新
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

}
