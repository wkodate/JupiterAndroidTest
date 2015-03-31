package com.Ichif1205.jupiter.tests;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.Loader;
import android.test.ActivityInstrumentationTestCase2;

import com.Ichif1205.jupiter.Constant;
import com.Ichif1205.jupiter.MainActivity;
import com.Ichif1205.jupiter.item.ItemData;

/**
 * MainActivityのテスト.
 *
 * @author wkodate
 *
 */
public class MainActivityTest extends
        ActivityInstrumentationTestCase2<MainActivity> {

    /**
     * ログ.
     */
    private static final String TAG = "MainActivityTest";

    /**
     * テスト用URL.
     */
    private final String SAMPLE_URL = "http://www.test.com";

    /**
     * テスト用タイトル.
     */
    private final String SAMPLE_TITLE = "test title";

    /**
     * テスト用RSSタイトル.
     */
    private final String SAMPLE_RSS_TITLE = "test rss title";

    /**
     * MainActivity.
     */
    private MainActivity activity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.activity = getActivity();
    }

    @Override
    protected void tearDown() {
        activity.finish();
    }

    /**
     * Loaderの起動確認.
     */
    public void testOnCreateLoader() {
        Loader<List<ItemData>> loader = null;
        Bundle bundle = new Bundle();
        loader = activity.onCreateLoader(1, bundle);
        assertNotNull(loader);
        assertTrue(loader.isReset());
    }

    /**
     * Loader完了の確認.
     */
    public void testLoader() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                int count = 1;
                Loader<List<ItemData>> loader = activity.onCreateLoader(
                        count,
                        bundle);
                List<ItemData> list = new ArrayList<>();
                ItemData item = makeItemData(SAMPLE_URL, SAMPLE_TITLE,
                        SAMPLE_RSS_TITLE);
                list.add(item);
                activity.onLoadFinished(loader, list);
            }
        });
        // UIスレッド終了まで待つ
        getInstrumentation().waitForIdleSync();

        // listviewのチェック
        assertTrue(activity.getListview().isEnabled());
        assertFalse(activity.getListview().getAdapter().isEmpty());

        // loaderのチェック
        assertFalse(activity.getAsyncFetcher().isStarted());

        // TODO: クリック時の処理

    }

    /**
     * シェア用Intentの設定テスト.
     */
    public void testShareIntent() {
        Intent intent = activity.getDefaultShareIntent();
        assertEquals(intent.getAction(), Intent.ACTION_SEND);
        assertEquals(intent.getType(), "text/plain");
        assertEquals(intent.getExtras().get(Intent.EXTRA_TEXT),
                Constant.SHARE_TEXT);
    }

    /**
     * WebviewへのIntentをチェック.
     */
    public void testWebviewIntent() {
        Intent intent = activity.getWebviewIntent(SAMPLE_URL, SAMPLE_TITLE); ;

        assertEquals(SAMPLE_URL, intent.getExtras().get("url"));
        assertEquals(SAMPLE_TITLE, intent.getExtras().get("title"));
    }

    /**
     * ItemDataを作成.
     *
     * @param url
     * @param title
     * @param rssTitle
     * @return ItemData
     */
    private ItemData makeItemData(String url, String title, String rssTitle) {
        ItemData item = new ItemData();
        item.setLink(url);
        item.setTitle(title);
        item.setRssTitle(rssTitle);
        return item;
    }

}
