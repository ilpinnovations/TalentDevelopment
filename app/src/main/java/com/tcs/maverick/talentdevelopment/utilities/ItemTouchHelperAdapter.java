package com.tcs.maverick.talentdevelopment.utilities;

/**
 * Created by abhi on 3/22/2016.
 */
public interface ItemTouchHelperAdapter {
    void onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
