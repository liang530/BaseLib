package com.liang530.views.recyclerview.drag;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by hongliang on 16-6-3.
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback{

    public static final float ALPHA_FULL = 1.0f;

    private final ItemTouchHelperAdapterListener mAdapter;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapterListener adapter) {
        super(ItemTouchHelper.UP,ItemTouchHelper.DOWN);
        mAdapter = adapter;
    }

    /**
     * 开启长按拖动
     * @return
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }
    /**
     * 开启滑动
     * @return
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // Set movement flags based on the layout manager
        //设置那些个动作认为是滑动,哪些是拖动
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            //拖动动作
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            //滑动动作
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        } else {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        }

        // Notify the adapter of the move
        mAdapter.itemMove(source.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        // Notify the adapter of the dismissal
        mAdapter.itemDismiss(viewHolder.getAdapterPosition());
    }

    /**
     * 绘制子布局,可以做自己特定的操作
     * @param c
     * @param recyclerView
     * @param viewHolder
     * @param dX
     * @param dY
     * @param actionState
     * @param isCurrentlyActive
     */
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            // Fade out the view as it is swiped out of the parent's bounds
            final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    /**
     * 选中时可以改变item状态(颜色)
     * @param viewHolder
     * @param actionState
     */
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        // We only want the active item to change
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof ItemTouchHelperViewHolderListener) {
                // Let the view holder know that this item is being moved or dragged
                ItemTouchHelperViewHolderListener itemViewHolder = (ItemTouchHelperViewHolderListener) viewHolder;
                itemViewHolder.itemSelected();
            }
        }

        super.onSelectedChanged(viewHolder, actionState);
    }

    /**
     * 清除选中
     * @param recyclerView
     * @param viewHolder
     */
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        viewHolder.itemView.setAlpha(ALPHA_FULL);

        if (viewHolder instanceof ItemTouchHelperViewHolderListener) {
            // Tell the view holder it's time to restore the idle state
            ItemTouchHelperViewHolderListener itemViewHolder = (ItemTouchHelperViewHolderListener) viewHolder;
            itemViewHolder.itemClear();
        }
    }
}
