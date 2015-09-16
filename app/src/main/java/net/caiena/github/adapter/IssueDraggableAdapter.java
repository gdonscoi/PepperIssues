package net.caiena.github.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import net.caiena.github.R;
import net.caiena.github.Util.AbstractDataProvider;
import net.caiena.github.Util.DrawableUtils;
import net.caiena.github.Util.ViewUtils;
import net.caiena.github.activity.IssueDetailsActivity;
import net.caiena.github.model.DAO.IssueDAO;
import net.caiena.github.model.bean.Label;

public class IssueDraggableAdapter extends RecyclerView.Adapter<IssueDraggableAdapter.MyViewHolder> implements DraggableItemAdapter<IssueDraggableAdapter.MyViewHolder> {

    private AbstractDataProvider mProvider;
    private static final String TAG = "IssueDraggableAdapter";
    private TextView labelText;
    private Context context;

    public static class MyViewHolder extends AbstractDraggableItemViewHolder implements View.OnClickListener {
        public FrameLayout mContainer;
        public View mDragHandle;
        private AbstractDataProvider mProvider;

        public RelativeLayout relativeLayout;
        public TextView titleIssue;
        public TextView ownerLogin;
        public PredicateLayout containerLabels;

        public MyViewHolder(View v, AbstractDataProvider mProvider) {
            super(v);
            mContainer = (FrameLayout) v.findViewById(R.id.container);
            relativeLayout = (RelativeLayout) mContainer.findViewById(R.id.container_item_issue);

            mDragHandle = mContainer.findViewById(R.id.drag_handle);

            titleIssue = (TextView) relativeLayout.findViewById(R.id.title_issue);
            ownerLogin = (TextView) relativeLayout.findViewById(R.id.owner_issue);
            containerLabels = (PredicateLayout) relativeLayout.findViewById(R.id.container_label_issue);
            this.mProvider = mProvider;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(view.getContext(), IssueDetailsActivity.class);
            i.putExtra("issue", ((IssueDataProvider.ConcreteData) mProvider.getItem(getAdapterPosition())).issue);
            view.getContext().startActivity(i);
        }
    }

    public IssueDraggableAdapter(AbstractDataProvider dataProvider, Context context) {
        this.context = context;
        mProvider = dataProvider;
        // DraggableItemAdapter requires stable ID, and also
        // have to implement the getItemId() method appropriately.
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return mProvider.getItem(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        return mProvider.getItem(position).getViewType();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.item_issue_list, parent, false);
        return new MyViewHolder(v, mProvider);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AbstractDataProvider.Data item = mProvider.getItem(position);
        holder.titleIssue.setText("#".concat(String.valueOf(((IssueDataProvider.ConcreteData) item.getObject()).issue.number))
                                     .concat(" - ")
                                     .concat(((IssueDataProvider.ConcreteData) item.getObject()).issue.title));
        holder.ownerLogin.setText(((IssueDataProvider.ConcreteData) item.getObject()).issue.ownerLogin);

        if (holder.containerLabels.getChildCount() > 0) {
            holder.containerLabels.removeAllViews();
        }
        for (Label label : ((IssueDataProvider.ConcreteData) item.getObject()).issue.labels) {
            labelText = new TextView(context);
            labelText.setPadding(5, 0, 5, 0);
            labelText.setSingleLine(true);
            labelText.setEllipsize(TextUtils.TruncateAt.END);
            labelText.setTextSize(18);
            labelText.setTypeface(null, Typeface.BOLD);
            labelText.setText(label.name);

            int color = (int) Long.parseLong(label.color, 16);
            labelText.setTextColor(Color.rgb((255 - ((color >> 16) & 0xFF)), (255 - ((color >> 8) & 0xFF)), (255 - ((color >> 0) & 0xFF))));
            labelText.setBackgroundColor(Color.parseColor("#".concat(label.color)));
            holder.containerLabels.addView(labelText, new PredicateLayout.LayoutParams(3, 3));
        }

        // set background resource (target view ID: container)
        final int dragState = holder.getDragStateFlags();

        if (((dragState & RecyclerViewDragDropManager.STATE_FLAG_IS_UPDATED) != 0)) {
            int bgResId;

            if ((dragState & RecyclerViewDragDropManager.STATE_FLAG_IS_ACTIVE) != 0) {
                bgResId = R.drawable.bg_item_dragging_active_state;

                // need to clear drawable state here to get correct appearance of the dragging item.
                DrawableUtils.clearState(holder.mContainer.getForeground());
            } else if ((dragState & RecyclerViewDragDropManager.STATE_FLAG_DRAGGING) != 0) {
                bgResId = R.drawable.bg_item_dragging_state;
            } else {
                bgResId = R.drawable.bg_item_normal_state;
            }

            holder.mContainer.setBackgroundResource(bgResId);
        }
    }

    @Override
    public int getItemCount() {
        return mProvider.getCount();
    }

    @Override
    public void onMoveItem(int fromPosition, int toPosition) {
        Log.d(TAG, "onMoveItem(fromPosition = " + fromPosition + ", toPosition = " + toPosition + ")");

        if (fromPosition == toPosition) {
            return;
        }

        mProvider.moveItem(fromPosition, toPosition);

        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public boolean onCheckCanStartDrag(MyViewHolder holder, int position, int x, int y) {
        // x, y --- relative from the itemView's top-left
        final View containerView = holder.mContainer;
        final View dragHandleView = holder.mDragHandle;

        final int offsetX = containerView.getLeft() + (int) (ViewCompat.getTranslationX(containerView) + 0.5f);
        final int offsetY = containerView.getTop() + (int) (ViewCompat.getTranslationY(containerView) + 0.5f);

        return ViewUtils.hitTest(dragHandleView, x - offsetX, y - offsetY);
    }

    @Override
    public ItemDraggableRange onGetItemDraggableRange(MyViewHolder holder, int position) {
        // no drag-sortable range specified
        return null;
    }

    public void saveOrderList() {
        int listSize = getItemCount();
        SQLiteDatabase db = null;
        try {
            db = IssueDAO.getInstance(context).getConnectionDataBase();
            db.beginTransaction();
            for (int i = 0; i < listSize; i++) {
                if (((IssueDataProvider.ConcreteData) mProvider.getItem(i).getObject()).issue.position != i) {
                    ((IssueDataProvider.ConcreteData) mProvider.getItem(i).getObject()).issue.position = i;
                    IssueDAO.getInstance(context).createOrUpdate(((IssueDataProvider.ConcreteData) mProvider.getItem(i).getObject()).issue);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            return;
        } finally {
            if (db != null) {
                db.endTransaction();
            }
        }
    }
}
