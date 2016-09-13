package com.zy.tbshoppingcart.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zy.tbshoppingcart.R;
import com.zy.tbshoppingcart.entity.GoodsInfo;
import com.zy.tbshoppingcart.entity.StoreInfo;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 购物车数据适配器
 */
public class ShopcartAdapter extends BaseExpandableListAdapter {

    private List<StoreInfo> groups;
    private Map<String, List<GoodsInfo>> children;
    private Context context;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    public int flag = 0;
    private GroupEdtorListener mListener;

    public GroupEdtorListener getmListener() {
        return mListener;
    }

    public void setmListener(GroupEdtorListener mListener) {
        this.mListener = mListener;
    }

    /**
     * 构造函数
     *
     * @param groups   组元素列表
     * @param children 子元素列表
     * @param context
     */
    public ShopcartAdapter(List<StoreInfo> groups, Map<String, List<GoodsInfo>> children, Context context) {
        this.groups = groups;
        this.children = children;
        this.context = context;
    }

    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        String groupId = groups.get(groupPosition).getId();
        return children.get(groupId).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<GoodsInfo> childs = children.get(groups.get(groupPosition).getId());
        return childs.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final GroupViewHolder gholder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_shopcart_group, null);
            gholder = new GroupViewHolder(convertView);
            convertView.setTag(gholder);
        } else {
            gholder = (GroupViewHolder) convertView.getTag();
        }
        final StoreInfo group = (StoreInfo) getGroup(groupPosition);

        gholder.tvSourceName.setText(group.getName());
        gholder.determineChekbox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v)

            {
                group.setChoosed(((CheckBox) v).isChecked());
                checkInterface.checkGroup(groupPosition, ((CheckBox) v).isChecked());// 暴露组选接口
            }
        });
        gholder.determineChekbox.setChecked(group.isChoosed());
        if (group.isEdtor()) {
            gholder.tvStoreEdtor.setText("完成");
        } else {
            gholder.tvStoreEdtor.setText("编辑");
        }
        gholder.tvStoreEdtor.setOnClickListener(new GroupViewClick(groupPosition, gholder.tvStoreEdtor, group));
        notifyDataSetChanged();
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, View convertView, final ViewGroup parent) {

        final ChildViewHolder cholder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_shopcart_product, null);
//            if(isLastChild&&getChild(groupPosition,childPosition)!=null)
//            {
//                View    v = View.inflate(context, R.layout.child_footer,null);
//                TextView txtFooter = (TextView)v.findViewById(R.id.txtFooter);
//                txtFooter.setText("店铺满99元包邮");
//                if(convertView instanceof ViewGroup){
//                    ((ViewGroup) convertView).addView(v);
//                }
//            }

            cholder = new ChildViewHolder(convertView);
            convertView.setTag(cholder);
        } else {
            cholder = (ChildViewHolder) convertView.getTag();
        }

        if (groups.get(groupPosition).isEdtor() == true) {
            cholder.llEdtor.setVisibility(View.VISIBLE);
            cholder.rlNoEdtor.setVisibility(View.GONE);
        } else {
            cholder.llEdtor.setVisibility(View.GONE);
            cholder.rlNoEdtor.setVisibility(View.VISIBLE);
        }
        final GoodsInfo goodsInfo = (GoodsInfo) getChild(groupPosition, childPosition);


        if(isLastChild&&getChild(groupPosition,childPosition)!=null){
            cholder.stub.setVisibility(View.VISIBLE);
            //  TextView tv= (TextView) cholder.stub.findViewById(R.id.txtFooter);//这里用来动态显示店铺满99元包邮文字内容
        }else{
            cholder.stub.setVisibility(View.GONE);
        }
        if (goodsInfo != null) {


            cholder.tvIntro.setText(goodsInfo.getDesc());
            cholder.tvPrice.setText("￥" + goodsInfo.getPrice() + "");
            cholder.tvNum.setText(goodsInfo.getCount() + "");
            cholder.ivAdapterListPic.setImageResource(goodsInfo.getGoodsImg());
            cholder.tvColorsize.setText("颜色：" + goodsInfo.getColor() + "," + "尺码：" + goodsInfo.getSize() + "瓶/斤");
            SpannableString spanString = new SpannableString("￥" + String.valueOf(goodsInfo.getDiscountPrice()));
            StrikethroughSpan span = new StrikethroughSpan();
            spanString.setSpan(span, 0, String.valueOf(goodsInfo.getDiscountPrice()).length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //避免无限次的appand
            if (cholder.tvDiscountPrice.getText().toString().length() > 0) {
                cholder.tvDiscountPrice.setText("");
            }
            cholder.tvDiscountPrice.append(spanString);
            cholder.tvBuyNum.setText("x" + goodsInfo.getCount());
            cholder.checkBox.setChecked(goodsInfo.isChoosed());
            cholder.checkBox.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    goodsInfo.setChoosed(((CheckBox) v).isChecked());
                    cholder.checkBox.setChecked(((CheckBox) v).isChecked());
                    checkInterface.checkChild(groupPosition, childPosition, ((CheckBox) v).isChecked());// 暴露子选接口
                }
            });
            cholder.tvAdd.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doIncrease(groupPosition, childPosition, cholder.tvNum, cholder.checkBox.isChecked());// 暴露增加接口
                }
            });
            cholder.tvReduce.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doDecrease(groupPosition, childPosition, cholder.tvNum, cholder.checkBox.isChecked());// 暴露删减接口
                }
            });
            //删除 购物车
            cholder.tvGoodsDelete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alert = new AlertDialog.Builder(context).create();
                    alert.setTitle("操作提示");
                    alert.setMessage("您确定要将这些商品从购物车中移除吗？");
                    alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    return;
                                }
                            });
                    alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    modifyCountInterface.childDelete(groupPosition, childPosition);

                                }
                            });
                    alert.show();

                }
            });

        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;

    }





    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param groupPosition 组元素位置
         * @param isChecked     组元素选中与否
         */
        void checkGroup(int groupPosition, boolean isChecked);

        /**
         * 子选框状态改变时触发的事件
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param isChecked     子元素选中与否
         */
        void checkChild(int groupPosition, int childPosition, boolean isChecked);
    }

    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        /**
         * 删减操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        /**
         * 删除子item
         *
         * @param groupPosition
         * @param childPosition
         */
        void childDelete(int groupPosition, int childPosition);
    }

    /**
     * 监听编辑状态
     */
    public interface GroupEdtorListener {
        void groupEdit(int groupPosition);
    }

    /**
     * 使某个组处于编辑状态
     * <p/>
     * groupPosition组的位置
     */
    class GroupViewClick implements OnClickListener {
        private int groupPosition;
        private Button edtor;
        private StoreInfo group;

        public GroupViewClick(int groupPosition, Button edtor, StoreInfo group) {
            this.groupPosition = groupPosition;
            this.edtor = edtor;
            this.group = group;
        }

        @Override
        public void onClick(View v) {
            int groupId = v.getId();
            if (groupId == edtor.getId()) {
                if (group.isEdtor()) {
                    group.setIsEdtor(false);
                } else {
                    group.setIsEdtor(true);

                }
                notifyDataSetChanged();
            }
        }
    }

    /**
     * 组元素绑定器
     */
    static class GroupViewHolder {
        @BindView(R.id.determine_chekbox)
        CheckBox determineChekbox;
        @BindView(R.id.tv_source_name)
        TextView tvSourceName;
        @BindView(R.id.tv_store_edtor)
        Button tvStoreEdtor;

        GroupViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 子元素绑定器
     */
    static class ChildViewHolder {
        @BindView(R.id.check_box)
        CheckBox checkBox;
        @BindView(R.id.iv_adapter_list_pic)
        ImageView ivAdapterListPic;
        @BindView(R.id.tv_intro)
        TextView tvIntro;
        @BindView(R.id.tv_color_size)
        TextView tvColorSize;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_discount_price)
        TextView tvDiscountPrice;
        @BindView(R.id.tv_buy_num)
        TextView tvBuyNum;
        @BindView(R.id.rl_no_edtor)
        RelativeLayout rlNoEdtor;
        @BindView(R.id.tv_reduce)
        TextView tvReduce;
        @BindView(R.id.tv_num)
        TextView tvNum;
        @BindView(R.id.tv_add)
        TextView tvAdd;
        @BindView(R.id.ll_change_num)
        LinearLayout llChangeNum;
        @BindView(R.id.tv_colorsize)
        TextView tvColorsize;
        @BindView(R.id.tv_goods_delete)
        TextView tvGoodsDelete;
        @BindView(R.id.ll_edtor)
        LinearLayout llEdtor;
        @BindView(R.id.stub)
        ViewStub stub;
        ChildViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }
}
