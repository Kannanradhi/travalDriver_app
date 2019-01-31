package com.cabily.cabilydriver.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.app.sqliteDb.RegistrationDB;
import com.cabily.cabilydriver.Pojo.DriverDocumentPojo;
import com.cabily.cabilydriver.R;
import com.cabily.cabilydriver.Utils.SessionManager;

import java.util.ArrayList;

/**
 * Created by user144 on 7/3/2018.
 */

public class DriverRegsitrationDocumentListAdapter extends BaseAdapter {

    private Context context;
    LayoutInflater inflater;

    public SessionManager session;
    private ArrayList<DriverDocumentPojo> docList;
    private DriverDocumentSelect driverdocumentselect;
    private ArrayList<String> documentListId;
    private ArrayList<String> documentListName;
    RegistrationDB db = null;
    String flag = "";
    Spannable wordtoSpan;

    public DriverRegsitrationDocumentListAdapter(Context context, ArrayList<DriverDocumentPojo> docList, String flag, DriverDocumentSelect driverdocumentselect) {
        this.context = context;
        this.docList = docList;
        System.out.println("====documentlist====" + docList.toString() + flag);
        this.flag = flag;
        this.driverdocumentselect = driverdocumentselect;
        inflater = LayoutInflater.from(context);
        db = new RegistrationDB(context);
        documentListId = new ArrayList<String>();
        documentListName = new ArrayList<String>();
        documentListId = db.getAllID();
        session = new SessionManager(context);
    }

    @Override
    public int getCount() {
        return docList.size();
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
    public int getViewTypeCount() {
        return 1;
    }


    public class ViewHolder {
        TextView txtTitle, pco_tv1, expirytext;
        ImageView imgIcon;
        LinearLayout expirylayout;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = inflater.inflate(R.layout.driverregistration_documentsingle, parent, false);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) view.findViewById(R.id.pco_tv);
            holder.imgIcon = (ImageView) view.findViewById(R.id.pco_add);
            holder.pco_tv1 = (TextView) view.findViewById(R.id.pco_tv1);
            holder.expirylayout = (LinearLayout) view.findViewById(R.id.expirylayout);
            holder.expirytext = (TextView) view.findViewById(R.id.expirytext);
            view.setTag(holder);


        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        try {
            if (flag.equalsIgnoreCase("1")) {


                if (docList.get(position).getImageadded().equals("") || docList.get(position).getImageadded().equals(" ")) {
                    holder.imgIcon.setImageResource(R.drawable.plus1);
                } else {
                    holder.imgIcon.setImageResource(R.drawable.tick);
                }


            } else {
                holder.expirytext.setVisibility(View.VISIBLE);

                if (documentListId.size() > 0) {
                    documentListName = db.getAllName();
                    if (!documentListName.get(position).equalsIgnoreCase("")) {
                        if (docList.get(position).getDocument_status().equalsIgnoreCase("0") && !documentListName.get(position).equalsIgnoreCase("")) {
                            holder.expirytext.setText("Unverified");
                            holder.expirytext.setBackgroundColor(Color.parseColor("#42bcf4"));
                            holder.imgIcon.setVisibility(View.GONE);
                        } else if (docList.get(position).getDocument_status().equalsIgnoreCase("1") && !documentListName.get(position).equalsIgnoreCase("")) {
                            holder.expirytext.setText("Unverified");
                            holder.expirytext.setBackgroundColor(Color.parseColor("#0AD7AF"));
                        } else if (docList.get(position).getDocument_status().equalsIgnoreCase("2") && !documentListName.get(position).equalsIgnoreCase("")) {
                            holder.expirytext.setText("Unverified");
                            holder.expirytext.setBackgroundColor(Color.parseColor("#062A40"));
                        } else if (docList.get(position).getDocument_status().equalsIgnoreCase("3") && !documentListName.get(position).equalsIgnoreCase("")) {
                            holder.expirytext.setText("Unverified");
                            holder.expirytext.setBackgroundColor(Color.parseColor("#000000"));
                        } else if (docList.get(position).getDocument_status().equalsIgnoreCase("4") && !documentListName.get(position).equalsIgnoreCase("")) {
                            holder.expirytext.setText("Unverified");
                            holder.expirytext.setBackgroundColor(Color.parseColor("#0AD7AF"));
                        }
                    } else {
                        if (docList.get(position).getDocument_status().equalsIgnoreCase("0")) {
                            holder.expirytext.setVisibility(View.GONE);
                            holder.expirytext.setBackgroundColor(Color.parseColor("#42bcf4"));
                            holder.imgIcon.setImageResource(R.drawable.plus1);
                        } else if (docList.get(position).getDocument_status().equalsIgnoreCase("1")) {
                            holder.expirytext.setText("Verified");
                            holder.expirytext.setBackgroundColor(Color.parseColor("#0AD7AF"));
                        } else if (docList.get(position).getDocument_status().equalsIgnoreCase("2")) {
                            holder.expirytext.setText("Unverified");
                            holder.expirytext.setBackgroundColor(Color.parseColor("#062A40"));
                        } else if (docList.get(position).getDocument_status().equalsIgnoreCase("3")) {
                            // holder.expirytext.setText("Expiring Soon");
                            holder.expirytext.setText("Rejected");
                            holder.expirytext.setBackgroundColor(Color.parseColor("#FFD34D"));
                        } else if (docList.get(position).getDocument_status().equalsIgnoreCase("4")) {
                            holder.expirytext.setText("Expired");
                            holder.expirytext.setBackgroundColor(Color.parseColor("#0AD7AF"));
                        }
                    }
                }
            }

            //holder.txtTitle.setText(docList.get(position).getName().trim()+ Html.fromHtml(context.getResources().getString(R.string.dottext)));
            holder.pco_tv1.setVisibility(View.GONE);
            if (docList.get(position).getIs_req().equalsIgnoreCase("Yes")) {
                String spntring = docList.get(position).getName().trim() + "*";
                wordtoSpan = new SpannableString(spntring);
                wordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), spntring.length() - 1, spntring.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.txtTitle.setText(wordtoSpan);

                //  holder.pco_tv1.setVisibility(View.VISIBLE);
            } else {
                holder.txtTitle.setText(docList.get(position).getName().trim());

                //holder.pco_tv1.setVisibility(View.GONE);
            }




           /* holder.imgIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flag.equalsIgnoreCase("1")) {
                        driverdocumentselect.OnSelectedDocument(position);
                    }
                }
            });*/
            holder.expirylayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flag.equalsIgnoreCase("1")) {
                        driverdocumentselect.OnSelectedDocument(position);
                    }
                    if (flag.equalsIgnoreCase("2")) {
                        driverdocumentselect.OnSelectedDocument(position);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Exception", e.getMessage() + e.toString());
            Log.e("Exception1", e.getMessage() + e.toString());
        }

        return view;
    }

    public interface DriverDocumentSelect {

        void OnSelectedDocument(int Position);

    }

}