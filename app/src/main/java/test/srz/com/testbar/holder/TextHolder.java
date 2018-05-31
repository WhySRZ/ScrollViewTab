package test.srz.com.testbar.holder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.drakeet.multitype.ItemViewBinder;
import test.srz.com.testbar.R;
import test.srz.com.testbar.bean.TextBean;

/**
 *  项目描述:工作履历列表holder
 *  作者: shenrunzhou 
 *  创建时间  :2018-03-14  16:21
 */

public class TextHolder
        extends ItemViewBinder<TextBean,TextHolder.ViewHolder> {
    private Context mContext;

    public TextHolder(Context mContext){
        this.mContext =mContext;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView mTextView;

        public ViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.tv_test);
        }
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater,
                                            @NonNull ViewGroup viewGroup)
    {

        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.text_item, viewGroup, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull TextBean item) {

        holder.mTextView.setText(item.mString);
    }


}
