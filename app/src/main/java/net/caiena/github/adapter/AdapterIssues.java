package net.caiena.github.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.caiena.github.R;
import net.caiena.github.activity.IssueDetailsActivity;
import net.caiena.github.model.bean.Issue;
import net.caiena.github.model.bean.Label;

import java.util.ArrayList;

public class AdapterIssues extends RecyclerView.Adapter<AdapterIssues.ViewHolder> {

    private ArrayList<Issue> issues;
    private Context context;
    private TextView labelText;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView titleIssue;
        public PredicateLayout containerLabels;
        private ArrayList<Issue> issues;

        public ViewHolder(ArrayList<Issue> issues, View v) {
            super(v);
            titleIssue = (TextView) v.findViewById(R.id.title_issue);
            containerLabels = (PredicateLayout) v.findViewById(R.id.container_label_issue);
            this.issues = issues;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(view.getContext(), IssueDetailsActivity.class);
            i.putExtra("issue", issues.get(getAdapterPosition()));
            view.getContext().startActivity(i);
        }
    }

    public AdapterIssues(ArrayList<Issue> issues, Context context) {
        this.context = context;
        this.issues = issues;
        setHasStableIds(true);
    }

    @Override
    public AdapterIssues.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_issue_list, parent, false);

        return new ViewHolder(issues, v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titleIssue.setText(issues.get(position).title);

        if (holder.containerLabels.getChildCount() > 0) {
            holder.containerLabels.removeAllViews();
        }
        for (Label label : issues.get(position).labels) {
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

    }

    @Override
    public long getItemId(int position) {
        return issues.get(position).number;
    }

    @Override
    public int getItemCount() {
        return issues.size();
    }

    public void moveItem(int start, int end) {
        int max = Math.max(start, end);
        int min = Math.min(start, end);
        if (min >= 0 && max < issues.size()) {
            Issue item = issues.remove(min);
            issues.add(max, item);
            notifyItemMoved(min, max);
        }
    }

    public int getPositionForId(long id) {
        int index = issues.size();
        for (int i = 0; i < index; i++) {
            if (issues.get(i).number == id) {
                return i;
            }
        }
        return -1;
    }

}
