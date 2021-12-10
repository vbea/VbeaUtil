package com.vbes.util.matisse.ui;

import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.vbes.util.matisse.entity.Album;
import com.vbes.util.matisse.entity.Item;
import com.vbes.util.matisse.entity.SelectionSpec;
import com.vbes.util.matisse.model.AlbumMediaCollection;
import com.vbes.util.matisse.ui.adapter.PreviewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AlbumPreviewActivity extends BasePreviewActivity implements
        AlbumMediaCollection.AlbumMediaCallbacks {

    public static final String EXTRA_ALBUM = "extra_album";
    public static final String EXTRA_ITEM = "extra_item";

    private AlbumMediaCollection mCollection = new AlbumMediaCollection();

    private boolean mIsAlreadySetPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!SelectionSpec.getInstance().hasInited) {
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        mCollection.onCreate(this, this);
        Album album = getIntent().getParcelableExtra(EXTRA_ALBUM);
        mCollection.load(album);

        Item item = getIntent().getParcelableExtra(EXTRA_ITEM);
        if (mSpec.countable) {
            mCheckView.setCheckedNum(mSelectedCollection.checkedNumOf(item));
        } else {
            mCheckView.setChecked(mSelectedCollection.isSelected(item));
        }
        updateSize(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCollection.onDestroy();
    }

    @Override
    public void onAlbumMediaLoad(Cursor cursor) {
        List<Item> items = new ArrayList<>();
        while (cursor.moveToNext()) {
            items.add(Item.valueOf(cursor));
        }
//        cursor.close();

        if (items.isEmpty()) {
            return;
        }

        PreviewPagerAdapter adapter = (PreviewPagerAdapter) mPager.getAdapter();
        adapter.addAll(items);
        adapter.notifyDataSetChanged();
        if (!mIsAlreadySetPosition) {
            //onAlbumMediaLoad is called many times..
            mIsAlreadySetPosition = true;
            Item selected = getIntent().getParcelableExtra(EXTRA_ITEM);
            int selectedIndex = items.indexOf(selected);
            mPager.setCurrentItem(selectedIndex, false);
            mPreviousPos = selectedIndex;
        }
    }

    @Override
    public void onAlbumMediaReset() {

    }
}
