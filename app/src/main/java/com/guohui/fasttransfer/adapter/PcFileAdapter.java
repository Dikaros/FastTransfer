package com.guohui.fasttransfer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.guohui.fasttransfer.R;
import com.guohui.fasttransfer.base.FileMsg;
import com.guohui.fasttransfer.base.WebFile;
import com.guohui.fasttransfer.utils.FileUtils;

import java.util.List;

/**
 * Created by Dikaros on 2016/5/29.
 */
public class PcFileAdapter extends BaseAdapter {


    Context context;
    List<WebFile> fileMsgs;

    public PcFileAdapter(Context context, List<WebFile> fileMsgs) {
        this.context = context;
        this.fileMsgs = fileMsgs;
    }

    @Override
    public int getCount() {
        return fileMsgs.size();
    }

    @Override
    public Object getItem(int position) {
        return fileMsgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WebFile file = fileMsgs.get(position);

        ViewHolder holder = null;
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.cell_pc_file_item,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(file.getName());
        holder.tvType.setText(file.getType());
        holder.tvProgress.setText(file.getProgress());
        holder.tvSize.setText(FileUtils.longToFileSize(Long.parseLong(file.getLength())));
        return convertView;
    }

    class ViewHolder{
        TextView tvName,tvSize,tvProgress,tvType;

        public ViewHolder(View v){
            tvName = (TextView) v.findViewById(R.id.tv_item_file_name);
            tvProgress = (TextView) v.findViewById(R.id.tv_item_file_progress);
            tvSize = (TextView) v.findViewById(R.id.tv_item_file_size);
            tvType = (TextView) v.findViewById(R.id.tv_item_file_type);
            tvProgress.setText("准备传输");
        }
    }

    public void updateFile(WebFile file,int index){
        fileMsgs.get(index).setProgress(file.getProgress());
        fileMsgs.get(index).setSize(file.getSize());
        fileMsgs.get(index).setLength(file.getLength());
        fileMsgs.get(index).setReaded(file.getReaded());

    }
}
