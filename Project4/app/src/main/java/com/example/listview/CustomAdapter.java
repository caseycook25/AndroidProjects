package com.example.listview;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Cook01 on 4/4/2017.
 */

public class CustomAdapter extends BaseAdapter {

    Context context;
    private List<BikeData> myList;
    private Activity activity;
    private 				int layoutId;
    public LayoutInflater inflater;
    public static int position;
    public int pos;

    public static final int COMPANY = 0;
    public static final int MODEL = 1;
    public static final int LOCATION = 3;
    public static final int PRICE = 2;
    public static final int PICTURE = 3;
    public String pic;
    private ImageView imgView;
    private String url = Activity_ListView._url;

    private static final String TAG = "CustomAdapter";


    /**
     * @param activity
     *            what the view is attached to
     * @param layoutId
     *            defines how each row looks
     *
     */
    public CustomAdapter(Activity activity, int layoutId, List<BikeData> bikeList, ImageView imgView) {
        this.activity = activity;
        this.myList = bikeList;
        this.layoutId = layoutId;
        this.imgView = imgView;

        if (activity != null)
            this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return (myList != null) ? myList.size() : 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return (myList != null) ? myList.get(arg0) : null;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    public void sortList(int iSort) {
        switch (iSort) {
            case COMPANY:
                Collections.sort(myList, new MyDataIsCompanyComparator());
                break;
            case LOCATION:
                Collections.sort(myList, new MyDataIsLocationComparator());
                break;
            case PRICE:
                Collections.sort(myList, new MyDataIsPriceComparator());
                break;
            case MODEL:
                Collections.sort(myList, new MyDataIsModelComparator());
                break;
            default:
                Collections.sort(myList, new MyDataIsCompanyComparator());
                break;
        }
        notifyDataSetChanged();
    }

    public class ViewHolder {
        public int position;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parentView) {
        ViewHolder holder = new ViewHolder();
        holder.position = pos;
        this.pos = pos;
        View view = null;
        if (convertView == null) {
            view = inflater.inflate(layoutId, parentView, false);
        } else {
            view = convertView;
        }



        // set the description
        TextView desc = (TextView) view.findViewById(R.id.Description);
        desc.setText(myList.get(pos).Description);

        // set the model
        TextView model = (TextView) view.findViewById(R.id.Model);
        model.setText(myList.get(pos).Model);

        // set the price
        TextView price = (TextView) view.findViewById(R.id.Price);
        price.setText(Double.toString(myList.get(pos).Price));


        pic = myList.get(pos).Picture;
        imgView = (ImageView)view.findViewById(R.id.imageView1);

        DownloadImageTask task = new DownloadImageTask(pic, imgView, pos, holder);
        task.execute(url + "/" + pic);

        return view;
    }
}
