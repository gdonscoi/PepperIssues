package net.caiena.github.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.caiena.github.R;
import net.caiena.github.model.Repository;

import java.util.ArrayList;

public class AdapterRepos extends RecyclerView.Adapter<AdapterRepos.ViewHolder> {

    private ArrayList<Repository> repositories;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameRepo;
        public ImageView statusRepo;
        public RelativeLayout container;

        public ViewHolder(View v) {
            super(v);
            container = (RelativeLayout) v.findViewById(R.id.container_item_repo);
            nameRepo = (TextView) container.findViewById(R.id.name_repo);
            statusRepo = (ImageView) container.findViewById(R.id.status_repo);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            Toast.makeText(view.getContext(), getAdapterPosition() + " position = " + getPosition(), Toast.LENGTH_SHORT).show();
//            Intent i = new Intent(view.getContext(), ListActivity.class);
//            i.putExtra("directory", LoadController.getInstance(null).getItemFromCategory(this.directory, getAdapterPosition()));
//            i.putExtra("title", ((TextView) view.findViewById(R.id.txtCategoryTitle)).getText());
//            view.getContext().startActivity(i);
//            ((Activity) view.getContext()).overridePendingTransition(R.anim.enter_from_right, R.anim.enter_from_left);
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

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameRepo.setText(repositories.get(position).nameFull);
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
