package net.caiena.github.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.caiena.github.R;
import net.caiena.github.activity.IssuesActivity;
import net.caiena.github.model.bean.Repository;

import java.util.ArrayList;

public class AdapterRepos extends RecyclerView.Adapter<AdapterRepos.ViewHolder> {

    private ArrayList<Repository> repositories;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameRepo;
        public TextView descriptionRepo;
        public TextView ownerRepo;
        public ImageView statusRepo;
        public RelativeLayout container;
        private ArrayList<Repository> repositories;

        public ViewHolder(ArrayList<Repository> repositories ,View v) {
            super(v);
            container = (RelativeLayout) v.findViewById(R.id.container_item_repo);
            nameRepo = (TextView) container.findViewById(R.id.name_repo);
            descriptionRepo = (TextView) container.findViewById(R.id.description_repo);
            ownerRepo = (TextView) container.findViewById(R.id.owner_repo);
            statusRepo = (ImageView) container.findViewById(R.id.status_repo);
            this.repositories = repositories;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            Toast.makeText(view.getContext(), getAdapterPosition() + " position = " + getPosition(), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(view.getContext(), IssuesActivity.class);
            i.putExtra("idRepository", repositories.get(getAdapterPosition()).id);
            i.putExtra("nameRepository", repositories.get(getAdapterPosition()).nameFull);
            view.getContext().startActivity(i);
        }
    }

    public AdapterRepos(ArrayList<Repository> repositories, Context context) {
        this.context = context;
        this.repositories = repositories;
    }

    @Override
    public AdapterRepos.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_repo_list, parent, false);

        return new ViewHolder(repositories,v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameRepo.setText(repositories.get(position).nameFull);
        if(!repositories.get(position).description.trim().equals("")) {
            holder.descriptionRepo.setVisibility(View.VISIBLE);
            holder.descriptionRepo.setText(repositories.get(position).description);
        }
        holder.ownerRepo.setText(repositories.get(position).ownerLogin);
        if (repositories.get(position).isPrivate)
            holder.statusRepo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.lock_image));
        else
            holder.statusRepo.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.open_image));
    }


    @Override
    public int getItemCount() {
        return repositories.size();
    }

}
