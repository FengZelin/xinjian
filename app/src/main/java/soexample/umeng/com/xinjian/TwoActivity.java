package soexample.umeng.com.xinjian;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import soexample.umeng.com.xinjian.adapter.MyAdapter;
import soexample.umeng.com.xinjian.bean.Bean;

public class TwoActivity extends AppCompatActivity implements View.OnClickListener {
    String url = "http://www.zhaoapi.cn/product/getCarts?uid=71";
    private ExpandableListView elc_show_main;
    private CheckBox cb_allCheck_main;
    private TextView btn_allprice_main;
    private Button btn_allNumber_main;
    private MyAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        initView();
        initData();
    }

    private void initData() {
        OkHttpClient build = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(url).build();
        build.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String s = response.body().string();
                runOnUiThread(new Runnable() {


                    private List<Bean.DataBean> data;

                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        Bean bean = gson.fromJson(s, Bean.class);
                        data = bean.getData();
                        adapter = new MyAdapter(TwoActivity.this, data);
                        adapter.setOnCartListChangeListener(new MyAdapter.OnCartListChangeListener() {
                            @Override
                            public void SellerSelectedChange(int groupPosition) {
                                boolean b = adapter.isCurrentSellerAllProductSeleted(groupPosition);
                                adapter.changeCurrentSellerAllProductSelected(groupPosition,!b);
                                adapter.notifyDataSetChanged();
                                refreshAllSelectedAndTotalPriceAndTotalNumber();
                            }

                            @Override
                            public void changeCurrentProductSelected(int groupPosition, int childPosition) {
                                adapter.changeCurrentSelected(groupPosition,childPosition);
                                adapter.notifyDataSetChanged();
                                refreshAllSelectedAndTotalPriceAndTotalNumber();
                            }

                            @Override
                            public void ProductNumberChange(int groupPosition, int childPosition, int number) {
                                adapter.changeCurrentNumber(groupPosition,childPosition,number);
                                adapter.notifyDataSetChanged();
                                refreshAllSelectedAndTotalPriceAndTotalNumber();
                            }
                        });
                        elc_show_main.setAdapter(adapter);
//
                        for (int i = 0; i < data.size(); i++) {
                            elc_show_main.expandGroup(i);

                        }
                    }
                });
            }
        });
    }


    private void initView() {
        elc_show_main = (ExpandableListView) findViewById(R.id.elc_show_main);
        cb_allCheck_main = (CheckBox) findViewById(R.id.cb_allCheck_main);
        btn_allprice_main = (TextView) findViewById(R.id.btn_allprice_main);
        btn_allNumber_main = (Button) findViewById(R.id.btn_allNumber_main);

        btn_allNumber_main.setOnClickListener(this);
    }
    private void  refreshAllSelectedAndTotalPriceAndTotalNumber(){

        boolean allProductsSelected = adapter.isAllProductsSelected();
        cb_allCheck_main.setChecked(allProductsSelected);
//计算总金额
        Double totalPrice = adapter.TotalPrice();
        btn_allprice_main.setText("总价：￥"+totalPrice);
        //计算总数量
        int totalNumber = adapter.TotalNumber();
        btn_allNumber_main.setText("去结算("+totalNumber+")");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_allNumber_main:

                break;
            case R.id.cb_allCheck_main:
                boolean allProductsSelected = adapter.isAllProductsSelected();
                adapter.ProductsSelected(!allProductsSelected);
                adapter.notifyDataSetChanged();
                //刷新底部的方法
                refreshAllSelectedAndTotalPriceAndTotalNumber();
                break;
        }
    }
}
