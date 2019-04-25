package com.zxin.root.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import com.zxin.root.R;
import com.zxin.root.adapter.simple.SimpleAdapter;
import com.zxin.root.adapter.simple.ZxinViewHolder;
import com.zxin.root.bean.VerticalBanner;
import com.zxin.root.util.UiUtils;
import com.zxin.root.view.banner.BaseBannerAdapter;
import com.zxin.root.view.banner.VerticalBannerView;
import java.util.List;

/**
 * Description:
 * <p/>
 * Created by kui.liu<br/>
 * Date: 16/1/7<br/>
 * Time: 下午2:41<br/>
 */
public class VerBannerAdapter extends BaseBannerAdapter<VerticalBanner> {

    public VerBannerAdapter(Context mContext,List<VerticalBanner> datas) {
        super(mContext,datas);
    }

    @Override
    public View getView(VerticalBannerView parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.common_recycle_notitle,null);
    }

    @Override
    public void setItem(final View view, final VerticalBanner data) {

    }

    @Override
    public void setItem(View view, List<VerticalBanner> itemList) {
        if (itemList==null||itemList.isEmpty())
            return;
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_recycle_review);
        recyclerView.setLayoutManager(UiUtils.getInstance(mContext).getLayoutManager(UiUtils.LayoutManager.VERTICAL));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(new SimpleAdapter<VerticalBanner>(mContext, itemList, R.layout.item_common_banner) {
            @Override
            protected void onBindViewHolder(final ZxinViewHolder holder, final VerticalBanner localCircle, int viewType) {
                holder.setText(R.id.tv_banner_title, localCircle.getTitle())
                        .setImageDrawable(R.id.iv_banner_image,UiUtils.getInstance(mContext).getDrawable(R.drawable.point_select));
                holder.setOnItemListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener!=null)
                            listener.onItemClick(getDatas().indexOf(localCircle));
                    }
                });
            }
        });
    }
}
