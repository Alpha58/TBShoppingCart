***做商城类电商app购物车确实一直是一个难点，为什么难呢？***   

主要原因呢是他里面的逻辑复杂，然后 百度的资源好像都不太理想，好多就是一个简单的listView来实现根本就达不到开发的需求。然后 一般都涉及到了店铺概念，就不再是一个简单listView能解决的，如果用2个listView来嵌套的话涉及到批量操作和商品的勾选以及单个商品的或整个店铺商品的操作，那样逻辑变复杂了，然后动不动要用map去保存勾选状态，时不时出现position的错位和数组下标越界等，而且性能感觉不太好。
这里我使用的ExpandableListView，然后要完全实现淘宝购物车也是有难度的，由于能力也是有限这里也是参考了一些人的然后搞了好几天才大致实现了淘宝购物车功能。   

我的博客[http://blog.csdn.net/u013278099/article/details/50822074](http://blog.csdn.net/u013278099/article/details/50822074)
####本篇的效果：（如下4张图）  

![pic1](https://github.com/zilianliuxue/TBShoppingCart/blob/master/pic1.gif)     
![pic2](https://github.com/zilianliuxue/TBShoppingCart/blob/master/pic2.gif)  
![pic3](https://github.com/zilianliuxue/TBShoppingCart/blob/master/pic3.gif)  
![pic4](https://github.com/zilianliuxue/TBShoppingCart/blob/master/pic4.gif)  
###实现思路
主布局就是一个ExpandableListView,然后top的title显示购物车的商品数量，当删除某个商品需动态更新，右上角编辑按钮改变地步遮罩层的布局并且执行相关的操作，bottom是一个遮罩层编辑时显示删除不编辑时可以去结算。然后child的布局也是通过group的编辑状态来显示不同的布局，编辑状态下需要改变商品的数量和移除商品。
###实例代码演示
先来购物车主界面：
```java
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical" >
    <LinearLayout
        android:id="@+id/top_bar"
        android:layout_width="match_parent"
        android:layout_height="48dp"
        android:background="@drawable/topbar_background"
        android:orientation="vertical" >

        <RelativeLayout
            android:layout_width="match_parent"
            android:layout_height="48dp"
           android:background="@android:color/transparent"
            android:orientation="vertical" >
            <ImageView
                android:id="@+id/back"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_alignParentLeft="true"
                android:layout_gravity="center_vertical"
                android:padding="12dp"
                android:src="@mipmap/topbar_up" />
            <TextView
                android:id="@+id/title"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:gravity="center"
                android:minHeight="48dp"
                android:text="购物车"
                android:textColor="#1a1a1a"
                android:textSize="16sp" />
            <TextView
                android:id="@+id/subtitle"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_alignParentRight="true"
                android:layout_marginRight="40dp"
                android:gravity="center"
                android:minHeight="48dp"
                android:text="编辑"
                android:textColor="#1a1a1a"
                android:textSize="14sp"
                android:visibility="visible" />
        </RelativeLayout>
    </LinearLayout>
    <ExpandableListView
        android:id="@+id/exListView"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:childIndicator="@null"
        android:groupIndicator="@null" >
    </ExpandableListView>

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="50dp"
            android:gravity="center_vertical"
            android:orientation="horizontal" >

            <CheckBox
                android:id="@+id/all_chekbox"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:layout_gravity="center_vertical"
                android:layout_marginRight="4dp"
                android:button="@drawable/check_box_bg"
                android:checkMark="?android:attr/listChoiceIndicatorMultiple"
                android:gravity="center"
                android:minHeight="64dp"
                android:layout_marginLeft="10dp"
                android:text="全选"
                android:textAppearance="?android:attr/textAppearanceLarge"
                />
        <LinearLayout
                android:id="@+id/ll_info"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="4"
                >
                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="vertical"
                    android:layout_marginRight="20dp"
                    android:layout_weight="1"
                    >
                    <LinearLayout
           android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                        android:orientation="horizontal"
                        android:gravity="right"
                        >
                        <TextView
                       android:layout_width="wrap_content"
                      android:layout_height="wrap_content"
                           android:layout_marginLeft="5dp"
                            android:text="合计:"
                            android:textSize="18sp"
                            android:textStyle="bold" />
                        <TextView
                          android:id="@+id/tv_total_price"
                       android:layout_width="wrap_content"
                     android:layout_height="wrap_content"
                            android:text="￥0.00"
                     android:textColor="@color/orangered"
                            android:textSize="16sp"
                            android:textStyle="bold" />
                    </LinearLayout>
                    <TextView
                       android:layout_width="match_parent"
                      android:layout_height="wrap_content"
                        android:text="不含运费"
                        android:gravity="right"
                        android:textColor="@color/gray"
                        android:textSize="16sp"
                        android:textStyle="bold" />
                </LinearLayout>
            <TextView
                android:id="@+id/tv_go_to_pay"
                android:layout_width="match_parent"
                android:layout_height="match_parent"
                android:layout_weight="3"
                android:background="@color/orange"
                android:clickable="true"
                android:gravity="center"
                android:text="结算(0)"
                android:textColor="#FAFAFA"
                />
            </LinearLayout>
            <LinearLayout
                android:id="@+id/ll_shar"
                android:layout_width="0dp"
                android:layout_height="match_parent"
                android:layout_weight="4"
                android:orientation="horizontal"
                android:visibility="gone"
                >
                <TextView
                    android:id="@+id/tv_share"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:gravity="center"
                    android:layout_weight="1"
                    android:layout_marginLeft="5dp"
                    android:text="分享宝贝"
                    android:textColor="@color/white"
                    android:background="@color/orange"
                    android:textSize="16sp"
                    android:layout_marginRight="5dp"
                    android:textStyle="bold" />
                <TextView
                    android:id="@+id/tv_save"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:layout_weight="1"
                    android:gravity="center"
                    android:text="移到收藏夹"
                    android:background="@color/orange"
                    android:textColor="@color/white"
                    android:layout_marginRight="5dp"
                    android:textSize="16sp"
                    android:textStyle="bold" />
                <TextView
                    android:id="@+id/tv_delete"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:layout_weight="1"
                    android:background="@color/crimson"
                    android:clickable="true"
                    android:gravity="center"
                    android:text="删除"
                    android:textColor="#FAFAFA"
                    />
            </LinearLayout>
        </LinearLayout>
</LinearLayout>
```
大致的效果图：

![group](https://github.com/zilianliuxue/TBShoppingCart/blob/master/6.BX.S.CB.6MOHSY.GU.png)

接下来我们来看child的 布局：

```java
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical" >

    <View
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:background="#CCCCCC" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@color/page_backgroup"
        android:orientation="horizontal" >

        <CheckBox
            android:id="@+id/check_box"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center_vertical"
            android:layout_marginLeft="10dp"
            android:layout_marginRight="4dp"
            android:button="@drawable/check_box_bg"
            android:checkMark="?android:attr/listChoiceIndicatorMultiple"
            android:gravity="center"
            android:minHeight="64dp"
            android:minWidth="32dp"
            android:textAppearance="?android:attr/textAppearanceLarge"
            android:visibility="visible" />

        <ImageView
            android:id="@+id/iv_adapter_list_pic"
            android:layout_width="85dp"
            android:layout_height="85dp"
            android:layout_marginBottom="15dp"
            android:layout_marginTop="13dp"
            android:scaleType="centerCrop"
            android:src="@drawable/goods1" />

        <RelativeLayout
            android:id="@+id/rl_no_edtor"
            android:layout_width="wrap_content"
            android:layout_height="match_parent"
            android:layout_marginLeft="13dp"
            >

            <TextView
                android:id="@+id/tv_intro"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginRight="10dp"
                android:layout_marginTop="20dp"
                android:ellipsize="end"
                android:maxLines="2"
                android:text="第八号当铺美女一枚"
                android:textColor="@color/grey_color1"
                android:textSize="@dimen/txt_14" />
            <TextView
                android:id="@+id/tv_color_size"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="颜色：黑色；尺码：29"
                android:textColor="@color/gray"
                android:layout_centerVertical="true"
                android:layout_alignParentLeft="true"
                android:layout_alignParentStart="true" />
            <RelativeLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:orientation="horizontal"
                android:layout_alignParentBottom="true"
                android:layout_alignParentLeft="true"
               android:layout_marginBottom="20dp"
                android:layout_alignParentStart="true">

                <TextView
                    android:id="@+id/tv_price"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_centerVertical="true"
                    android:singleLine="true"
                    android:text="￥ 308.00"
                    android:textColor="@color/orange_color"
                    android:textSize="@dimen/txt_14"
                    android:textStyle="bold" />

                <TextView
                    android:id="@+id/tv_discount_price"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_centerVertical="true"
                    android:layout_marginLeft="10dp"
                    android:layout_toRightOf="@+id/tv_price"
                    android:text=""
                    android:textColor="@color/gray"
                    android:textSize="@dimen/txt_10"
                    />
                <TextView
                    android:id="@+id/tv_buy_num"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_centerVertical="true"
                    android:layout_marginRight="20dp"
                    android:layout_alignParentRight="true"
                    android:text="X 1"
                    android:textColor="@color/gray"
                    android:textSize="@dimen/txt_10"
                    />
            </RelativeLayout>
        </RelativeLayout>
        <LinearLayout
            android:id="@+id/ll_edtor"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:layout_marginLeft="13dp"
            android:visibility="gone"
            android:orientation="horizontal">
            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:orientation="vertical">
                <LinearLayout
                    android:id="@+id/ll_change_num"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_alignParentLeft="true"
                    android:layout_alignParentTop="true"
                    android:layout_centerVertical="true"
                    android:layout_marginTop="10dp"
                    android:orientation="horizontal" >

                    <TextView
                        android:id="@+id/tv_reduce"
                        android:layout_width="35dp"
                        android:layout_height="35dp"
                            android:background="@drawable/text_angle_gray"
                        android:gravity="center"
                        android:text="一"
                        android:textColor="@color/grey_color1"
                        android:textSize="@dimen/txt_12" />

                    <TextView
                        android:id="@+id/tv_num"
                        android:layout_width="35dp"
                        android:layout_height="35dp"
                       android:background="@drawable/text_angle"
                        android:gravity="center"
                        android:singleLine="true"
                        android:text="1"
android:textColor="@color/grey_color1"
                        android:textSize="@dimen/txt_12" />
                    <TextView
                        android:id="@+id/tv_add"
                        android:layout_width="35dp"
                        android:layout_height="35dp"
android:background="@drawable/text_angle_right"
                        android:gravity="center"
                        android:text="+"
                   android:textColor="@color/grey_color1"
                        android:textSize="@dimen/txt_12" />
                </LinearLayout>
                <TextView
                    android:id="@+id/tv_colorsize"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="10dp"
                    android:text="颜色：黑色；尺码：29"
                    android:layout_gravity="left"
                    android:textColor="@color/gray"/>
            </LinearLayout>
           <TextView
               android:id="@+id/tv_goods_delete"
               android:layout_width="match_parent"
               android:layout_height="match_parent"
               android:layout_weight="3"
               android:text="删除"
               android:background="@color/orange"
               android:gravity="center"
               android:layout_gravity="center"
               android:textColor="@color/white"/>
        </LinearLayout>
    </LinearLayout>
</LinearLayout>
```

大致效果就是这样的：

![child](https://github.com/zilianliuxue/TBShoppingCart/blob/master/J.J1EY.MPOMR.X.Y2MMW.png)

然后我们就来重点的讲代码了：

####**编辑按钮**的点击处理：

```java
  private int flag = 0;//设置按钮点击的标志位

  //group的按钮通过flag动态为（编辑/完成）
 gholder.store_edtor.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
               //回掉接口通过groupPosition处理group的编辑状态
                    mListener.groupEdit(groupPosition);
                    if (flag == 0) {
                        group.setIsEdtor(true);
                        gholder.store_edtor.setText("完成");
                    } else if (flag == 1) {
                        group.setIsEdtor(false);
                        gholder.store_edtor.setText("编辑");
                    }
                    flag = (flag + 1) % 2;//其余得到循环执行上面2个不同的功能
                }
            });
```

###接口回调的设置

####**checkBox**的多选全选反选接口的回调：

```java
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
        public void checkGroup(int groupPosition, boolean isChecked);

        /**
         * 子选框状态改变时触发的事件
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param isChecked     子元素选中与否
         */
        public void checkChild(int groupPosition, int   childPosition, boolean isChecked);
    }
```

通过监听checkBox的状态设置group和全选的checkBox的勾选状态，便于删除和去结算。

####child商品的数量增减和删除接口回调

```java
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
        public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);
        /**
         * 删减操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);
        /**
         * 删除子item
         * @param groupPosition
         * @param childPosition
         */
        public void childDelete(int groupPosition,int childPosition);
    }
```

通过监听child的商品数量的变化，从而计算 购物车结算时的金额和数量，当店铺的商品删除完的时候顺便把店铺也移除出购物车。

####group的编辑状态的回调

```java
  /**
     * 监听group编辑状态
     */
    public interface GroupEdtorListener{
        public void groupEdit(int groupPosition);
    }
```

通过监听group的状态，动态 设置child的布局并且进行相关的添加和减少商品，并且还能移除商品出购物车。

####相关购物车的操作

#####购物车的删除

```java
 /**
     * 删除操作<br>
     * 1.不要边遍历边删除，容易出现数组越界的情况<br>
     * 2.现将要删除的对象放进相应的列表容器中，待遍历完后，以removeAll的方式进行删除
     */
    protected void doDelete() {
        List<StoreInfo> toBeDeleteGroups = new ArrayList<StoreInfo>();// 待删除的组元素列表
        for (int i = 0; i < groups.size(); i++) {
            StoreInfo group = groups.get(i);
            if (group.isChoosed()) {
                toBeDeleteGroups.add(group);
            }
            List<GoodsInfo> toBeDeleteProducts = new ArrayList<GoodsInfo>();// 待删除的子元素列表
            List<GoodsInfo> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++) {
                if (childs.get(j).isChoosed()) {
                    toBeDeleteProducts.add(childs.get(j));
                }
            }
            childs.removeAll(toBeDeleteProducts);
        }
        groups.removeAll(toBeDeleteGroups);
        selva.notifyDataSetChanged();
        calculate();
    }
```

#####购物车数量增加

```java
 @Override
    public void doIncrease(int groupPosition, int childPosition,
                           View showCountView, boolean isChecked) {
        GoodsInfo product = (GoodsInfo) selva.getChild(groupPosition,
                childPosition);
        int currentCount = product.getCount();
        currentCount++;
        product.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        selva.notifyDataSetChanged();
        calculate();
    }
```

#####购物车商品数量减少

```java
  @Override
    public void doDecrease(int groupPosition, int childPosition,View showCountView, boolean isChecked) {
        GoodsInfo product = (GoodsInfo) selva.getChild(groupPosition,
                childPosition);
        int currentCount = product.getCount();
        if (currentCount == 1)
            return;
        currentCount--;
        product.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        selva.notifyDataSetChanged();
        calculate();
    }
```

#####购物车物品的勾选状态变化

```java
  @Override
    public void checkGroup(int groupPosition, boolean isChecked) {//判断group是否勾选
        StoreInfo group = groups.get(groupPosition);
        List<GoodsInfo> childs = children.get(group.getId());
        for (int i = 0; i < childs.size(); i++) {
            childs.get(i).setChoosed(isChecked);
        }
        if (isAllCheck())
            allChekbox.setChecked(true);
        else
            allChekbox.setChecked(false);
        selva.notifyDataSetChanged();
        calculate();
    }
    @Override
    public void checkChild(int groupPosition, int childPosiTion, boolean isChecked) {//判断child是否勾选
        boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
        StoreInfo group = groups.get(groupPosition);
        List<GoodsInfo> childs = children.get(group.getId());
        for (int i = 0; i < childs.size(); i++) {
            // 不全选中
            if (childs.get(i).isChoosed() != isChecked) {
                allChildSameState = false;
                break;
            }
        }
        //获取店铺选中商品的总金额
        if (allChildSameState) {
            group.setChoosed(isChecked);// 如果所有子元素状态相同，那么对应的组元素被设为这种统一状态
        } else {
            group.setChoosed(false);// 否则，组元素一律设置为未选中状态
        }
        if (isAllCheck()) {
            allChekbox.setChecked(true);// 全选
        } else {
            allChekbox.setChecked(false);// 反选
        }
        selva.notifyDataSetChanged();
        calculate();
    }
     private boolean isAllCheck() {//是否全选
        for (StoreInfo group : groups) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }
    /**
     * 全选与反选
     */
    private void doCheckAll() {
groups.get(i).setChoosed(allChekbox.isChecked());
            StoreInfo group = groups.get(i);
            List<GoodsInfo> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++) {
childs.get(j).setChoosed(allChekbox.isChecked());
            }
        }
        selva.notifyDataSetChanged();
        calculate();
    }
```

#####购物车结算金额的计算

```java
/**
     * 统计操作<br>
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
     * 3.给底部的textView进行数据填充
     */
    private void calculate() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < groups.size(); i++) {
            StoreInfo group = groups.get(i);
            List<GoodsInfo> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++) {
                GoodsInfo product = childs.get(j);
                if (product.isChoosed()) {
                    totalCount++;
  totalPrice += product.getPrice() * product.getCount();
                }
            }
        }
        tvTotalPrice.setText("￥" + totalPrice);
        tvGoToPay.setText("去支付(" + totalCount + ")");
    }
```

好了，前面主要的 逻辑代码都贴的差不多了，确实也看的比较琐碎，不坑大家了直接上2个 完整类的代码：

```java
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

/**
 * 购物车数据适配器
 */
public class ShopcartAdapter extends BaseExpandableListAdapter {
    private List<StoreInfo> groups;
    private Map<String, List<GoodsInfo>> children;
    private Context context;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;
    private int flag = 0;
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
            gholder = new GroupViewHolder();
            convertView = View.inflate(context, R.layout.item_shopcart_group, null);
            gholder.cb_check = (CheckBox) convertView.findViewById(R.id.determine_chekbox);
            gholder.tv_group_name = (TextView) convertView.findViewById(R.id.tv_source_name);
            gholder.store_edtor = (Button) convertView.findViewById(R.id.tv_store_edtor);
            convertView.setTag(gholder);
        } else {
            gholder = (GroupViewHolder) convertView.getTag();
        }
        final StoreInfo group = (StoreInfo) getGroup(groupPosition);
        if (group != null) {
            gholder.tv_group_name.setText(group.getName());
            gholder.cb_check.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v)

                {
                    group.setChoosed(((CheckBox) v).isChecked());
                    checkInterface.checkGroup(groupPosition, ((CheckBox) v).isChecked());// 暴露组选接口
                }
            });
            gholder.cb_check.setChecked(group.isChoosed());
            gholder.store_edtor.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //暴露group的编辑状态接口
                    mListener.groupEdit(groupPosition);
                    if (flag == 0) {
                        group.setIsEdtor(true);
             gholder.store_edtor.setText("完成");
                    } else if (flag == 1) {
                        group.setIsEdtor(false);
                        gholder.store_edtor.setText("编辑");
                    }
      flag = (flag + 1) % 2;//其余得到循环执行上面2个不同的功能
                }
            });
        } else {
            groups.remove(groupPosition);
        }
        return convertView;
    }
    /**
    *childView的view
    */
    @Override
    public View getChildView(final int groupPosition, final int childPosition, final boolean isLastChild, View convertView, final ViewGroup parent) {
        final ChildViewHolder cholder;
        if (convertView == null) {
            cholder = new ChildViewHolder();
            convertView = View.inflate(context, R.layout.item_shopcart_product, null);
            cholder.cb_check = (CheckBox) convertView.findViewById(R.id.check_box);
            cholder.tv_product_desc = (TextView) convertView.findViewById(R.id.tv_intro);
            cholder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            cholder.iv_increase = (TextView) convertView.findViewById(R.id.tv_add);
            cholder.iv_decrease = (TextView) convertView.findViewById(R.id.tv_reduce);
            cholder.tv_count = (TextView) convertView.findViewById(R.id.tv_num);
            cholder.rl_no_edtor = (RelativeLayout) convertView.findViewById(R.id.rl_no_edtor);
            cholder.tv_color_size = (TextView) convertView.findViewById(R.id.tv_color_size);
            cholder.tv_discount_price = (TextView) convertView.findViewById(R.id.tv_discount_price);
            cholder.tv_buy_num = (TextView) convertView.findViewById(R.id.tv_buy_num);
            cholder.ll_edtor = (LinearLayout) convertView.findViewById(R.id.ll_edtor);
            cholder.tv_colorsize = (TextView) convertView.findViewById(R.id.tv_colorsize);
            cholder.tv_goods_delete = (TextView) convertView.findViewById(R.id.tv_goods_delete);
            cholder.iv_adapter_list_pic= (ImageView) convertView.findViewById(R.id.iv_adapter_list_pic);
            convertView.setTag(cholder);
        } else {
            cholder = (ChildViewHolder) convertView.getTag();
        }
        if (groups.get(groupPosition).isEdtor() == true) {
            cholder.ll_edtor.setVisibility(View.VISIBLE);
            cholder.rl_no_edtor.setVisibility(View.GONE);
        } else {
            cholder.ll_edtor.setVisibility(View.GONE);
            cholder.rl_no_edtor.setVisibility(View.VISIBLE);
        }
        final GoodsInfo goodsInfo = (GoodsInfo) getChild(groupPosition, childPosition);
        if (goodsInfo != null) {
            cholder.tv_product_desc.setText(goodsInfo.getDesc());
            cholder.tv_price.setText("￥" + goodsInfo.getPrice() + "");
            cholder.tv_count.setText(goodsInfo.getCount() + "");
            cholder.iv_adapter_list_pic.setImageResource(goodsInfo.getGoodsImg());
            cholder.tv_color_size.setText("颜色：" + goodsInfo.getColor() + "," + "尺码：" + goodsInfo.getSize() + "瓶/斤");
            SpannableString spanString = new SpannableString("￥"+String.valueOf(goodsInfo.getDiscountPrice()));
            StrikethroughSpan span = new StrikethroughSpan();
            spanString.setSpan(span, 0, String.valueOf(goodsInfo.getDiscountPrice()).length()+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if(cholder.tv_discount_price.getText().toString().length()>0){
                cholder.tv_discount_price.setText("");
            }
            cholder.tv_discount_price.append(spanString);
            cholder.tv_buy_num.setText("x" + goodsInfo.getCount());
            cholder.cb_check.setChecked(goodsInfo.isChoosed());
            cholder.cb_check.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    goodsInfo.setChoosed(((CheckBox) v).isChecked());
                    cholder.cb_check.setChecked(((CheckBox) v).isChecked());
                    checkInterface.checkChild(groupPosition, childPosition, ((CheckBox) v).isChecked());// 暴露子选接口
                }
            });
            cholder.iv_increase.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doIncrease(groupPosition, childPosition, cholder.tv_count, cholder.cb_check.isChecked());// 暴露增加接口
                }
            });
            cholder.iv_decrease.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doDecrease(groupPosition, childPosition, cholder.tv_count, cholder.cb_check.isChecked());// 暴露删减接口
                }
            });
            //删除 购物车
            cholder.tv_goods_delete.setOnClickListener(new OnClickListener() {
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
     * 组元素绑定器
     */
    private class GroupViewHolder {
        CheckBox cb_check;
        TextView tv_group_name;
        Button store_edtor;
    }

    /**
     * 子元素绑定器
     */
    private class ChildViewHolder {
        CheckBox cb_check;
        ImageView iv_adapter_list_pic;
        TextView tv_product_name;
        TextView tv_product_desc;
        TextView tv_price;
        TextView iv_increase;
        TextView tv_count;
        TextView iv_decrease;
        RelativeLayout rl_no_edtor;
        TextView tv_color_size;
        TextView tv_discount_price;
        TextView tv_buy_num;
        LinearLayout ll_edtor;
        TextView tv_colorsize;
        TextView tv_goods_delete;
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
        public void checkGroup(int groupPosition, boolean isChecked);

        /**
         * 子选框状态改变时触发的事件
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param isChecked     子元素选中与否
         */
        public void checkChild(int groupPosition, int childPosition, boolean isChecked);
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
        public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        /**
         * 删减操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        /**
         * 删除子item
         * @param groupPosition
         * @param childPosition
         */
        public void childDelete(int groupPosition,int childPosition);
    }

    /**
     * 监听编辑状态
     */
    public interface GroupEdtorListener{
        public void groupEdit(int groupPosition);
    }
}

```
接下来就是购物车界面代码：
```java
package com.zy.tbshoppingcart;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.zy.tbshoppingcart.adapter.ShopcartAdapter;
import com.zy.tbshoppingcart.entity.GoodsInfo;
import com.zy.tbshoppingcart.entity.StoreInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
/**
 * 模仿淘宝购物车界面
 */
public class ShopcartActivity extends Activity implements ShopcartAdapter.CheckInterface,ShopcartAdapter.ModifyCountInterface, ShopcartAdapter.GroupEdtorListener {
    @InjectView(R.id.back)
    ImageView back;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.subtitle)
    TextView subtitle;
    @InjectView(R.id.top_bar)
    LinearLayout topBar;
    @InjectView(R.id.exListView)
    ExpandableListView exListView;
    @InjectView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @InjectView(R.id.all_chekbox)
    CheckBox allChekbox;
    @InjectView(R.id.tv_delete)
    TextView tvDelete;
    @InjectView(R.id.tv_go_to_pay)
    TextView tvGoToPay;
    @InjectView(R.id.ll_shar)
    LinearLayout llShar;
    @InjectView(R.id.ll_info)
    LinearLayout llInfo;
    @InjectView(R.id.tv_share)
    TextView tvShare;
    @InjectView(R.id.tv_save)
    TextView tvSave;
    private Context context;
    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量
    private ShopcartAdapter selva;
    private List<StoreInfo> groups = new ArrayList<StoreInfo>();// 组元素数据列表
    private Map<String, List<GoodsInfo>> children = new HashMap<String, List<GoodsInfo>>();// 子元素数据列表
    private int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        context = this;
        initDatas();
        ButterKnife.inject(this);
        initEvents();
    }
    private void initEvents() {
        selva = new ShopcartAdapter(groups, children, this);
        selva.setCheckInterface(this);// 关键步骤1,设置复选框接口
        selva.setModifyCountInterface(this);// 关键步骤2,设置数量增减接口
        selva.setmListener(this);
        exListView.setAdapter(selva);
        for (int i = 0; i < selva.getGroupCount(); i++) {
            exListView.expandGroup(i);// 关键步骤3,初始化时，将ExpandableListView以展开的方式呈现
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        setCartNum();
    }
    /**
     * 设置购物车产品数量
     */
    private void setCartNum() {
        int count=0;
        for (int i = 0; i < groups.size(); i++) {
groups.get(i).setChoosed(allChekbox.isChecked());
            StoreInfo group = groups.get(i);
            List<GoodsInfo> childs = children.get(group.getId());
            for (GoodsInfo goodsInfo:childs){
                count+=1;
            }
        }
        title.setText("购物车" + "(" + count + ")");
    }
    /**
     * 模拟数据<br>
     * 遵循适配器的数据列表填充原则，组元素被放在一个List中，对应的组元素下辖的子元素被放在Map中，<br>
     * 其键是组元素的Id(通常是一个唯一指定组元素身份的值)
     */
    private void initDatas() {
        for (int i = 0; i < 3; i++) {
            groups.add(new StoreInfo(i + "", "天猫店铺" + (i + 1) + "号店"));
            List<GoodsInfo> products = new ArrayList<GoodsInfo>();
            for (int j = 0; j <= i; j++) {
             int[]   img={R.drawable.goods1,R.drawable.goods2,R.drawable.goods3,R.drawable.goods4,R.drawable.goods5,R.drawable.goods6};
                products.add(new GoodsInfo(j + "", "商品", groups.get(i).getName() + "的第" + (j + 1) + "个商品", 12.00 + new Random().nextInt(23), new Random().nextInt(5) + 1, "豪华", "1", img[i*j],6.00+ new Random().nextInt(13)));
            }
            children.put(groups.get(i).getId(), products);// 将组元素的一个唯一值，这里取Id，作为子元素List的Key
        }
    }

    /**
     * 删除操作<br>
     * 1.不要边遍历边删除，容易出现数组越界的情况<br>
     * 2.现将要删除的对象放进相应的列表容器中，待遍历完后，以removeAll的方式进行删除
     */
    protected void doDelete() {
        List<StoreInfo> toBeDeleteGroups = new ArrayList<StoreInfo>();// 待删除的组元素列表
        for (int i = 0; i < groups.size(); i++) {
            StoreInfo group = groups.get(i);
            if (group.isChoosed()) {
                toBeDeleteGroups.add(group);
            }
            List<GoodsInfo> toBeDeleteProducts = new ArrayList<GoodsInfo>();// 待删除的子元素列表
            List<GoodsInfo> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++) {
                if (childs.get(j).isChoosed()) {
                    toBeDeleteProducts.add(childs.get(j));
                }
            }
            childs.removeAll(toBeDeleteProducts);
        }
        groups.removeAll(toBeDeleteGroups);
        selva.notifyDataSetChanged();
        calculate();
    }
    /**购物车数量增加***/
    @Override
    public void doIncrease(int groupPosition, int childPosition,View showCountView, boolean isChecked) {
        GoodsInfo product = (GoodsInfo) selva.getChild(groupPosition,
                childPosition);
        int currentCount = product.getCount();
        currentCount++;
        product.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        selva.notifyDataSetChanged();
        calculate();
    }
    /***购物车数量减少****/
    @Override
    public void doDecrease(int groupPosition, int childPosition,View showCountView, boolean isChecked) {
        GoodsInfo product = (GoodsInfo) selva.getChild(groupPosition,
                childPosition);
        int currentCount = product.getCount();
        if (currentCount == 1)
            return;
        currentCount--;
        product.setCount(currentCount);
        ((TextView) showCountView).setText(currentCount + "");
        selva.notifyDataSetChanged();
        calculate();
    }
    /****child的商品删除****/
    @Override
    public void childDelete(int groupPosition, int childPosition) {
children.get(groups.get(groupPosition).getId()).remove(childPosition);
        StoreInfo group = groups.get(groupPosition);
        List<GoodsInfo> childs = children.get(group.getId());
        if (childs.size() == 0) {//当child全删除光后移除group
            groups.remove(groupPosition);
        }
        selva.notifyDataSetChanged();
        handler.sendEmptyMessage(0);//通知title的购物车数量改变
    }
    /****判断group是否选中****/
    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        StoreInfo group = groups.get(groupPosition);
        List<GoodsInfo> childs = children.get(group.getId());
        for (int i = 0; i < childs.size(); i++) {
            childs.get(i).setChoosed(isChecked);
        }
        if (isAllCheck())
            allChekbox.setChecked(true);
        else
            allChekbox.setChecked(false);
        selva.notifyDataSetChanged();
        calculate();
    }
    /****child是否勾选****/
    @Override
    public void checkChild(int groupPosition, int childPosiTion,boolean isChecked) {
        boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
        StoreInfo group = groups.get(groupPosition);
        List<GoodsInfo> childs = children.get(group.getId());
        for (int i = 0; i < childs.size(); i++) {
            // 不全选中
            if (childs.get(i).isChoosed() != isChecked) {
                allChildSameState = false;
                break;
            }
        }
        //获取店铺选中商品的总金额
        if (allChildSameState) {
            group.setChoosed(isChecked);// 如果所有子元素状态相同，那么对应的组元素被设为这种统一状态
        } else {
            group.setChoosed(false);// 否则，组元素一律设置为未选中状态
        }
        if (isAllCheck()) {
            allChekbox.setChecked(true);// 全选
        } else {
            allChekbox.setChecked(false);// 反选
        }
        selva.notifyDataSetChanged();
        calculate();
    }
    /***判断是否全选***/
    private boolean isAllCheck() {
        for (StoreInfo group : groups) {
            if (!group.isChoosed())//有一个没有勾选就为不全选
                return false;
        }
        return true;
    }
    /**
     * 全选与反选
     */
    private void doCheckAll() {
        for (int i = 0; i < groups.size(); i++) {
groups.get(i).setChoosed(allChekbox.isChecked());
            StoreInfo group = groups.get(i);
            List<GoodsInfo> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++) {
childs.get(j).setChoosed(allChekbox.isChecked());
            }
        }
        selva.notifyDataSetChanged();
        calculate();
    }
    /**
     * 统计操作<br>
     * 1.先清空全局计数器<br>
     * 2.遍历所有子元素，只要是被选中状态的，就进行相关的计算操作<br>
     * 3.给底部的textView进行数据填充
     */
    private void calculate() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < groups.size(); i++) {
            StoreInfo group = groups.get(i);
            List<GoodsInfo> childs = children.get(group.getId());
            for (int j = 0; j < childs.size(); j++) {
                GoodsInfo product = childs.get(j);
                if (product.isChoosed()) {
                    totalCount++;
                    totalPrice += product.getPrice() * product.getCount();
                }
            }
        }
        tvTotalPrice.setText("￥" + totalPrice);
        tvGoToPay.setText("去支付(" + totalCount + ")");
    }

    @OnClick({R.id.all_chekbox, R.id.tv_delete, R.id.tv_go_to_pay, R.id.subtitle, R.id.tv_save, R.id.tv_share})
    public void onClick(View view) {
        AlertDialog alert;
        switch (view.getId()) {
            case R.id.all_chekbox:
                doCheckAll();
                break;
            case R.id.tv_delete:
                if (totalCount == 0) {
                    Toast.makeText(context, "请选择要移除的商品", Toast.LENGTH_LONG).show();
                    return;
                }
                alert = new AlertDialog.Builder(context).create();
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
                                doDelete();
                            }
                        });
                alert.show();
                break;
            case R.id.tv_go_to_pay:
                if (totalCount == 0) {
                    Toast.makeText(context, "请选择要支付的商品", Toast.LENGTH_LONG).show();
                    return;
                }
                alert = new AlertDialog.Builder(context).create();
                alert.setTitle("操作提示");
                alert.setMessage("总计:\n" + totalCount + "种商品\n" + totalPrice + "元");
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
                                return;
                            }
                        });
                alert.show();
                break;
            case R.id.subtitle:
                if (flag == 0) {
                    llInfo.setVisibility(View.GONE);
                    tvGoToPay.setVisibility(View.GONE);
                    llShar.setVisibility(View.VISIBLE);
                    subtitle.setText("完成");
                } else if (flag == 1) {
                    llInfo.setVisibility(View.VISIBLE);
                    tvGoToPay.setVisibility(View.VISIBLE);
                    llShar.setVisibility(View.GONE);
                    subtitle.setText("编辑");
                }
       flag = (flag + 1) % 2;//其余得到循环执行上面2个不同的功能
                break;
            case R.id.tv_share:
                if (totalCount == 0) {
                    Toast.makeText(context, "请选择要分享的商品", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(ShopcartActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_save:
                if (totalCount == 0) {
                    Toast.makeText(context, "请选择要保存的商品", Toast.LENGTH_LONG).show();
                    return;}
                Toast.makeText(ShopcartActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    @Override
    public void groupEdit(int groupPosition) {
        //设置group的监听状态
        groups.get(groupPosition).setIsEdtor(true);
        selva.notifyDataSetChanged();
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //删除购物车后动态改变数量
            setCartNum();
        }
    };
}

```
