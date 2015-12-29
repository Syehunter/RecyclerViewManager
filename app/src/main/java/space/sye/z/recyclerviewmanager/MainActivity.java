package space.sye.z.recyclerviewmanager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import space.sye.z.library.adapter.RefreshRecyclerViewAdapter;
import space.sye.z.library.listener.OnBothRefreshListener;
import space.sye.z.library.listener.OnLoadMoreListener;
import space.sye.z.library.listener.OnPullDownListener;
import space.sye.z.library.manager.RecyclerMode;
import space.sye.z.library.manager.RecyclerViewManager;
import space.sye.z.library.RefreshRecyclerView;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> mDatas;
    private RefreshRecyclerView recyclerView;

    private static final int PULL_DOWN = 1;
    private static final int LOAD_MORE = 2;
    private int counts = 10;
    private MyAdapter myAdapter;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatas = new ArrayList<>();
        for (int i = 0; i < counts; i++){
            mDatas.add("Item " + i);
        }

        View header = View.inflate(this, R.layout.recycler_header, null);
        View header2 = View.inflate(this, R.layout.recycler_header2, null);
        View footer = View.inflate(this, R.layout.recycler_footer, null);

        recyclerView = (RefreshRecyclerView) findViewById(R.id.recyclerView);
        myAdapter = new MyAdapter();

        RecyclerViewManager.with(myAdapter, new LinearLayoutManager(this))
                .setMode(RecyclerMode.BOTH)
                .addHeaderView(header)
                .addHeaderView(header2)
                .addFooterView(footer)
                .setOnBothRefreshListener(new OnBothRefreshListener() {
                    @Override
                    public void onPullDown() {
                        //模拟网络请求
                        Message msg = new Message();
                        msg.what = PULL_DOWN;
                        mHandler.sendMessageDelayed(msg, 2000);
                    }

                    @Override
                    public void onLoadMore() {
                        //模拟网络请求
                        if (page > 5) {
                            //模拟共有5页数据
                            Toast.makeText(MainActivity.this, "No more datas!", Toast.LENGTH_SHORT).show();
                            recyclerView.onRefreshCompleted();
                            return;
                        }
                        page++;
                        Message msg = new Message();
                        msg.what = LOAD_MORE;
                        mHandler.sendMessageDelayed(msg, 2000);
                    }
                })
//                .setOnPullDownListener(new OnPullDownListener() {
//                    @Override
//                    public void onPullDown() {
//                        Message msg = new Message();
//                        msg.what = PULL_DOWN;
//                        mHandler.sendMessageDelayed(msg, 2000);
//                    }
//                })
//                .setOnLoadMoreListener(new OnLoadMoreListener() {
//                    @Override
//                    public void onLoadMore() {
//                        //模拟网络请求
//                        if (page > 5) {
//                            //模拟共有5页数据
//                            Toast.makeText(MainActivity.this, "No more datas!", Toast.LENGTH_SHORT).show();
//                            recyclerView.onRefreshCompleted();
//                            return;
//                        }
//                        page++;
//                        Message msg = new Message();
//                        msg.what = LOAD_MORE;
//                        mHandler.sendMessageDelayed(msg, 2000);
//                    }
//                })
                .setOnItemClickListener(new RefreshRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                        Toast.makeText(MainActivity.this, "item" + position, Toast.LENGTH_SHORT).show();
                    }
                })
                .into(recyclerView, this);
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case PULL_DOWN:
                    mDatas.add(0, "new Item");
                    break;
                case LOAD_MORE:
                    for (int i = 0; i < 10; i++){
                        mDatas.add("item" + (counts + i));
                    }
                    counts += 10;
                    break;
            }
            recyclerView.onRefreshCompleted();
            myAdapter.notifyDataSetChanged();
        }
    };

    private class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView tv_item;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_item = (TextView) itemView.findViewById(R.id.tv_item);
        }

    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(MainActivity.this).
                    inflate(R.layout.recycler_item, parent, false);
            MyViewHolder holder = new MyViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv_item.setText(mDatas.get(position));
        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       switch (id){
           case R.id.action_linear:
               RecyclerViewManager.setLayoutManager(new LinearLayoutManager(this));
               break;
           case R.id.action_grid:
               RecyclerViewManager.setLayoutManager(new GridLayoutManager(this, 3));
               break;
           case R.id.action_staggered:
               RecyclerViewManager.setLayoutManager(new StaggeredGridLayoutManager(
                       2, StaggeredGridLayoutManager.VERTICAL));
               break;
       }

        return super.onOptionsItemSelected(item);
    }
}
