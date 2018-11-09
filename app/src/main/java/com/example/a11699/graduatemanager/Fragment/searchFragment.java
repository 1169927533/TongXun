package com.example.a11699.graduatemanager.Fragment;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.a11699.graduatemanager.Adapter.zhoujihistoryAdapter;
import com.example.a11699.graduatemanager.R;
import com.example.a11699.graduatemanager.SQL.Sql;
import com.example.a11699.graduatemanager.activity.searchInfromationActivity;
import com.example.a11699.graduatemanager.askHttpDao.zhoujiDao;
import com.example.a11699.graduatemanager.askhttpDaoImp.zhoujiDaoImp;
import com.example.a11699.graduatemanager.lei.stationInformation;
import com.example.a11699.graduatemanager.lei.zhoujiinformation;
import com.example.a11699.graduatemanager.lei.studentInfromation;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class searchFragment extends Fragment {
    private EditText search_body;//搜索框
    private ImageView search_back, search_sousuo;//返回
    private RecyclerView search_recycle;//显示界面
    View view;
    //数据库变量
    private Sql helper;
    private SQLiteDatabase db;
    LinearLayoutManager linearLayoutManager;

    List<studentInfromation> liststudent = new ArrayList<>();
    zhoujihistoryAdapter adapter;//适配器

    zhoujiDao zhoujiDao = new zhoujiDaoImp(this);

    public searchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //   view=inflater.inflate(R.layout.fragment_search, container, false);
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_search, container, false);
            initview();
            deleteData();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        return view;
    }

    void initview() {
        helper = new Sql(getContext());
        search_body = view.findViewById(R.id.search_body);
        search_back = view.findViewById(R.id.search_back);
        search_sousuo = view.findViewById(R.id.search_sousuo);
        search_recycle = view.findViewById(R.id.search_recycle);
        zhoujiDao.quanbuzhouji();
        search_sousuo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
// 1. 点击搜索按键后，根据输入的搜索字段进行查询
                    // 注：由于此处需求会根据自身情况不同而不同，所以具体逻辑由开发者自己实现，此处仅留出接口
                    //  if (!(mCallBack == null)){
                    //     mCallBack.SearchAciton(et_search.getText().toString());
                    // }
                    // 2. 点击搜索键后，对该搜索字段在数据库是否存在进行检查（查询）->> 关注1
                    boolean hasData = hasData(search_body.getText().toString().trim());
                    // 3. 若存在，则不保存；若不存在，则将该搜索字段保存（插入）到数据库，并作为历史搜索记录
                   /*if (!hasData) {
                       insertData(search_body.getText().toString().trim());
                       queraData("");
                   }
                   */
                }
                return false;
            }
        });
        search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                //删除数据库
                // helper.getWritableDatabase().execSQL("DROP TABLE IF EXISTS "+ "gra.db");
            }
        });
        search_body.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String tname = search_body.getText().toString();
                queraData(tname);
            }
        });
        /*adapter.setOnclickListener(new zhoujihistoryAdapter.OnclickListener() {
            @Override
            public void OnClick(int position) {
                Intent intent = new Intent(getActivity(), searchInfromationActivity.class);
                intent.putExtra("hid", position);
                startActivity(intent);
            }
        });
        */
    }

    public void show(final List<zhoujiinformation> list) {
        Thread bb=new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < list.size(); i++) {

                    insertData(list.get(i).getTitle().toString().trim(), list.get(i).getCid().toString().trim());
                }

            }
        });
       bb.start();
    }

    /**
     * 向数据库插入数据
     *
     * @param tempName
     */
    void insertData(String tempName, String cid) {
        db = helper.getWritableDatabase();//打开数据库进行读写操作
        db.execSQL("insert into records(name,sid) values('" + tempName + "','" + cid + "')");
        db.close();
    }

    /**
     * 查找数据
     *
     * @param namee
     */
    private void queraData(String namee) {
        //模糊查询
        db = helper.getReadableDatabase();
        boolean d = hasData(namee);
        if (d) {
            Cursor cursor = db.rawQuery("select id as _id,name,sid from records where name like '%" + namee + "%' order by id desc ", null);
            cursor.moveToFirst();
            liststudent = new ArrayList<>();
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
             //  Log.i("ceshi",name+" "+cursor.getColumnIndex("sid"));
              String s=cursor.getString(cursor.getColumnIndex("sid"));
               // String cid = cursor.getString(cursor.getColumnIndex("sid"));
                studentInfromation studentInfromation = new studentInfromation(name,s);
                liststudent.add(studentInfromation);
            } while (cursor.moveToNext());
            adapter = new zhoujihistoryAdapter(getContext(), liststudent);
            linearLayoutManager = new LinearLayoutManager(getContext());
            search_recycle.setLayoutManager(linearLayoutManager);
            search_recycle.setAdapter(adapter);
            adapter.setOnclickListener(new zhoujihistoryAdapter.OnclickListener() {
                @Override
                public void OnClick(int position) {
                    Intent intent=new Intent(getActivity(),searchInfromationActivity.class);

                    intent.putExtra("hid",String.valueOf(position));
                    startActivity(intent);
                }
            });
            adapter.notifyDataSetChanged();
        }
        db.close();
    }

    private void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from records");
        /**query()、rawQuery()	查询数据库
         * insert()	插入数据
         delete()	删除数据
         *execSQL()	可进行增删改操作, 不能进行查询操作
         */
        db.close();

    }

    /**
     * 关注3
     * 检查数据库中是否已经有该搜索记录
     */
    private boolean hasData(String tempName) {
        // 从数据库中Record表里找到name=tempName的id
        Cursor cursor = db.rawQuery("select id as _id,name from records where name like '%" + tempName + "%' order by id desc ", null);

        //  判断是否有下一个
        return cursor.moveToNext();
    }


}





















