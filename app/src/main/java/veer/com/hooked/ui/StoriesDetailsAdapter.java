package veer.com.hooked.ui;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import veer.com.hooked.R;
import veer.com.hooked.database.Collaborators;
import veer.com.hooked.database.Story;
import veer.com.hooked.utils.GeneralUtils;


public class StoriesDetailsAdapter extends RecyclerView.Adapter<StoriesDetailsAdapter.ViewHolder> {

    private SortedList<Collaborators> collaboratorsList;

    public StoriesDetailsAdapter() {
        this.collaboratorsList = new SortedList<>(Collaborators.class, new SortedListAdapterCallback<Collaborators>(this) {
            @Override
            public int compare(Collaborators o1, Collaborators o2) {
                return Integer.compare(o1.getId(), o2.getId());
            }

            @Override
            public boolean areContentsTheSame(Collaborators oldItem, Collaborators newItem) {
                return oldItem.getMessage().equals(newItem.getMessage());

            }

            @Override
            public boolean areItemsTheSame(Collaborators item1, Collaborators item2) {
                return item1.getTimestamp() == item2.getTimestamp();
            }


            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }
        });
    }

    public void addCollaborators(List<Collaborators> list) {
        collaboratorsList.addAll(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collaborator, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.author.setText(collaboratorsList.get(position).getAuthor());
        holder.date.setText(collaboratorsList.get(position).getDate());
        holder.messsage.setText(collaboratorsList.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return collaboratorsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_author)
        TextView author;
        @BindView(R.id.tv_date)
        TextView date;
        @BindView(R.id.tv_message)
        TextView messsage;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
