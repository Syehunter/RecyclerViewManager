# RecyclerViewManager
RecyclerViewManager supports PullToRefresh and LoadMore, U can also add headers or footers for RecyclerView.
Also supports onItemClickEvent.

Use setMode() if you want it PullDown or LoadMore only.
With RecyclerMode.PullDown replace setOnRefreshListener() to setOnPullDownListener(), and use setOnLoadMoreListener() in the same way.

	allprojects {
	    repositories {
	        jcenter()
	        maven { url "https://jitpack.io" }
	    }
	}
 	
 	dependencies {
	    compile 'com.github.Syehunter:RecyclerViewManager:0.1.6.4'
	}

![RecyclerViewManagerDemo.gif](http://7xn4z4.com1.z0.glb.clouddn.com/RecyclerViewManager.gif)

Layout:

	<space.sye.z.library.widget.RefreshRecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

Usage in Activity or some others:

	recyclerView = (RefreshRecyclerView) findViewById(R.id.recyclerView);
    MyAdapter myAdapter = new MyAdapter(); //a RecyclerView.Adapter

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
                        Message msg = new Message();
                        msg.what = LOAD_MORE;
                        mHandler.sendMessageDelayed(msg, 2000);
                    }
                })
                .setOnItemClickListener(new RefreshRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(RecyclerView.ViewHolder holder, int position) {
                        Toast.makeText(MainActivity.this, "item" + position, Toast.LENGTH_SHORT).show();
                    }
                })
                .into(recyclerView, this);

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
                    counts = counts + 10;
                    break;
            }
            recyclerView.onRefreshCompleted();
            mAdapter.notifyDataSetChanged();
        }
    };
    
 Use recyclerView.real() to get the real RecyclerView if you want to use methods of it.
    
Simple Introduction for RecyclerViewManager(Chinese): http://z.sye.space/2015/11/23/RecyclerViewManager/
