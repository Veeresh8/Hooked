package veer.com.hooked.ui;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import veer.com.hooked.R;
import veer.com.hooked.database.Collaborators;
import veer.com.hooked.database.Story;
import veer.com.hooked.utils.GeneralUtils;


public abstract class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.ViewHolder> {

    private SortedList<Story> sortedStoryList;

    public StoriesAdapter() {
        this.sortedStoryList = new SortedList<>(Story.class, new SortedListAdapterCallback<Story>(this) {
            @Override
            public int compare(Story o1, Story o2) {
                return Integer.compare(o1.getId(), o2.getId());
            }

            @Override
            public boolean areContentsTheSame(Story oldItem, Story newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(Story item1, Story item2) {
                return item1.getId() == item2.getId();
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

    public void addStories(List<Story> storyList) {
        sortedStoryList.addAll(storyList);
    }

    public void clearList() {
        sortedStoryList.clear();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_story, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (position == sortedStoryList.size() - 1) {
            loadMore();
        }

        holder.author.setText(sortedStoryList.get(position).getAuthor());
        holder.date.setText(sortedStoryList.get(position).getDate());
        holder.title.setText(sortedStoryList.get(position).getTitle());
        holder.totalWords.setText(GeneralUtils.getWordCountPrefix(sortedStoryList.get(position).getTotalWords()));
        if (sortedStoryList.get(position).getCollaboratorsList() != null && sortedStoryList.get(position).getCollaboratorsList().size() > 0)
            holder.collaborators.setText(GeneralUtils.getCollaboratorPrefix(GeneralUtils.getCollaborators(sortedStoryList.get(position).getCollaboratorsList())));
        else
            holder.collaborators.setText(R.string.empty_collaborators);
    }

    @Override
    public int getItemCount() {
        return sortedStoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_author)
        TextView author;
        @BindView(R.id.tv_date)
        TextView date;
        @BindView(R.id.tv_story_title)
        TextView title;
        @BindView(R.id.tv_total_words)
        TextView totalWords;
        @BindView(R.id.tv_collaborators)
        TextView collaborators;

        @OnClick(R.id.item_root_layout)
        public void onItemClick() {
            if (getAdapterPosition() == -1)
                return;
            onClick(sortedStoryList.get(getAdapterPosition()).getId());
        }

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public abstract void loadMore();

    public abstract void onClick(int id);
}
