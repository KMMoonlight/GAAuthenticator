package com.kmmoonlight.gademo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CodeAdapter extends BaseAdapter {

    private List<Code> dataList;

    private LayoutInflater layoutInflater;

    public CodeAdapter(List<Code> dataList, Context context) {
        this.dataList = dataList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CodeViewHolder holder;

        if (convertView == null) {

            holder = new CodeViewHolder();
            convertView = layoutInflater.inflate(R.layout.code_list_item, null);
            holder.tv_code_content = convertView.findViewById(R.id.tv_code_content);
            holder.tv_code_name = convertView.findViewById(R.id.tv_code_name);

            convertView.setTag(holder);
        }else {
            holder = (CodeViewHolder) convertView.getTag();
        }

        holder.tv_code_name.setText(dataList.get(position).getCodeName());
        holder.tv_code_content.setText(dataList.get(position).getCodeContent());

        return convertView;
    }



    static class CodeViewHolder {

        TextView tv_code_name;

        TextView tv_code_content;

    }



}
