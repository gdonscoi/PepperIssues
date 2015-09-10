package net.caiena.github.adapter;

import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;

import net.caiena.github.Util.AbstractDataProvider;
import net.caiena.github.model.bean.Issue;

import java.util.ArrayList;
import java.util.List;

public class IssueDataProvider extends AbstractDataProvider {

    private List<ConcreteData> mData;
    private ConcreteData mLastRemovedData;
    private int mLastRemovedPosition = -1;

    public IssueDataProvider(ArrayList<Issue> issues) {
        this.mData = new ArrayList<>();

        for (Issue issue : issues) {
            final long id = mData.size();
            final int viewType = 0;
            final String text = issue.title;
            final int swipeReaction = RecyclerViewSwipeManager.REACTION_CAN_NOT_SWIPE_BOTH;
            mData.add(new ConcreteData(id, viewType, text, swipeReaction,issue));
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Data getItem(int index) {
        if (index < 0 || index >= getCount()) {
            throw new IndexOutOfBoundsException("index = " + index);
        }

        return mData.get(index);
    }

    @Override
    public int undoLastRemoval() {
        if (mLastRemovedData != null) {
            int insertedPosition;
            if (mLastRemovedPosition >= 0 && mLastRemovedPosition < mData.size()) {
                insertedPosition = mLastRemovedPosition;
            } else {
                insertedPosition = mData.size();
            }

            mData.add(insertedPosition, mLastRemovedData);

            mLastRemovedData = null;
            mLastRemovedPosition = -1;

            return insertedPosition;
        } else {
            return -1;
        }
    }

    @Override
    public void moveItem(int fromPosition, int toPosition) {
        if (fromPosition == toPosition) {
            return;
        }

        final ConcreteData item = mData.remove(fromPosition);

        mData.add(toPosition, item);
        mLastRemovedPosition = -1;
    }

    @Override
    public void removeItem(int position) {
        //noinspection UnnecessaryLocalVariable
        final ConcreteData removedItem = mData.remove(position);

        mLastRemovedData = removedItem;
        mLastRemovedPosition = position;
    }

    public static final class ConcreteData extends Issue {

        private final long mId;
        private final String mText;
        private final int mViewType;
        private final int mSwipeReaction;
        private boolean mPinnedToSwipeLeft;
        public Issue issue;

        ConcreteData(long id, int viewType, String text, int swipeReaction,Issue issue) {
            mId = id;
            mViewType = viewType;
            mText = makeText(id, text, swipeReaction);
            mSwipeReaction = swipeReaction;
            this.issue = issue;
        }

        private static String makeText(long id, String text, int swipeReaction) {
            final StringBuilder sb = new StringBuilder();

            sb.append(id);
            sb.append(" - ");
            sb.append(text);

            return sb.toString();
        }

        @Override
        public boolean isSectionHeader() {
            return false;
        }

        @Override
        public int getViewType() {
            return mViewType;
        }

        @Override
        public long getId() {
            return mId;
        }

        @Override
        public String toString() {
            return mText;
        }

        @Override
        public int getSwipeReactionType() {
            return mSwipeReaction;
        }

        @Override
        public String getText() {
            return mText;
        }

        @Override
        public boolean isPinnedToSwipeLeft() {
            return mPinnedToSwipeLeft;
        }

        @Override
        public void setPinnedToSwipeLeft(boolean pinedToSwipeLeft) {
            mPinnedToSwipeLeft = pinedToSwipeLeft;
        }
    }
}

