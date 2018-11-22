package soexample.umeng.com.xinjian.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import soexample.umeng.com.xinjian.MyAddSubView;
import soexample.umeng.com.xinjian.R;
import soexample.umeng.com.xinjian.bean.Bean;

/**
 * date:2018/11/22
 * author:冯泽林(asus)
 * function:
 */
public class MyAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<Bean.DataBean> list;

    public MyAdapter(Context context, List<Bean.DataBean> list) {
        mContext = context;
        this.list = list;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return list.get(i).getList().size();
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int i, boolean b, View view, ViewGroup viewGroup) {
        GroupViewHolder holder;
        if(view==null){
            holder = new GroupViewHolder();
            view=View.inflate(mContext,R.layout.item,null);
            holder.cb_item=view.findViewById(R.id.cb_item);
            holder.tv_bt_group=view.findViewById(R.id.tv_bt_group);
            view.setTag(holder);
        }else{
            holder= (GroupViewHolder) view.getTag();
        }
        Bean.DataBean dataBean = list.get(i);
        holder.tv_bt_group.setText(dataBean.getSellerName());
        boolean b1 = isCurrentSellerAllProductSeleted(i);
        holder.cb_item.setChecked(b1);
        holder.cb_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCartListChangeListener != null) {
                    onCartListChangeListener.SellerSelectedChange(i);
                }
            }
        });
        return view;
    }
    public void changeCurrentSellerAllProductSelected(int position,boolean b){
        List<Bean.DataBean.ListBean> data  = list.get(position).getList();
        for (int i = 0; i < data.size(); i++) {
            data.get(i).setSelected(b ? 1 : 0);

        }
    }
//    点击按钮   用来判断外层的Selected值 如果是0 不选中 否则选中
    public boolean isCurrentSellerAllProductSeleted(int position){
        List<Bean.DataBean.ListBean> data = list.get(position).getList();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getSelected() == 0) {
                return false;
            }
        }
        return true;
    }
//    外层的 holder
    public static class GroupViewHolder {

        public CheckBox cb_item;
        public TextView tv_bt_group;
    }
    @Override
    public View getChildView(final int i, final int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildViewHolder holder;
        if(view==null){holder=new ChildViewHolder();
        view= View.inflate(mContext,R.layout.item2,null);
        holder.add_view_child=view.findViewById(R.id.add_view_child);
        holder.iv_tupian_child=view.findViewById(R.id.iv_tupian_child);
        holder.jiage_child=view.findViewById(R.id.jiage_child);
        holder.zcb__item=view.findViewById(R.id.zcb_child_item);
        holder.tv_biaoti_child=view.findViewById(R.id.tv_biaoti_child);
        view.setTag(holder);
        }else{
            holder= (ChildViewHolder) view.getTag();
        }
        Bean.DataBean.ListBean listBean = list.get(i).getList().get(i1);
//        商品标题
        holder.tv_biaoti_child.setText(listBean.getTitle());
//        商品图片
        String images = listBean.getImages();
        String[] split = images.split("\\|");
        Glide.with(mContext).load(split[0]).into(holder.iv_tupian_child);
//        商品价格
        holder.jiage_child.setText("$"+listBean.getPrice());
//        商品点击
        holder.zcb__item.setChecked(listBean.getSelected()==1);
        holder.zcb__item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCartListChangeListener.changeCurrentProductSelected(i,i1);
            }
        });
        holder.add_view_child.setOnNumberChangeListener(new MyAddSubView.OnNumberChangeListener() {
            @Override
            public void OnNumberChange(int number) {
                onCartListChangeListener.ProductNumberChange(i,i1,number);
            }
        });
        return view;
    }
    public void changeCurrentNumber(int i,int i1,int number){
        Bean.DataBean.ListBean listBean = list.get(i).getList().get(i1);
        listBean.setNum(number);
    }
    public void changeCurrentSelected(int i,int i1){
        Bean.DataBean.ListBean listBean = list.get(i).getList().get(i1);
        listBean.setSelected(listBean.getSelected()==0 ? 1:0);

    }
    public double TotalPrice(){
        double totalprice=0;
        for(int i=0;i<list.size();i++){
            List<Bean.DataBean.ListBean> data =list.get(i).getList();
            for(int j=0;j<data.size();j++){
                //只有选中采取计算
                if(data.get(j).getSelected()==1){
                    double price = data.get(j).getPrice();
                    int num = data.get(j).getNum();
                    totalprice +=price*num;
                }
            }
        }
        return totalprice;
    }

    public int TotalNumber(){
        int totalNumber=0;
        for(int i=0;i<list.size();i++){
            List<Bean.DataBean.ListBean> data = list.get(i).getList();
            for(int j=0;j<data.size();j++){
                //只有选中采取计算
                if(data.get(j).getSelected()==1){
                    int num = data.get(j).getNum();
                    totalNumber +=num;
                }
            }
        }
        return totalNumber;
    }

    public boolean isAllProductsSelected() {
        for (int i = 0; i < list.size(); i++) {
            List<Bean.DataBean.ListBean> data = list.get(i).getList();
            for (int j = 0; j < data.size(); j++) {
                if (data.get(j).getSelected() == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void ProductsSelected(boolean b){
        for(int i=0;i<list.size();i++){
            List<Bean.DataBean.ListBean> data = this.list.get(i).getList();
            for(int j=0;j<data.size();j++){
                data.get(j).setSelected(b ? 1:0);
            }
        }
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
//    内层的数据
    public static class ChildViewHolder {

        public CheckBox zcb__item;
        public ImageView iv_tupian_child;
        public TextView tv_biaoti_child;
        public TextView jiage_child;
        public MyAddSubView add_view_child;
    }


//    接口回调
    OnCartListChangeListener onCartListChangeListener;

    public void setOnCartListChangeListener(OnCartListChangeListener onCartListChangeListener) {
        this.onCartListChangeListener = onCartListChangeListener;
    }

    public interface OnCartListChangeListener {
        void SellerSelectedChange(int groupPosition);

        void changeCurrentProductSelected(int groupPosition, int childPosition);

        void ProductNumberChange(int groupPosition, int childPosition, int number);
    }
}
