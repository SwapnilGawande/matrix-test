package com.kaltura.matrixtest.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kaltura.matrixtest.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by swapnil.d.gawande on 10/12/17.
 * Adapter for recyclerview which will show N*N matrix
 */
public class MatrixAdapter extends RecyclerView.Adapter<MatrixAdapter.ViewHolder> {
    private ArrayList<Integer> matrixData;
    private int matrixSize;
    private Context context;

    /**
     * Constructor
     *
     * @param context    the Context of activity
     * @param matrixSize the size of matrix i.e. 'N'
     * @param matrixData the matrix data which is list representation of 2D array
     */
    public MatrixAdapter(Context context, int matrixSize, ArrayList<Integer> matrixData) {
        this.context = context;
        this.matrixSize = matrixSize;
        this.matrixData = matrixData;
    }

    @Override
    public MatrixAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MatrixAdapter.ViewHolder viewHolder, int position) {
        viewHolder.tvGridItem.setText(String.valueOf(matrixData.get(position)));
    }

    @Override
    public int getItemCount() {
        return (matrixData == null) ? 0 : matrixData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvGridItem;

        public ViewHolder(View view) {
            super(view);
            tvGridItem = view.findViewById(R.id.tvGridItem);
        }
    }

    /**
     * Method to transpose the matrix, it has the current position of item, it calculates the new position
     * and swaps the item, also animation is shown using notifyItemRangeChanged while transposing the matrix.
     */
    public void transposeMatrix() {
        int size = matrixData.size() - 1;
        ArrayList<Integer> changedPositions = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            if (!changedPositions.contains(i)) {
                int newPosition = getNewPosition(i, size);
                Collections.swap(matrixData, i, newPosition);
                notifyItemMoved(i, newPosition);
                changedPositions.add(newPosition);
            }
        }
        notifyItemRangeChanged(0, size);
    }

    /**
     * Method to calculate new position of an item in matrix, the logic to find new position is to multiply
     * the current position with matrix size, if the result is greater than max index of list then
     * recursively minus the max index from new position till the time new position is less than or equals to max index.
     *
     * @param currentPosition the current position of item
     * @param maxIndex        the max index of list
     * @return the new position of item
     */
    private int getNewPosition(int currentPosition, int maxIndex) {
        int newPosition = currentPosition * matrixSize;
        while (newPosition > maxIndex) {
            newPosition -= maxIndex;
        }
        return newPosition;
    }
}